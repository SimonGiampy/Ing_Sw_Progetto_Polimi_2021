package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the market to the player
 */
public class MarketShow extends Message {

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
