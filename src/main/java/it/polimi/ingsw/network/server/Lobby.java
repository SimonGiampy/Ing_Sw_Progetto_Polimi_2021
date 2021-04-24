package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.GameConfigRequest;
import it.polimi.ingsw.network.messages.GenericMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Lobby implements Runnable {

	private ArrayList<User> clients;
	private List<String> nicknames;
	private int numberOfPlayers;
	
	private ClientHandler host;
	private ArrayList<ClientHandler> handlersList;
	private final GameController gameController;
	
	public static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
	
	public Lobby(ClientHandler host) {
		clients = new ArrayList<>();
		nicknames = Collections.synchronizedList(new ArrayList<>());
		handlersList = new ArrayList<>();
		this.host = host;
		
		gameController = new GameController(this);
		
	}
	
	/**
	 * Method called by the server for the initialization of the lobby (choose the number of players)
	 */
	public void setUpLobby() {
		numberOfPlayers = host.readNumberOfPlayers();
		connectClient(host);
	}
	
	/**
	 * The request of choosing which game config to be used must be asynchronous for this thread
	 * The game can't be started until the host doesn't choose with config file to use for the match
	 */
	public void setUpGameConfig() {
		host.sendMessage(new GameConfigRequest());
		String config = host.readGameConfig();
		gameController.setGameConfig(config);
	}

	public void connectClient(ClientHandler client) {
		handlersList.add(client);
	}

	public void join(ClientHandler handler) {
		VirtualView view = new VirtualView(handler);
		String nick;
		boolean valid;
		do {
			view.askNickname(); // asks for a nickname (message from the server to the client)
			nick = handler.readNickname();
			synchronized (nicknames) {
				valid = nicknames.contains(nick); //checks if the nickname isn't already chosen by another client
			}
			view.showNicknameConfirmation(valid); // sends the login result to the client, otherwise
		} while (!valid);
		
		synchronized (nicknames) {
			nicknames.add(nick); // memorizes the accepted nickname
		}
		
		handler.setLobby(this);
		clients.add(new User(nick, handler, view));
		
		// updates the other connected clients that a new user entered the game lobby
		broadcastMessage(new GenericMessage(nick + " joined the lobby!"));
		
	}
	
	public void removeClient(String nickname) {
		clients.removeIf(user -> user.getNickname().equals(nickname));
		//TODO: remove virtual view association
		LOGGER.info(() -> "Removed " + nickname + " from the client list.");
	}
	
	public void setNumberOfPlayers() {
		numberOfPlayers = clients.size();
	}
	
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
	}
	
	//TODO: add broadcast message function via the virtual views
	
	/**
	 * Forwards a received message from the client to the GameController
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		//gameController.onMessageReceived(message);
	}
	
	/*
	//TODO: handle client disconnection before the match starts and after the match is started

	public synchronized void onDisconnect(ClientHandler clientHandler) {
		String clientNickname = clientHandlerHashMap.entrySet()
				.stream()
				.filter(entry -> clientHandler.equals(entry.getValue()))
				.map(HashMap.Entry::getKey)
				.findFirst()
				.orElse(null);
		
		if (clientNickname != null) {
			//check if the game is started
			removeClient(clientNickname);
			
			//if the game has started
				//broadcast disconnection message via the game controller
				// end the game in GameController
				//clientHandlerHashMap.clear();
		}
	}

	 */
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	public int getConnectedClients() {
		return clients.size();
	}
	
	private class User {
		private String nickname;
		private ClientHandler handler;
		private VirtualView view;

		public String getNickname() {
			return nickname;
		}
		
		public ClientHandler getHandler() {
			return handler;
		}
		
		public VirtualView getView() {
			return view;
		}
		
		public User(String nickname, ClientHandler handler, VirtualView view) {
			this.nickname = nickname;
			this.handler = handler;
			this.view = view;
		}
	}
	
}
