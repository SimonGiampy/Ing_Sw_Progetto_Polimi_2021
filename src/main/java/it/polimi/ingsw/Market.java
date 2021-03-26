package it.polimi.ingsw;

import java.util.Random;

public class Market {
	
	// representation as a Matrix
	private Marbles[][] market;
	private Marbles extraBall;
	
	public Market() {
		market = new Marbles[3][4];
		
		Random random = new Random();
		Marbles[] list = Marbles.values();
		
		for (int i = 0; i < 3; i++) { // rows
			for (int j = 0; j < 4; j++) { // columns
				market[i][j] = list[random.nextInt(6)];
			}
		}
		extraBall = list[random.nextInt(6)];
	}
	
	protected void showMarket() {
		System.out.println("\033[0m" + "extra ball = " + extraBall.colorCode + "o");
		for (int i = 0; i < 3; i++) { // rows
			for (int j = 0; j < 4; j++) { // columns
				System.out.print(market[i][j].colorCode + "o\t");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	protected void shiftRow(int row) {
		Marbles temp = extraBall;
		extraBall = market[row][0];
		market[row][0] = market[row][1];
		market[row][1] = market[row][2];
		market[row][2] = market[row][3];
		market[row][3] = temp;
	}
	
	protected void shiftCol(int col) {
		Marbles temp = extraBall;
		extraBall = market[0][col];
		market[0][col] = market[1][col];
		market[1][col] = market[2][col];
		market[2][col] = temp;
	}

}
