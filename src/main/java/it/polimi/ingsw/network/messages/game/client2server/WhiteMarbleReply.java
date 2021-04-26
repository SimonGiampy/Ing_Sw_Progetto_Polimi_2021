package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class WhiteMarbleReply extends Message {

	private final int leader1;
	private final int leader2;

	public WhiteMarbleReply(String nickname, int leader1, int leader2) {
		super(nickname, MessageType.WHITE_MARBLE_REPLY);
		this.leader1 = leader1;
		this.leader2 = leader2;
	}

	public int getLeader1() {
		return leader1;
	}

	public int getLeader2() {
		return leader2;
	}
}
