package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server implements Runnable {
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private final List<Lobby> lobbies; // concurrent list for lobby accesses
	private final ExecutorService lobbyStarter;
	
	private ServerSocket serverSocket;

	private Thread serverThread;
	
	/**
	 * Host a server on the local machine
	 * @param port the port that enables socket communication
	 */
	public Server(int port) {
		lobbies = Collections.synchronizedList(new ArrayList<>());
		
		try {
			serverSocket = new ServerSocket(port); // creates a local socket connection
			LOGGER.info("Socket lobby started on port " + port + ".");
		} catch (IOException e) {
			LOGGER.severe("Lobby could not start!");
		}
		lobbyStarter = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Runnable that accepts new clients connecting to the server
	 */
	@Override
	public void run() {
		serverThread = Thread.currentThread();
		
		while (!serverThread.isInterrupted()) {
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
			for (Lobby l : lobbies) {
				lobbyDes.add("Lobby " + i + ": " + l.getConnectedClients() + "/" + l.getNumberOfPlayers() + ";");
				i++;
			}
		}
		
		return lobbyDes;
	}
	
	/**
	 * Method called by the client handler for the creation of a new lobby
	 * @param host the client that becomes game host and decides the match parameters (automatically joins the lobby)
	 * @return the lobby just created
	 */
	public Lobby createLobby(ClientHandler host) {
		Lobby lobby = new Lobby(this, host);
		lobby.setUpLobby();
		synchronized (lobbies) {
			lobbies.add(lobby);
		}
		return lobby;
	}
	
	/**
	 * Method called by client handler for joining the selected lobby
	 * @param number from 1 onwards, describing the lobby in the game to be accessed
	 * @param client thta makes the request for accessing the lobby
	 * @return null if the client cannot access the selected lobby (because it is full)
	 *      otherwise returns the Lobby if the client entered it
	 */
	public Lobby joinLobby(int number, ClientHandler client) {
		synchronized (lobbies) {
			if (lobbies.get(number - 1).getConnectedClients() < lobbies.get(number - 1).getNumberOfPlayers()) {
				lobbies.get(number - 1).connectClient(client);
				return lobbies.get(number - 1);
			}
			return null;
		}
	}
	
	/**
	 * method called by the last client handler that chose the nickname after entering the lobby
	 * @param lobby
	 */
	public void startLobby(Lobby lobby) {
		lobbyStarter.execute(lobby);
	}
	
	public void removeLobby(Lobby lobby) {
		synchronized (lobbies) {
			lobbies.remove(lobby);
		}
	}
	
	/*
	//TODO: PROBABLY the lobby thread may be needed to be started from a server thread, and not by a client handler
	protected class LobbyStarter implements Runnable {
		
		private ReentrantLock lock;
		private Condition condition;
		
		private LobbyStarter() {
			this.lock = new ReentrantLock();
			this.condition = lock.newCondition();
		}
		
		@Override
		public void run() {
			while (serverThread.isAlive()) {
			
			}
		}
		protected Condition getLock() {
			return this.condition;
		}
		
	}
	
	 */
}
