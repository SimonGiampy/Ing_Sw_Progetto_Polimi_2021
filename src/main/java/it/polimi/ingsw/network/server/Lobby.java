package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.generic.ErrorMessage;
import it.polimi.ingsw.network.messages.login.*;
import it.polimi.ingsw.network.messages.generic.GenericMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Lobby implements Runnable {

	private final ArrayList<User> clients;
	private final List<String> nicknames;
	private int numberOfPlayers;
	private boolean gameStarted;
	private boolean gameConfigSet;
	
	private ClientHandler host;
	private final Server server;
	private final List<ClientHandler> handlersList;
	private GameController gameController;
	
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
		gameConfigSet = false;
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
		Message message = (Message) host.getInputStream().readObject();
		if (message != null) {
			LOGGER.info("Received: " + message);
			if (message.getMessageType() == MessageType.PLAYER_NUMBER_REPLY) {
				PlayerNumberReply mes = (PlayerNumberReply) message;
				numberOfPlayers = mes.getPlayerNumber();
				gameController = new GameController(this, numberOfPlayers);
				connectClient(host);
			}
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
	 * The request of choosing which game config to be used is asynchronous, and directed to the game host
	 * The game can't be started until the host doesn't choose with config file to use for the match
	 */
	public void setUpGameConfig() throws IOException, ClassNotFoundException {
		host.sendMessage(new GameConfigRequest());
		Message message = (Message) host.getInputStream().readObject();
		String config;
		if (message != null) {
			LOGGER.info("Received: " + message);
			if (message.getMessageType() == MessageType.GAME_CONFIG_REPLY) {
				GameConfigReply mes = (GameConfigReply) message;
				config = mes.getGameConfiguration();
				gameController.setGameConfig(config);
				gameConfigSet = true;
				LOGGER.info("Game configuration has been read and applied to the lobby");
			}
		}
		
		if (clients.size() == numberOfPlayers && !gameStarted && gameConfigSet) {
			server.startLobby(this);
		}
	}
	
	/**
	 * Method called by the ClientHandler, that adds itself to the lobby. Then the procedure for choosing a valid nickname is started
	 * @param client that accesses this lobby after connecting
	 */
	public void join(ClientHandler client)  {
		VirtualView view = new VirtualView(client);
		String nick = null;
		boolean valid = false;
		do {
			view.askNickname(); // asks for a nickname (message from the server to the client)
			//nick = client.readNickname();
			Message message = null;
			try {
				message = (Message) client.getInputStream().readObject();
			}  catch (ClassCastException | ClassNotFoundException ex) {
				LOGGER.severe("Invalid class from stream from client");
			} catch (IOException e) {
				LOGGER.severe("Invalid IO from client caused by disconnection");
				client.disconnect();
				return;
			}
			if (message != null) {
				LOGGER.info("Received: " + message);
				if (message.getMessageType() == MessageType.NICKNAME_REPLY) {
					NicknameReply reply = (NicknameReply) message;
					nick = reply.getNicknameProposal();
					synchronized (nicknames) {
						valid = !nicknames.contains(nick); //checks if the nickname isn't already chosen by another client
					}
					view.showNicknameConfirmation(valid); // sends the login result to the client, otherwise
				}
			}
			
		} while (!valid);
		
		assert nick != null;
		synchronized (nicknames) {
			nicknames.add(nick); // memorizes the accepted nickname
		}
		
		client.setLobby(this);
		clients.add(new User(nick, client, view));
		
		if (clients.size() == numberOfPlayers && !gameStarted && gameConfigSet) {
			server.startLobby(this);
		}
		
		// updates the other connected clients that a new user entered the game lobby
		broadcastMessage(new GenericMessage(nick + " joined the lobby!"));
		
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
	 * Runnable starts when the match starts
	 */
	@Override
	public void run() {
		LOGGER.info("Match started");
		gameStarted = true;
		broadcastMessage(new GenericMessage("Match started!"));
		
		HashMap<String, VirtualView> virtualViewHashMap = new HashMap<>();
		for (User user: clients) {
			virtualViewHashMap.put(user.getNickname(), user.getView());
		}
		gameController.setVirtualViews(virtualViewHashMap);
		
		//TODO: initialize game via the game controller and keep this thread alive as long as the game is going on
		
	}
	
	/**
	 * Forwards a received message from the client to the GameController
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		gameController.onMessageReceived(message);
	}
	
	
	/**
	 * handle client disconnection before the match starts and after the match is started
	 * @param client that gets disconnected from the lobby
	 */
	public synchronized void onDisconnect(ClientHandler client) {
		synchronized (handlersList) {
			if (handlersList.contains(client)) { // client was connected
				
				if (client.equals(host)) { // the host disconnected from the lobby
					if (gameStarted) { // game was already started
						broadcastMessage(new ErrorMessage("A player disconnected: Game ended!"));
						handlersList.clear();
						gameController.haltGame();
						server.removeLobby(this);
					} else {
						if (handlersList.size() == 1) { // the host was the only one connected
							handlersList.clear();
							server.removeLobby(this);
						} else { // there were guests connected
							//broadcastMessage(new GenericMessage("The host left the lobby: new host is ?"));
							//TODO: server in crashing loop when the host quits the lobby and there is one player in the lobby
							User user = null;
							for (User u: clients) {
								if (u.getHandler().equals(host)) {
									user = u;
								}
							}
							handlersList.remove(0); //removes host
							if (user != null) {
								nicknames.remove(user.getNickname());
								clients.remove(user);
							}
							host = handlersList.get(0); //new host is the second client that entered the lobby
						}
					}
				} else { // a guest disconnected from the lobby
					User user = null;
					for (User u: clients) {
						if (u.getHandler().equals(client)) {
							user = u;
						}
					}
					if (gameStarted) { // if the game already started, halts it abruptly
						broadcastMessage(new ErrorMessage("A player disconnected: Game ended!"));
						handlersList.clear();
						gameController.haltGame();
						server.removeLobby(this);
					} else if (user != null) { // the client had already chosen a nickname but the game didn't start
						LOGGER.info("Removed " + user.getNickname() + " from the connected players list");
						nicknames.remove(user.getNickname());
						broadcastMessage(new ErrorMessage(user.getNickname() + " disconnected from the lobby"));
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
	
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	public int getConnectedClients() {
		int num;
		synchronized (handlersList) {
			num = handlersList.size();
		}
		return num;
	}
	
	/**
	 * Inner class that describes a connected client with 3 parameters: nickname, client handler and its virtual view
	 */
	private static class User {
		private final String nickname;
		private final ClientHandler handler;
		private final VirtualView view;

		private String getNickname() {
			return nickname;
		}
		
		private ClientHandler getHandler() {
			return handler;
		}
		
		private VirtualView getView() {
			return view;
		}
		
		private User(String nickname, ClientHandler handler, VirtualView view) {
			this.nickname = nickname;
			this.handler = handler;
			this.view = view;
		}
	}
	
}
