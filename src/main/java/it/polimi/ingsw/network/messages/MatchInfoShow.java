package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class MatchInfoShow extends Message{

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
