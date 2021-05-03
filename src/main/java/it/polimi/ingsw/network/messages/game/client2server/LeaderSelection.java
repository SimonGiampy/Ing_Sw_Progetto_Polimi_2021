package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the client sends chosen leaders selection
 */
public class LeaderSelection extends Message {
	
	private final ArrayList<Integer> leaderSelection;
	
	public LeaderSelection(String nickname, ArrayList<Integer> leaderSelection) {
		super(nickname, MessageType.LEADER_SELECTION);
		this.leaderSelection = leaderSelection;
	}

	public ArrayList<Integer> getLeaderSelection() {
		return leaderSelection;
	}
	
}
