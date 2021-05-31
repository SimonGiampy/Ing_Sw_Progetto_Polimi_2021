package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Marbles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class MarketTest {
	
	Market market;
	
	@BeforeEach
	void setUp() {
		market = new Market();
	}

	@Test
	void shiftRow() {
		System.out.println("market before shifting 2 row = " + Arrays.toString(market.getMarket()));
		Marbles[] m = market.shiftRow(2);
		System.out.println("market after shifting 2 row = " + Arrays.toString(market.getMarket()));
		System.out.println("Result = " + Arrays.toString(m));
	}

	@Test
	void shiftCol() {
		System.out.println("market before shifting 3 col = " + Arrays.toString(market.getMarket()));
		Marbles[] m = market.shiftCol(3);
		System.out.println("market after shifting 3 col = " +Arrays.toString(market.getMarket()));
		System.out.println("Result = " + Arrays.toString(m));
	}
	
	@Test
	void getExtraBall() {
		System.out.println("extra ball = " + market.getExtraBall().toString());
	}
}