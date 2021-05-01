package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Server implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private final List<Lobby> lobbies; // concurrent list for lobby accesses
	private int lobbyListVersion;
	
	private ServerSocket serverSocket;

	private Thread serverThread;
	
	/**
	 * Host a server on the local machine
	 * @param port the port that enables socket communication
	 */
	public Server(int port) {
		lobbies = Collections.synchronizedList(new ArrayList<>());
		lobbyListVersion = 0;
		
		try {
			serverSocket = new ServerSocket(port); // creates a local socket connection
			LOGGER.info("Socket lobby started on port " + port + ".");
		} catch (IOException e) {
			LOGGER.error("Lobby could not start!");
		}
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
				client.setSoTimeout(4000);
				ClientHandler clientHandler = new ClientHandler(this, client); // creation of the client handler
				new Thread(clientHandler).start();
				
			} catch (IOException e) {
				LOGGER.error("Connection dropped");
			}
		}
	}
	
	/**
	 * Generates a list of strings representing the list of lobbies present in the server.
	 * The description includes the lobby number, the number of present players, and the players expected for playing
	 * @return a list of descriptions of the lobbies.
	 */
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
	 * @throws IOException when there is an error in reading / writing messages in the socket
	 */
	public Lobby createLobby(ClientHandler host) throws IOException, ClassNotFoundException {
		Lobby lobby = new Lobby(this, host);
		lobby.setUpLobby();
		synchronized (lobbies) {
			lobbyListVersion++;
			lobbies.add(lobby);
		}
		return lobby;
	}
	
	/**
	 * Method called by client handler for joining the selected lobby
	 * @param number from 1 onwards, describing the lobby in the game to be accessed
	 * @param client that makes the request for accessing the lobby
	 * @return null if the client cannot access the selected lobby (because it is full)
	 *      otherwise returns the Lobby if the client entered it
	 */
	public Lobby joinLobby(int number, ClientHandler client) {
		synchronized (lobbies) {
			if (lobbies.size() < number) {
				return null;
			} else if (lobbies.get(number - 1).getConnectedClients() < lobbies.get(number - 1).getNumberOfPlayers()) {
				lobbies.get(number - 1).connectClient(client);
				return lobbies.get(number - 1);
			} else return null;
		}
	}
	
	/**
	 * method called by the last client handler that chose the nickname after entering the lobby
	 * @param lobby to be started
	 */
	public void startLobby(Lobby lobby) {
		lobby.setUpMatch();
	}
	
	/**
	 * removes a lobby from the list, making it invisible to the new clients. Method called when the lobby needs to be deleted
	 * @param lobby to be deleted
	 */
	public void removeLobby(Lobby lobby) {
		synchronized (lobbies) {
			lobbyListVersion++;
			lobbies.remove(lobby);
		}
	}

	public int getLobbyListVersion() {
		return lobbyListVersion;
	}
}
