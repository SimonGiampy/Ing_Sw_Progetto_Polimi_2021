package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
	
	private final Socket client;
	private final Server server;
	public static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	private Lobby lobby;
	
	private boolean connected;
	
	//TODO: add more sophisticated condition locks
	
	private ReentrantLock lock;
	private Condition inputLock;
	private Condition outputLock;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	/**
	 * Default constructor
	 * @param client the client connecting.
	 */
	public ClientHandler(Server server, Socket client) {
		this.lobby = null;
		this.client = client;
		this.server = server;
		this.connected = true;
		
		lock = new ReentrantLock();
		this.inputLock = lock.newCondition();
		this.outputLock = lock.newCondition();
		
		try {
			this.output = new ObjectOutputStream(client.getOutputStream());
			this.input = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
	
	// need to be modified, login request have to set the nickname in player
	@Override
	public void run() {
		LOGGER.info("Client connected from " + client.getRemoteSocketAddress());
		lobbyLogin();
		try {
			while (!Thread.currentThread().isInterrupted()) {
				synchronized (inputLock) {
					Message message = (Message) input.readObject();
					if (message != null) {
						LOGGER.info(() -> "Received: " + message);
						lobby.onMessageReceived(message);
					}
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid class from stream from client");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client");
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			LOGGER.severe("Client " + client.getRemoteSocketAddress() + " connection dropped.");
			disconnect();
		}
	}
	
	public void lobbyLogin() {
		Message message = null;
		boolean valid = false;
		while(!valid) {
			try {
				sendMessage(new LobbyList(server.getLobbiesDescription()));
				message = (Message) input.readObject();
				if (message.getMessageType() == MessageType.LOBBY_ACCESS) {
					LobbyAccess lobbyAccess = (LobbyAccess) message;
					if (lobbyAccess.getLobbyNumber() == 0) {
						// create new lobby and add the client. Then let it choose the number of players
						Lobby lobby = server.createLobby(this);
						lobby.addClient(this);
						valid = true;
					} else {
						// check if the lobby is not full
						Lobby lobby = server.joinLobby(lobbyAccess.getLobbyNumber(), this);
						if (lobby == null) {
							sendMessage(new LoginConfirmation(false)); //if the lobby is full, send negative confirmation
						} else {
							sendMessage(new LoginConfirmation(true)); //else send positive confirmation and add to the lobby
							lobby.addClient(this);    //add client to the lobby
							valid = true;
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
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
	 * //TODO: Disconnect the socket gracefully
	 */
	public void disconnect() {
		if (connected) {
			try {
				if (!client.isClosed()) {
					client.close();
				}
			} catch (IOException e) {
				LOGGER.severe(e.getMessage());
			}
			connected = false;
			Thread.currentThread().interrupt();
			
			//lobby.onDisconnect(this);
		}
	}
	
	/**
	 * Sends a message to the client via socket
	 * @param message the message to be sent.
	 */
	public synchronized void sendMessage(Message message) {
		try {
			output.writeObject(message);
			LOGGER.info(() -> "Sent: " + message);
			
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
			disconnect();
		}
	}
	
	public int readNumberOfPlayers() {

		sendMessage(new PlayerNumberRequest());
		try {
			Message message = (Message) input.readObject();
			
			if (message != null) {
				if (message.getMessageType() == MessageType.PLAYER_NUMBER_REPLY) {
					PlayerNumberReply mes = (PlayerNumberReply) message;
					return mes.getPlayerNumber();
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid stream from client");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client");
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized String readNickname() {
		try {
			Message message = (Message) input.readObject();
			if (message != null) {
				if (message.getMessageType() == MessageType.NICKNAME_REPLY) {
					NicknameReply nicknameReply = (NicknameReply) message;
					return nicknameReply.getNickname();
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "cipolla";
	}
	
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
}
