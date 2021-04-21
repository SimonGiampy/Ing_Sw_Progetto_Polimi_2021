package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {
	
	private final Socket client;
	private final SocketServer socketServer;
	
	private boolean connected;
	
	//TODO: add more sophisticated condition locks
	private final Object inputLock;
	private final Object outputLock;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	/**
	 * Default constructor
	 * @param socketServer the socket of the server.
	 * @param client       the client connecting.
	 */
	public SocketClientHandler(SocketServer socketServer, Socket client) {
		this.socketServer = socketServer;
		this.client = client;
		this.connected = true;
		
		this.inputLock = new Object();
		this.outputLock = new Object();
		
		try {
			this.output = new ObjectOutputStream(client.getOutputStream());
			this.input = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			Server.LOGGER.severe(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		Server.LOGGER.info("Client connected from " + client.getRemoteSocketAddress());
		try {
			while (!Thread.currentThread().isInterrupted()) {
				synchronized (inputLock) {
					Message message = (Message) input.readObject();
					
					if (message != null) {
						if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
							socketServer.addClient(message.getNickname(), this);
							Server.LOGGER.info("new client connected : " + message.toString());
						} else {
							Server.LOGGER.info(() -> "Received: " + message);
							socketServer.onMessageReceived(message);
						}
					}
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			Server.LOGGER.severe("Invalid stream from client");
		} catch (IOException e) {
			Server.LOGGER.severe("Invalid IO from client");
		}
		try {
			client.close();
		} catch (IOException e) {
			Server.LOGGER.severe("Client " + client.getRemoteSocketAddress() + " connection dropped.");
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
				Server.LOGGER.severe(e.getMessage());
			}
			connected = false;
			Thread.currentThread().interrupt();
			
			socketServer.onDisconnect(this);
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
				output.reset();
				Server.LOGGER.info(() -> "Sent: " + message);
			}
		} catch (IOException e) {
			Server.LOGGER.severe(e.getMessage());
			disconnect();
		}
	}
	
	
}
