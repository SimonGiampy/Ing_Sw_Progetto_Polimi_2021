package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Marbles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketTest {
	
	Market market;
	
	@BeforeEach
	void setUp() {
		market = new Market();
	}

	@Test
	void shiftRow() {
		System.out.println("market before shifting 2 row = " + market.getMarket().toString());
		Marbles[] m = market.shiftRow(2);
		System.out.println("market after shifting 2 row = " + market.getMarket().toString());
		System.out.println("Result = " + m.toString());
	}

	@Test
	void shiftCol() {
		System.out.println("market before shifting 3 col = " + market.getMarket().toString());
		Marbles[] m = market.shiftCol(3);
		System.out.println("market after shifting 3 col = " + market.getMarket().toString());
		System.out.println("Result = " + m.toString());
	}
	
	@Test
	void getExtraBall() {
		System.out.println("extra ball = " + market.getExtraBall().toString());
	}
}