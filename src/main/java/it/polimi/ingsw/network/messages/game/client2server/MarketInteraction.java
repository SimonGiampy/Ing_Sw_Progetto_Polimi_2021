package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the client sends market interaction info (which,where)
 */
public class MarketInteraction extends Message {
	private final String which;
	private final int where;
	private final int quantity1;
	private final int quantity2;

	public MarketInteraction(String nickname, String which, int where, int quantity1, int quantity2){
		super(nickname, MessageType.INTERACTION_WITH_MARKET);
		this.which=which;
		this.where=where;
		this.quantity1=quantity1;
		this.quantity2=quantity2;

	}

	public String getWhich() {
		return which;
	}

	public int getWhere() {
		return where;
	}

	public int getQuantity1() {
		return quantity1;
	}

	public int getQuantity2() {
		return quantity2;
	}
}
