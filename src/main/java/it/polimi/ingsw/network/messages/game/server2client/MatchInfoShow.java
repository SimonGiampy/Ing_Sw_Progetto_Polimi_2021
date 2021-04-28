package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends match's info (players nicknames and current player)
 */
public class MatchInfoShow extends Message {

	private final ArrayList<String> players;


	public MatchInfoShow(ArrayList<String> players) {
		super("lobby", MessageType.MATCH_INFO_SHOW);
		this.players = players;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

}
