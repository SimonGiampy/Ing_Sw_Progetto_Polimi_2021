package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
	
	private ArrayList<ClientHandler> waitingRoom;
	private final ArrayList<Lobby> lobbies;
	private final int port;
	ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
		lobbies= new ArrayList<>();
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			Lobby.LOGGER.info(() -> "Socket lobby started on port " + port + ".");
		} catch (IOException e) {
			Lobby.LOGGER.severe("Lobby could not start!");
			return;
		}
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// accepts new client connections
				Socket client = serverSocket.accept();
				
				ClientHandler clientHandler = new ClientHandler(this, client);
				if (waitingRoom.size()==0){

				}
				waitingRoom.add(clientHandler);

				Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
				thread.start();
			} catch (IOException e) {
				Lobby.LOGGER.severe("Connection dropped");
			}
		}
	}
	
	
	/**
	 * Handles the addition of a new client to the game
	 * @param nickname      the nickname of the new client.
	 * @param clientHandler the ClientHandler of the new client.
	 */
	public void addClient(String nickname, ClientHandler clientHandler) {
		lobby.addClient(nickname, clientHandler);
	}
	
	/**
	 * Forwards a received message from the client to the Lobby
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		lobby.onMessageReceived(message);
	}
	
	/**
	 * Handles a client disconnection.
	 * @param clientHandler the ClientHandler of the disconnecting client.
	 */
	public void onDisconnect(ClientHandler clientHandler) {
		lobby.onDisconnect(clientHandler);
	}
	
}
