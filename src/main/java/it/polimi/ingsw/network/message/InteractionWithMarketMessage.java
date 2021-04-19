package it.polimi.ingsw.network.message;

public class InteractionWithMarketMessage extends Message {
	private final String which;
	private final int where;

	public InteractionWithMarketMessage(String nickname, String which, int where){
		super(nickname,MessageType.INTERACTION_WITH_MARKET_MESSAGE);
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
