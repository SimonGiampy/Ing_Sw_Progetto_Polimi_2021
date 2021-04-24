package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class Server implements Runnable {
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private final List<Lobby> lobbies; // new approach for concurrent lobby accesses
	
	private final int port;
	private ServerSocket serverSocket;
	private int numberOfPlayers;
	
	public Server(int port) {
		this.port = port;
		numberOfPlayers = 5; // must never reach this number
		lobbies = Collections.synchronizedList(new ArrayList<>());
		
		try {
			serverSocket = new ServerSocket(port); // creates a local socket connection
			LOGGER.info("Socket lobby started on port " + port + ".");
		} catch (IOException e) {
			LOGGER.severe("Lobby could not start!");
		}
	}
	
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// accepts new client connections
				Socket client = serverSocket.accept();
				
				ClientHandler clientHandler = new ClientHandler(this, client); // creation of the client handler
				new Thread(clientHandler).start();
				
			} catch (IOException e) {
				LOGGER.severe("Connection dropped");
			}
		}
	}

	
	public ArrayList<String> getLobbiesDescription() {
		ArrayList<String> lobbyDes = new ArrayList<>();
		int i = 1;
		synchronized (lobbies) {
			for (Lobby l: lobbies) {
				lobbyDes.add("Lobby" + i + ": " + l.getConnectedClients() + "/" + l.getNumberOfPlayers() + ";");
				i++;
			}
		}

		return lobbyDes;
	}
	
	public Lobby createLobby(ClientHandler host) {
		Lobby lobby = new Lobby(host);
		lobby.setUpLobby();
		synchronized (lobbies) {
			lobbies.add(lobby);
		}
		return lobby;
	}

	public Lobby joinLobby(int number, ClientHandler client){
		synchronized (lobbies) {
			if (lobbies.get(number - 1).getConnectedClients() < lobbies.get(number - 1).getNumberOfPlayers()) {
				lobbies.get(number - 1).connectClient(client);
				return lobbies.get(number-1);
			}
			return null;
		}
	}
	
	/*
				ClientHandler clientHandler = new ClientHandler(client); // creation of the client handler
				VirtualView view = new VirtualView(clientHandler); // associates the virtual view to the client handler
				
				String nick;
				boolean valid;
				do {
					view.askNickname(); // asks for a nickname (message from the server to the client)
					nick = clientHandler.readNickname();
					valid = nicknames.contains(nick); //checks if the nickname isn't already chosen by another client
					view.showLoginResult(valid); // sends the login result to the client, otherwise
				} while (!valid);
				
				nicknames.add(nick); // memorizes the accepted nickname
				lobbyList.getLast().addClient(nick, clientHandler, view); // adds the client to the lobby
				
				// if the waiting room is empty, the host client decides the number of players
				if (nicknames.size() == 1) {
					clientHandler.sendMessage(new PlayerNumberRequest()); // asks for the number of players to be assigned in the lobby
					numberOfPlayers = clientHandler.readNumberOfPlayers(); // gets the number of players
				}
				
				// if the waiting room is full, creates a Lobby, assigns the clients and proceed to reset the waiting room
				if(nicknames.size() == numberOfPlayers) {
					// creates a lobby and passes the list of clients and their relative hashmap
					lobbyList.getLast().setNumberOfPlayers();
					new Thread(lobbyList.getLast()).start();
					
					lobbyList.add(new Lobby()); // creates a new empty lobby for the next players
					nicknames.clear(); // clears the list of nicknames
				}

	 */
	
	
/*
	/**
	 * Forwards a received message from the client to the Lobby
	 * @param message the message to be forwarded.
	 
	public void onMessageReceived(int lobbyIndex, Message message) {
		lobbyList.get(lobbyIndex).onMessageReceived(message);
	}
	
	
	/**
	 * Handles a client disconnection.
	 * @param clientHandler the ClientHandler of the disconnecting client.
	 
	public void onDisconnect(int lobbyIndex, ClientHandler clientHandler) {
		lobbyList.get(lobbyIndex).onDisconnect(clientHandler);
	}
	*/
}
