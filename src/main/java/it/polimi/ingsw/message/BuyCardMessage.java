package it.polimi.ingsw.message;

import it.polimi.ingsw.util.Colors;

public class BuyCardMessage extends Message {
	private final int level;
	private final Colors color;

	public BuyCardMessage(String nickname, int level, Colors color){
		super(nickname,MessageType.BUY_CARD_MESSAGE);
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
