package it.polimi.ingsw;

import java.util.ArrayList;

public class DevelopmentCardsDeck {
	private DevelopmentCard cardStackStructure[][];
	public DevelopmentCardsDeck( DevelopmentCard[][] initialDeck ){
		cardStackStructure= initialDeck;
	}
	public DevelopmentCard takeCard(int row, int column){
		return cardStackStructure[row][column];
	}
	public void addCard(DevelopmentCard newCard, int row, int column){
		cardStackStructure[row][column]=newCard;
	}
}
