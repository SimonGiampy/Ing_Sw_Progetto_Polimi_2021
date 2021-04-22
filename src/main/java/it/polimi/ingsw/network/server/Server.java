package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PlayerNumberRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Server implements Runnable {
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	private ArrayList<ClientHandler> waitingRoom;
	private final ArrayList<Lobby> lobbies;
	private final int port;
	ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
		lobbies = new ArrayList<>();
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
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// accepts new client connections
				Socket client = serverSocket.accept();
				
				ClientHandler clientHandler = new ClientHandler(this, client);
				if (waitingRoom.size() == 0) {
					clientHandler.sendMessage(new PlayerNumberRequest());
				}
				waitingRoom.add(clientHandler);

				Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
				thread.start();
			} catch (IOException e) {
				LOGGER.severe("Connection dropped");
			}
		}
	}
	
	
	/**
	 * Handles the addition of a new client to the game
	 * @param nickname      the nickname of the new client.
	 * @param clientHandler the ClientHandler of the new client.
	 */
	public void addClient(int lobbyIndex, String nickname, ClientHandler clientHandler) {
		lobbies.get(lobbyIndex).addClient(nickname, clientHandler);
	}
	
	/**
	 * Forwards a received message from the client to the Lobby
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(int lobbyIndex, Message message) {
		lobbies.get(lobbyIndex).onMessageReceived(message);
	}
	
	/**
	 * Handles a client disconnection.
	 * @param clientHandler the ClientHandler of the disconnecting client.
	 */
	public void onDisconnect(int lobbyIndex, ClientHandler clientHandler) {
		lobbies.get(lobbyIndex).onDisconnect(clientHandler);
	}
	
}
