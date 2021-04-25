package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the client sends market interaction info (which,where)
 */
public class InteractionWithMarket extends Message {
	private final String which;
	private final int where;

	public InteractionWithMarket(String nickname, String which, int where){
		super(nickname, MessageType.INTERACTION_WITH_MARKET);
		this.which=which;
		this.where=where;
	}

	public String getWhich() {
		return which;
	}

	public int getWhere() {
		return where;
	}
}
