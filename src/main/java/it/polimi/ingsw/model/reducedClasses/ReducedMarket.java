package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.ResourceDeck;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.model.util.Unicode;

import java.io.Serializable;

public class ReducedMarket implements Serializable {

	private final Marbles[][] market;
	private Marbles extraBall;
	private boolean whiteMarble1;

	private boolean whiteMarble2;

	public ReducedMarket(Market market, ResourceDeck deck){
		this.market=market.getMarket();
		extraBall=market.getExtraBall();
		whiteMarble1=deck.isWhiteAbility1Activated();
		whiteMarble2=deck.isWhiteAbility2Activated();
	}

	public Marbles[][] getMarket() {
		return market;
	}

	public Marbles getExtraBall() {
		return extraBall;
	}

	public boolean isWhiteMarble1() {
		return whiteMarble1;
	}

	public boolean isWhiteMarble2() {
		return whiteMarble2;
	}
}
