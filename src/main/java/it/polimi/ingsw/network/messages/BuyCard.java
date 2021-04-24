package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.util.Colors;

/**
 *  the client sends chosen color and chosen level
 */
public class BuyCard extends Message {
	private final int level;
	private final Colors color;

	public BuyCard(String nickname, int level, Colors color){
		super(nickname,MessageType.BUY_CARD);
		this.level=level;
		this.color=color;
	}

	public int getLevel() {
		return level;
	}

	public Colors getColor() {
		return color;
	}
}
