package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends leader cards to the player, with a parameter to describe what to do with them
 */
public class LeaderInteractions extends Message {

	private final ArrayList<ReducedLeaderCard> leaderCards;
	private final int action; //0: choose 2 leaders, 1: just show, 2: ask play or discard

	public LeaderInteractions(String nickname, ArrayList<ReducedLeaderCard> leaderCards, int action){
		super(nickname, MessageType.LEADER_SHOW);
		this.leaderCards=leaderCards;
		this.action = action;
	}

	public ArrayList<ReducedLeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public int getAction() {
		return action;
	}
}
