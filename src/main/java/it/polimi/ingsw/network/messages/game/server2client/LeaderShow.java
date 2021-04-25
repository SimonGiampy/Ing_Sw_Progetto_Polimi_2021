package it.polimi.ingsw.network.messages.game.server2client;
/**
 * the server sends leader cards to the player
 */

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class LeaderShow extends Message {

	private final ArrayList<ReducedLeaderCard> leaderCards;
	private final int action; //0: choose 2 leaders, 1: just show, 2: ask play or discard

	public LeaderShow(ArrayList<ReducedLeaderCard> leaderCards, int action){
		super("Server", MessageType.LEADER_SHOW);
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
