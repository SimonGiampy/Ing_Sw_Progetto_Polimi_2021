package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.PlayerNumberReply;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	
	private final Socket client;
	private Lobby lobby;
	
	private boolean connected;
	
	//TODO: add more sophisticated condition locks
	private final Object inputLock;
	private final Object outputLock;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	/**
	 * Default constructor
	 * @param client the client connecting.
	 */
	public ClientHandler(Socket client) {
		this.lobby = null;
		this.client = client;
		this.connected = true;
		
		this.inputLock = new Object();
		this.outputLock = new Object();
		
		try {
			this.output = new ObjectOutputStream(client.getOutputStream());
			this.input = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			Lobby.LOGGER.severe(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		Lobby.LOGGER.info("Client connected from " + client.getRemoteSocketAddress());
		try {
			while (!Thread.currentThread().isInterrupted()) {
				synchronized (inputLock) {
					Message message = (Message) input.readObject();
					if (message != null) {
						if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
							lobby.addClient(message.getNickname(), this);
							Lobby.LOGGER.info("new client connected : " + message.toString());
						} else {
							Lobby.LOGGER.info(() -> "Received: " + message);
							lobby.onMessageReceived(message);
						}
					}
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			Lobby.LOGGER.severe("Invalid stream from client");
		} catch (IOException e) {
			Lobby.LOGGER.severe("Invalid IO from client");
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			Lobby.LOGGER.severe("Client " + client.getRemoteSocketAddress() + " connection dropped.");
			disconnect();
		}
	}
	
	/**
	 * Returns the current status of the connection.
	 * @return {@code true} if the connection is still active, {@code false} otherwise.
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Disconnect the socket.
	 */
	public void disconnect() {
		if (connected) {
			try {
				if (!client.isClosed()) {
					client.close();
				}
			} catch (IOException e) {
				Lobby.LOGGER.severe(e.getMessage());
			}
			connected = false;
			Thread.currentThread().interrupt();
			
			lobby.onDisconnect(this);
		}
	}
	
	/**
	 * Sends a message to the client via socket
	 * @param message the message to be sent.
	 */
	public void sendMessage(Message message) {
		try {
			synchronized (outputLock) {
				output.writeObject(message);
				//output.reset();
				Lobby.LOGGER.info(() -> "Sent: " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Lobby.LOGGER.severe(e.getMessage());
			disconnect();
		}
	}

	public int readNumberOfPlayers() {
		try {
			synchronized (inputLock) {
				Message message = (Message) input.readObject();
				if (message != null) {
					if (message.getMessageType() == MessageType.PLAYER_NUMBER_REPLY) {
						PlayerNumberReply mes = (PlayerNumberReply) message;
						return mes.getPlayerNumber();
					}
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			Lobby.LOGGER.severe("Invalid stream from client");
		} catch (IOException e) {
			Lobby.LOGGER.severe("Invalid IO from client");
			e.printStackTrace();
		}
		return 0;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
}
