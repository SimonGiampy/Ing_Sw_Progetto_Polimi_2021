package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.util.HashMap;
import java.util.logging.Logger;

public class Server {

	private final HashMap<String, SocketClientHandler> clientHandlerHashMap;
	
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	//TODO: add gamecontroller instance here
	
	public Server() {
		this.clientHandlerHashMap = new HashMap<>();
	}
	
	public void addClient(String nickname, SocketClientHandler clientHandler) {
		// check if game is not started
		// check if the chosen nickname is valid
		clientHandlerHashMap.put(nickname, clientHandler);
		// handle login in game controller and assign virtual view object
		//else show invalid nickname message and disconnects the client
	}
	
	public void removeClient(String nickname) {
		clientHandlerHashMap.remove(nickname);
		// remove virtual view association
		LOGGER.info(() -> "Removed " + nickname + " from the client list.");
	}
	
	/**
	 * Forwards a received message from the client to the GameController
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		//gameController.onMessageReceived(message);
	}
	
	public synchronized void onDisconnect(SocketClientHandler clientHandler) {
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
}
