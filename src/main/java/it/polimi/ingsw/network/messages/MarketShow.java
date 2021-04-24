package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedMarket;

public class MarketShow extends Message{

	 private final ReducedMarket market;
	 private final int action; // 0: show, 1: ask action

	public MarketShow(ReducedMarket market, int action){
		super("Lobby", MessageType.MARKET_SHOW);
		this.market=market;
		this.action = action;
	}

	public ReducedMarket getMarket() {
		return market;
	}

	public int getAction() {
		return action;
	}
}
