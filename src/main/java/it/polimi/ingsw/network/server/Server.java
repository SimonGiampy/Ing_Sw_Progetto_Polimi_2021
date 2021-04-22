package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.PlayerNumberRequest;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Server implements Runnable {
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private HashMap<String, ClientHandler> waitingRoom;
	
	private final ArrayList<Lobby> lobbyList;
	private final int port;
	ServerSocket serverSocket;
	private int numberOfPlayers;
	
	public Server(int port) {
		this.port = port;
		lobbyList = new ArrayList<>();
		numberOfPlayers = 5; // must never reach this number
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			LOGGER.info(() -> "Socket lobby started on port " + port + ".");
		} catch (IOException e) {
			LOGGER.severe("Lobby could not start!");
			return;
		}
		
		String hostname = "";
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// accepts new client connections
				Socket client = serverSocket.accept();
				
				ClientHandler clientHandler = new ClientHandler(client); // creation of the client handler
				VirtualView view = new VirtualView(clientHandler); // associates the virtual view to the client handler
				view.askNickname(); // asks for a nickname (message from the server to the client)
				// gets the nickname the client chose, and memorizes it in the relative hashmap
				String nick = clientHandler.readNickname();
				waitingRoom.put(nick, clientHandler);
				
				// if the waiting room is empty, the host client decides the number of players
				if (waitingRoom.size() == 0) {
					hostname = nick;
					clientHandler.sendMessage(new PlayerNumberRequest()); // asks for the number of players to be assigned in the lobby
					numberOfPlayers = clientHandler.readNumberOfPlayers(); // gets the number of players
				}
				
				//waitingRoom.add(clientHandler); // adds the client to the waiting room list
				// if the waiting room is full, creates a Lobby, assigns the clients and proceed to reset the waiting room
				if(waitingRoom.size() == numberOfPlayers) {
					// creates a lobby and passes the list of clients and their relative hashmap
					
					//TODO:
					
					Lobby lobby = new Lobby(new HashMap<>(waitingRoom), hostname);
					lobbyList.add(lobby);
					new Thread(lobby).start();
					
					waitingRoom.clear();
				}


			} catch (IOException e) {
				LOGGER.severe("Connection dropped");
			}
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
