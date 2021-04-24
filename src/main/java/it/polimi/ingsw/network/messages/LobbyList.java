package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

/**
 * the server tells the client the available lobbies
 */
public class LobbyList extends Message {
	
	private final ArrayList<String> lobbies;
	
	public LobbyList(ArrayList<String> lobbies) {
		super("Server", MessageType.LOBBY_LIST);
		this.lobbies = lobbies;
	}
	
	public String getLobby(int n) {
		return lobbies.get(n - 1);
	}

	public ArrayList<String> getLobbies() {
		return lobbies;
	}
}
