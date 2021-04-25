package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.login.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
	
	private final Socket socket;
	private final Server server;
	public static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	private Lobby lobby;
	
	private boolean connected;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	
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
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
	
	
	@Override
	public void run() {
		LOGGER.info("Client connected from " + socket.getRemoteSocketAddress());
		lobbyLogin();
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = (Message) input.readObject();
				if (message != null) {
					LOGGER.info("Received: " + message);
					lobby.onMessageReceived(message);
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid class from stream from client");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client caused by disconnection");
			disconnect();
		}
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.severe("Client " + socket.getRemoteSocketAddress() + " connection dropped.");
			disconnect();
		}
	}
	
	public void lobbyLogin() {
		Message message;
		boolean valid = false;
		while(!valid) {
			try {
				sendMessage(new LobbyList(server.getLobbiesDescription()));
				message = (Message) input.readObject();
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.LOBBY_ACCESS) {
					LobbyAccess lobbyAccess = (LobbyAccess) message;
					
					if (lobbyAccess.getLobbyNumber() == 0) { // client logs in as game host
						// create new lobby and add the client. Then let it choose the number of players and configuration file
						lobby = server.createLobby(this);
						lobby.join(this);
						lobby.setUpGameConfig();
						valid = true;
					} else { // client logs in as guest player
						// check if the lobby is not full
						lobby = server.joinLobby(lobbyAccess.getLobbyNumber(), this);
						if (lobby == null) { //if the lobby is full, send negative confirmation
							sendMessage(new LoginConfirmation(false));
						} else { //else send positive confirmation and adds the client to the lobby
							sendMessage(new LoginConfirmation(true));
							lobby.join(this);
							valid = true;
						}
					}
				}
			} catch (ClassCastException | ClassNotFoundException ex) {
				LOGGER.severe("Invalid stream from client: wrong message class");
			} catch (IOException e) {
				LOGGER.severe("Invalid IO from client");
				disconnect();
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
				Server.LOGGER.severe(e.getMessage());
			}
			connected = false;
			Thread.currentThread().interrupt();
			
			//TODO: this generates an error if the host quits before it chooses its nickname
			lobby.onDisconnect(this);
		}
	}
	
	/**
	 * Sends a message to the client via socket
	 * @param message the message to be sent.
	 */
	public void sendMessage(Message message) {
		try {
			output.writeObject(message);
			LOGGER.info("Sent: " + message);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
			disconnect();
		}
	}
	
	public int readNumberOfPlayers() {
		sendMessage(new PlayerNumberRequest());
		try {
			Message message = (Message) input.readObject();
			if (message != null) {
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.PLAYER_NUMBER_REPLY) {
					PlayerNumberReply mes = (PlayerNumberReply) message;
					return mes.getPlayerNumber();
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid stream from client: wrong message class");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client");
			disconnect();
		}
		return 0;
	}
	
	public String readNickname() {
		try {
			Message message = (Message) input.readObject();
			if (message != null) {
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.NICKNAME_REPLY) {
					NicknameReply nicknameReply = (NicknameReply) message;
					return nicknameReply.getNicknameProposal();
				}
			}
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid stream from client: wrong message class");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client");
			disconnect();
		}
		return "cipolla";
	}
	
	public String readGameConfig() {
		try {
			Message message = (Message) input.readObject();
			if (message != null) {
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.GAME_CONFIG_REPLY) {
					GameConfigReply configReply = (GameConfigReply) message;
					return configReply.getGameConfiguration();
				}
			}
			
		} catch (ClassCastException | ClassNotFoundException ex) {
			LOGGER.severe("Invalid stream from client: wrong message class");
		} catch (IOException e) {
			LOGGER.severe("Invalid IO from client");
			disconnect();
		}
		return "cipolla"; //TODO: this should never be called
	}
	
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
}
