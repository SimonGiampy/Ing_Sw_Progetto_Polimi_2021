package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the client sends chosen color and chosen level
 */
public class BuyCard extends Message {
	private final int level;
	private final Colors color;
	private final int slot;

	public BuyCard(String nickname, int level, Colors color, int slot){
		super(nickname, MessageType.BUY_CARD);
		this.level=level;
		this.color=color;
		this.slot=slot;
	}

	public int getLevel() {
		return level;
	}

	public Colors getColor() {
		return color;
	}

	public int getSlot() {
		return slot;
	}
}
