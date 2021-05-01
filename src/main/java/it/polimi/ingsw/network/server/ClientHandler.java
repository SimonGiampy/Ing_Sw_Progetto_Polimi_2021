package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Logger;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.login.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Server-side handling of connected players. A group of client handlers form a lobby
 */
public class ClientHandler implements Runnable {
	
	private final Socket socket;
	private final Server server;
	private Lobby lobby;
	
	private boolean connected; // true if connected to the lobby
	
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	
	/**
	 * Constructor for the Server-side runnable that communicates between the Lobby and the Client.
	 * @param server the server that creates this runnable
	 * @param socket the socket created from the connection to the server
	 */
	public ClientHandler(Server server, Socket socket) {
		this.lobby = null;
		this.socket = socket;
		this.server = server;
		this.connected = true;
		
		try {
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			LOGGER.error("Bad in/out stream from socket: " + e.getMessage());
		}
	}
	
	/**
	 * Initial phase is the login process, handled by this class and the lobby.
	 * When the game starts, this runnable listens for new incoming messages
	 */
	@Override
	public void run() {
		LOGGER.info("Client connected from " + socket.getRemoteSocketAddress());
		lobbyLogin(); // login phase
		try {
			// reads the messages incoming from the client and forwards them to the lobby,
			// which in turn forwards them to the game controller
			while (!Thread.currentThread().isInterrupted()) {
				Message message = (Message) inputStream.readObject();
				if (message != null && message.getMessageType() != MessageType.PING) {
					LOGGER.info("Received: " + message);
					lobby.onMessageReceived(message); // message forwarding
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.error("Invalid class from stream from client: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Invalid IO from client caused by disconnection: " + e.getMessage());
			disconnect();
		}
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.error("Client " + socket.getRemoteSocketAddress() + " connection dropped.");
			disconnect();
		}
	}
	
	/**
	 * Login phase: a client accesses a lobby (choosing it from a list of available lobbies). Then chooses a nickname.
	 * In this method the server communicates with the client and handles the clients requests
	 */
	public void lobbyLogin() {
		Message message;
		boolean valid = false;
		while(!valid) {
			try {
				//sends the list of available lobbies in the server to the client connected
				sendMessage(new LobbyList(server.getLobbiesDescription(), server.getLobbyListVersion()));
				message = (Message) inputStream.readObject();
				LOGGER.info("Received: " + message);
				if (message!= null && message.getMessageType() == MessageType.LOBBY_ACCESS) {
					LobbyAccess lobbyAccess = (LobbyAccess) message;
					
					if (lobbyAccess.getLobbyNumber() == 0) { // client logs in as game host
						// create new lobby and add the client. Then let it choose the number of players and configuration file
						lobby = server.createLobby(this);
						lobby.join(this);
						valid = true;
					} else { // client logs in as guest player
						// check if the lobby is not full
						if (server.getLobbyListVersion() == lobbyAccess.getIdVersion()) { // lobby list received is updated on the client
							lobby = server.joinLobby(lobbyAccess.getLobbyNumber(), this);
							if (lobby == null) { //if the lobby is full, send negative confirmation
								sendMessage(new LobbyConfirmation(false));
								valid = false; // repeats the process
							} else { //else send positive confirmation and adds the client to the lobby
								sendMessage(new LobbyConfirmation(true));
								lobby.join(this); // client chooses its nickname and completes the login process
								valid = true;
							}
						} else { // client's lobby list is not updated client side, so it's not valid
							sendMessage(new LobbyConfirmation(false));
							valid = false; // repeats the process
						}

					}
				}
			} catch (ClassNotFoundException ex) {
				LOGGER.error("Invalid stream from client: wrong message class");
			} catch (IOException e) {
				LOGGER.error("Invalid IO from client during login phase");
				disconnect();
				return;
			}
		}
	}
	
	
	/**
	 * disconnects the socket gracefully, and tells the lobby that the client handler is disconnecting
	 */
	public void disconnect() {
		if (connected) {
			try {
				if (!socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				LOGGER.error("Error during socket closing: " + e.getMessage());
			}
			connected = false;
			Thread.currentThread().interrupt();
			
			//disconnects the client handler from the lobby
			if (lobby != null) lobby.onDisconnect(this);
		}
	}
	
	/**
	 * Sends a message to the client via socket
	 * @param message the message to be sent.
	 */
	public void sendMessage(Message message) {
		try {
			outputStream.writeObject(message);
			
			outputStream.reset();
			LOGGER.info("Sent: " + message);
		} catch (IOException e) {
			LOGGER.error("Error in sending a message: " + e.getMessage());
			disconnect();
		}
	}
	
	
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
	
	public ObjectInputStream getInputStream() {
		return inputStream;
	}
}
