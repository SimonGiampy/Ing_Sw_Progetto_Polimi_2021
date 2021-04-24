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
	
	private final List<Lobby> lobbies; // new approach for concurrent lobby accesses
	private final ExecutorService lobbyStarter;
	
	private ServerSocket serverSocket;

	private Thread serverThread;
	
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
	
	@Override
	public void run() {
		serverThread = Thread.currentThread();
		
		// checks asynchronously if the match can be started
		//LobbyStarter lobbyStarter = new LobbyStarter();
		//new Thread(lobbyStarter).start();
		
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
				lobbyDes.add("Lobby" + i + ": " + l.getConnectedClients() + "/" + l.getNumberOfPlayers() + ";");
				i++;
			}
		}
		
		return lobbyDes;
	}
	
	public Lobby createLobby(ClientHandler host) {
		Lobby lobby = new Lobby(this, host);
		lobby.setUpLobby();
		synchronized (lobbies) {
			lobbies.add(lobby);
		}
		return lobby;
	}
	
	public Lobby joinLobby(int number, ClientHandler client) {
		synchronized (lobbies) {
			if (lobbies.get(number - 1).getConnectedClients() < lobbies.get(number - 1).getNumberOfPlayers()) {
				lobbies.get(number - 1).connectClient(client);
				return lobbies.get(number - 1);
			}
			return null;
		}
	}
	
	public void startLobby(Lobby lobby) {
		lobbyStarter.execute(lobby);
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
