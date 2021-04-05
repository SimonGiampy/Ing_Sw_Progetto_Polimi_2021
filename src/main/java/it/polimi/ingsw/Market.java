package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Market management
 */
public class Market {
	
	// representation as a Matrix
	private final Marbles[][] market;
	private Marbles extraBall;
	
	/**
	 * this constructor instantiates the market. The marbles positions are randomly generated from a constant set, since the number of marbles for
	 * each type is controlled and not random
	 */
	public Market() {
		market = new Marbles[3][4];
		
		ArrayList<Marbles> marblesList = new ArrayList<>(13);
		marblesList.add(Marbles.RED);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.BLUE);
		marblesList.add(Marbles.BLUE);
		marblesList.add(Marbles.GREY);
		marblesList.add(Marbles.GREY);
		marblesList.add(Marbles.YELLOW);
		marblesList.add(Marbles.YELLOW);
		marblesList.add(Marbles.PURPLE);
		marblesList.add(Marbles.PURPLE);
		
		Collections.shuffle(marblesList);
		extraBall = marblesList.get(0);
		int k = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				market[i][j] = marblesList.get(k);
				k++;
			}
		}
		
	}
	
	/**
	 * debugging function that shows the content of the market
	 */
	protected void showMarket() {
		System.out.println("\033[0m" + "extra ball = " + extraBall.colorCode + "o");
		for (int i = 0; i < 3; i++) { // rows
			for (int j = 0; j < 4; j++) { // columns
				System.out.print(market[i][j].colorCode + "o\t");
			}
			System.out.print("\n");
		}
		System.out.print("\033[0m\n");
	}
	
	/**
	 * getter for the extra ball that is not present in the matrix
	 * @return the other ball
	 */
	private Marbles getExtraBall() {
		return this.extraBall;
	}
	
	/**
	 * shift the selected row and updates the extra ball
	 * @param row the row where the player interacts
	 * @return the selected row, so that the equivalent resources are sent to the resource deck
	 */
	protected Marbles[] shiftRow(int row) {
		Marbles[] selected = market[row];

		Marbles temp = extraBall;
		extraBall = market[row][0];
		market[row][0] = market[row][1];
		market[row][1] = market[row][2];
		market[row][2] = market[row][3];
		market[row][3] = temp;

		return selected;
	}
	
	/**
	 * shift the selected col and updates the extra ball
	 * @param col the column where the player interacts
	 * @return the selected column, so that the equivalent resources are sent to the resource deck
	 */
	protected Marbles[] shiftCol(int col) {
		Marbles[] selected = new Marbles[3];
		selected[0] = market[0][col];
		selected[1] = market[1][col];
		selected[2] = market[2][col];

		Marbles temp = extraBall;
		extraBall = market[0][col];
		market[0][col] = market[1][col];
		market[1][col] = market[2][col];
		market[2][col] = temp;

		return selected;
	}


}
