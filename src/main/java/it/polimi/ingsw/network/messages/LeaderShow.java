package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;

import java.util.ArrayList;

public class LeaderShow extends Message{

	private final ArrayList<ReducedLeaderCard> leaderCards;
	private final boolean inGame; //false: choose 2 leaders, true: play or discard

	public LeaderShow(ArrayList<ReducedLeaderCard> leaderCards, boolean inGame){
		super("Server",MessageType.LEADER_SHOW);
		this.leaderCards=leaderCards;
		this.inGame = inGame;
	}

	public ArrayList<ReducedLeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public boolean isInGame() {
		return inGame;
	}
}
