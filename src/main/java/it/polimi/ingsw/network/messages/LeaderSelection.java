package it.polimi.ingsw.network.messages;
/**
 * the client sends chosen leaders
 */

import java.util.ArrayList;

public class LeaderSelection extends Message{
	private final ArrayList<Integer> leaderSelection;


	public LeaderSelection(String nickname, ArrayList<Integer> leaderSelection) {
		super(nickname, MessageType.LEADER_SELECTION);
		this.leaderSelection = leaderSelection;
	}

	public ArrayList<Integer> getLeaderSelection() {
		return leaderSelection;
	}
}
