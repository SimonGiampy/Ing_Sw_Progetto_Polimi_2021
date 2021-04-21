package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;

public class MarketShow extends Message{

	 private ReducedMarket market;

	public MarketShow(ReducedMarket market){
		super("server", MessageType.MARKET_SHOW);
		this.market=market;
	}

	public ReducedMarket getMarket() {
		return market;
	}
}
