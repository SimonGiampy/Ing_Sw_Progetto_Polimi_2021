package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the market to the player
 */
public class MarketShow extends Message {

	 private final ReducedMarket market;
	 private final boolean askAction; // false: only shows the market, true: ask action

	public MarketShow(String nickname,ReducedMarket market, boolean action){
		super(nickname, MessageType.MARKET_SHOW);
		this.market = market;
		this.askAction = action;
	}

	public ReducedMarket getMarket() {
		return market;
	}

	public boolean isAskAction() {
		return askAction;
	}
}
