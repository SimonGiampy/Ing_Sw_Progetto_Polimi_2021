package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.ResourceDeck;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.model.util.Unicode;

import java.io.Serializable;

public class ReducedMarket implements Serializable {

	private final Marbles[][] market;
	private Marbles extraBall;

	public ReducedMarket(Market market){
		this.market=market.getMarket();
		extraBall=market.getExtraBall();
	}

	public Marbles[][] getMarket() {
		return market;
	}

	public Marbles getExtraBall() {
		return extraBall;
	}

}
