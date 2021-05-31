package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ServerSideController;
import it.polimi.ingsw.network.Logger;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.generic.DisconnectionMessage;
import it.polimi.ingsw.network.messages.login.*;
import it.polimi.ingsw.network.messages.generic.GenericMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Lobby {

	private final ArrayList<User> clients;
	private final List<String> nicknames;
	private int numberOfPlayers;
	private boolean gameStarted; // flag indicating if the match is started
	
	private ClientHandler host;
	private final Server server;
	private final List<ClientHandler> handlersList;
	private ServerSideController serverSideController;
	
	public static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
	
	/**
	 * Lobby that handles a single game and the connected clients
	 * @param server the server that hosts the lobby
	 * @param host the client handler that connects first is the game host and decides the match parameters
	 */
	public Lobby(Server server, ClientHandler host) {
		clients = new ArrayList<>();
		nicknames = Collections.synchronizedList(new ArrayList<>());
		handlersList = Collections.synchronizedList(new ArrayList<>());
		gameStarted = false;
		this.host = host;
		this.server = server;
		
	}
	
	/**
	 * Method called by the server for the initialization of the lobby (the host chooses the number of players).
	 * Then the host connects to the lobby and joins it. After this method finishes its execution, the server shows the lobby to
	 * the other clients that connect to the server
	 */
	public void setUpLobby() throws IOException, ClassNotFoundException {
		host.sendMessage(new PlayerNumberRequest());
		boolean answer = false;
		Message message;
		do {
			message = (Message) host.getInputStream().readObject();
			if (message != null && message.getMessageType() != MessageType.PING) {
				answer = true;
			}
		} while (!answer);
		LOGGER.info("Received: " + message);
		if (message.getMessageType() == MessageType.PLAYER_NUMBER_REPLY) {
			PlayerNumberReply mes = (PlayerNumberReply) message;
			numberOfPlayers = mes.getPlayerNumber();
			serverSideController = new ServerSideController(this, numberOfPlayers);
			connectClient(host);
		}
	}
	
	
	/**
	 * method called from the client handler when this lobby isn't full. The client connects to the lobby.
	 * Then the client waits for choosing a nickname.
	 * @param client that is connecting to the game lobby
	 */
	public void connectClient(ClientHandler client) {
		synchronized (handlersList) {
			handlersList.add(client);
		}
		LOGGER.info("new client connected to the lobby");
	}
	
	
	/**
	 * Method called by the ClientHandler, that adds itself to the lobby. Then the procedure for choosing a valid nickname is started.
	 * The client chooses a nickname, which must be unique in the lobby, so no other user is allowed to use their nickname
	 * @param client that accesses this lobby after connecting
	 */
	public void join(ClientHandler client)  {
		VirtualView view = new VirtualView(client);
		String nick = null;
		boolean valid = false;
		do {
			view.askNickname(); // asks for a nickname (message from the server to the client)
			Message message;
			try {
				boolean answer = false;
				do {
					message = (Message) client.getInputStream().readObject();
					if (message != null && message.getMessageType() != MessageType.PING) {
						answer = true;
					}
				} while (!answer);
				
				// checks if the nickname is valid or not
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.NICKNAME_REPLY) {
					NicknameReply reply = (NicknameReply) message;
					nick = reply.getNicknameProposal();
					synchronized (nicknames) {
						valid = !nicknames.contains(nick); //checks if the nickname isn't already chosen by another client
					}
					view.showNicknameConfirmation(valid); // sends the login result to the client, otherwise
				}
				
			}  catch (ClassCastException | ClassNotFoundException ex) {
				LOGGER.error("Invalid class from stream from client");
			} catch (IOException e) {
				LOGGER.error("Invalid IO from client caused by disconnection");
				client.disconnect();
				return;
			}
			
		} while (!valid);
		
		assert nick != null;
		synchronized (nicknames) {
			nicknames.add(nick); // memorizes the accepted nickname
		}
		
		client.setLobby(this);
		clients.add(new User(nick, client, view));
		
		if (clients.size() == numberOfPlayers && !gameStarted) { // starts the lobby when it's full
			server.startLobby(this);
		}
		
		// updates the other connected clients that a new user entered the game lobby
		broadcastMessage(new GenericMessage(nick + " joined the lobby!"), client);
	}
	
	
	/**
	 * sets up the match parameters via the server side controller
	 */
	public void setUpMatch() {
		// creates an hashmap of nicknames and relative virtual views
		HashMap<String, VirtualView> virtualViewHashMap = new HashMap<>();
		for (User user: clients) {
			virtualViewHashMap.put(user.getNickname(), user.getView());
		}
		serverSideController.setVirtualViews(virtualViewHashMap);
	}
	
	/**
	 * Forwards a received message from the client to the ServerSideController
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		serverSideController.onMessageReceived(message);
	}
	
	
	/**
	 * handle client disconnection before the match starts and after the match is started.
	 * In the case the disconnected client was the host, then the lobby automatically picks a new host and continue the login phase
	 * @param client that gets disconnected from the lobby
	 */
	public synchronized void onDisconnect(ClientHandler client) {
		synchronized (handlersList) {
			if (handlersList.contains(client)) { // client was connected
				
				User user = null;
				if (client.equals(host)) { // the host disconnected from the lobby
					for (User u: clients) {
						if (u.getHandler().equals(host)) {
							user = u;
						}
					}
					if (gameStarted && user != null) { // game was already started
						haltGame(user);
					} else if (handlersList.size() == 1) { // the host was the only one connected
						handlersList.clear();
						server.removeLobby(this);
					} else { // there were guests connected
						host = handlersList.get(1); //new host is the second client that entered the lobby
						if (user != null) { // user chose its nickname before the host disconnected
							nicknames.remove(user.getNickname());
							clients.remove(user);
							String text = "The host (" + user.getNickname() + ") left the lobby: new host role assigned.";
							broadcastMessage(new DisconnectionMessage(text, false), host);
						} else { // the user didn't choose its nickname before the host disconnected
							broadcastMessage(new DisconnectionMessage("The host left the lobby: new host role assigned.", false), host);
						}
						handlersList.remove(client); //removes host
						host.sendMessage(new GenericMessage("You are the new lobby host."));
					}
					
				} else { // a guest disconnected from the lobby
					for (User u: clients) {
						if (u.getHandler().equals(client)) {
							user = u;
						}
					}
					if (gameStarted && user != null) { // if the game already started, halts it abruptly
						haltGame(user);
					} else if (user != null) { // the client had already chosen a nickname but the game didn't start
						LOGGER.info("Removed " + user.getNickname() + " from the connected players list");
						nicknames.remove(user.getNickname());
						broadcastMessage(new DisconnectionMessage(user.getNickname() + " disconnected from the lobby", false));
						clients.remove(user);
						handlersList.remove(client);
					} else { // the client disconnected before choosing a nickname
						LOGGER.info("Removed a client from the lobby");
						handlersList.remove(client);
					}
				}
			}
		}
	}
	
	/**
	 * Message broadcast directed to all the connected users in the game.
	 * The message is sent to all the players who have chosen a nickname
	 * @param message to be broadcast
	 */
	public void broadcastMessage(Message message) {
		for (User user: clients) {
			user.getHandler().sendMessage(message);
		}
	}
	
	
	/**
	 *  Message broadcast directed to all the connected users in the game, except the one with
	 *  the specified handler in input
	 * @param message to be broadcast
	 * @param except the clientHandler who must not receive any message (it's usually the one who starts the event)
	 */
	public void broadcastMessage(Message message, ClientHandler except) {
		for (User user: clients) {
			if (!user.getHandler().equals(except)) {
				user.getHandler().sendMessage(message);
			}
		}
	}
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	/**
	 * uses the list of clients handlers
	 * @return number of currently connected clients (that didn't necessarily choose a nickname)
	 */
	public int getConnectedClients() {
		int num;
		synchronized (handlersList) {
			num = handlersList.size();
		}
		return num;
	}
	
	/**
	 * starts the match and sends a broadcast message to the players connected
	 */
	public void matchStart() {
		LOGGER.info("Match started");
		gameStarted = true;
	}
	
	/**
	 * if a client disconnects from the lobby when the game already started, the lobby closes it abruptly
	 * @param user disconnected from the lobby
	 */
	private void haltGame(User user) {
		new Thread(() -> {
			for (ClientHandler h: handlersList) {
				if (!h.equals(user.getHandler())) {
					h.sendMessage(new DisconnectionMessage(user.getNickname() + " disconnected from the lobby: Game ended!", true));
				}
			}
			server.removeLobby(this);
		}).start();
		
	}
	
	public void endGame() {
		Lobby.LOGGER.info("Game has ended.");
		for (User user: clients) {
			user.getHandler().closeConnection();
		}
		server.removeLobby(this);
	}
	
	/**
	 * Inner class that describes a connected client with 3 parameters: nickname, client handler and its virtual view
	 */
	private record User(String nickname, ClientHandler handler, VirtualView view) {
		private String getNickname() {
			return nickname;
		}
		
		private ClientHandler getHandler() {
			return handler;
		}
		
		private VirtualView getView() {
			return view;
		}
		
	}
	
}
