package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends lobby's info (players nicknames)
 */
public class LobbyShow extends Message {

	private final ArrayList<String> players;

	public LobbyShow(ArrayList<String> players) {
		super("server", MessageType.LOBBY_SHOW);
		this.players = players;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}
	
}
