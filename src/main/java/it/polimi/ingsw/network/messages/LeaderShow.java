package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;

import java.util.ArrayList;

public class LeaderShow extends Message{

	private final ArrayList<ReducedLeaderCard> leaderCards;

	public LeaderShow(ArrayList<ReducedLeaderCard> leaderCards){
		super("server",MessageType.LEADER_SHOW);
		this.leaderCards=leaderCards;
	}

	public ArrayList<ReducedLeaderCard> getLeaderCards() {
		return leaderCards;
	}
}
