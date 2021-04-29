package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server tells the client the available lobbies
 */
public class LobbyList extends Message {
	
	private final ArrayList<String> lobbies;
	private final int idVersion;
	
	public LobbyList(ArrayList<String> lobbies, int idVersion) {
		super("Server", MessageType.LOBBY_LIST);
		this.lobbies = lobbies;
		this.idVersion = idVersion;
	}
	
	public String getLobby(int n) {
		return lobbies.get(n - 1);
	}

	public ArrayList<String> getLobbies() {
		return lobbies;
	}

	public int getIdVersion() {
		return idVersion;
	}
}
