package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.Market;
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

	/**
	 * debugging function that shows the content of the market
	 */
	public void showMarket() {
		System.out.println("\033[0m" + "extra ball = " + extraBall.colorCode + "\uD83D\uDFE3");
		for (int i = 0; i < 3; i++) { // rows
			for (int j = 0; j < 4; j++) { // columns
				System.out.print(market[i][j].colorCode + "\uD83D\uDFE3\t");
			}
			System.out.println(Unicode.RESET+"←");
		}
		for (int j = 0; j < 4; j++) {
			System.out.print(" ↑\t");
		}
		System.out.print("\n");
	}
}
