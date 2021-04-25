package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends lobby's info (players nicknames and number of players)
 */
public class LobbyShow extends Message {

	private final ArrayList<String> players;
	private final int numberOfPlayers;


	public LobbyShow(ArrayList<String> players, int numberOfPlayers) {
		super("server", MessageType.LOBBY_SHOW);
		this.players = players;
		this.numberOfPlayers = numberOfPlayers;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
}
