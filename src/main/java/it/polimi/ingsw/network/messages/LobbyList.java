package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class LobbyList extends Message {
	
	private ArrayList<String> lobbies;
	
	public LobbyList(ArrayList<String> lobbies) {
		super("Server", MessageType.LOBBY_LIST);
		this.lobbies = lobbies;
	}
	
	public String getLobby(int n) {
		return lobbies.get(n - 1);
	}
}
