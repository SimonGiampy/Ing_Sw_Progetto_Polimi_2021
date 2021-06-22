package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Marbles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketTest {
	
	Market market;
	
	@BeforeEach
	void setUp() {
		market = new Market();
	}

	@Test
	void shiftRow() {
		Marbles[][] before = new Marbles[3][4];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 4; j++){
				before[i][j] = market.getMarket()[i][j];
			}
		}
		Marbles extra = market.getExtraBall();
		market.shiftRow(2);
		Marbles[][] after = market.getMarket();
		assertEquals(extra, after[2][3]);
		assertEquals(before[2][3], after[2][2]);
		assertEquals(before[2][2], after[2][1]);
		assertEquals(before[2][1], after[2][0]);
		assertEquals(before[2][0], market.getExtraBall());

	}

	@Test
	void shiftCol() {
		Marbles[][] before = new Marbles[3][4];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 4; j++){
				before[i][j] = market.getMarket()[i][j];
			}
		}
		Marbles extra = market.getExtraBall();
		market.shiftCol(3);
		Marbles[][] after = market.getMarket();
		assertEquals(extra, after[2][3]);
		assertEquals(before[2][3], after[1][3]);
		assertEquals(before[1][3], after[0][3]);
		assertEquals(before[0][3], market.getExtraBall());

	}
}