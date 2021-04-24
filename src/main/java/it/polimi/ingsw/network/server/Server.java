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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Server implements Runnable {
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private final List<Lobby> lobbies; // new approach for concurrent lobby accesses
	private final ExecutorService lobbyStarter;
	
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
		lobbyStarter = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public void run() {
		// checks asynchronously if the match can be started
		LobbyStarter lobbyStarter = new LobbyStarter();
		new Thread(lobbyStarter).start();
		
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
	

	 protected class LobbyStarter implements Runnable {
		
		private ReentrantLock lock;
		private Condition condition;
		
		private LobbyStarter() {
			this.lock = new ReentrantLock();
			this.condition = lock.newCondition();
		}
		
		 @Override
		 public void run() {
			
		 }
		 
		 protected Condition getLock() {
			return this.condition;
		 }
		 
	 }
	
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
