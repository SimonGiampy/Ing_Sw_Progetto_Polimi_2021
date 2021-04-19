package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.util.Colors;

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
