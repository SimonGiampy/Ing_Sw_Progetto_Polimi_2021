package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends match's info (players nicknames and current player)
 */
public class MatchInfoShow extends Message {

	private final ArrayList<String> players;
	private final String activePlayer;


	public MatchInfoShow(ArrayList<String> players, String activePlayer) {
		super("lobby", MessageType.MATCH_INFO_SHOW);
		this.players = players;
		this.activePlayer = activePlayer;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public String getActivePlayer() {
		return activePlayer;
	}
}