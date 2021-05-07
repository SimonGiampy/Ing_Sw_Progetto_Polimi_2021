package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class ReducedCardProductionManagement implements Serializable {
	
	private final ArrayList<Stack<DevelopmentCard>> cards;

	public ReducedCardProductionManagement(CardProductionsManagement productionsManagement){
		cards = productionsManagement.getCards();
	}

	public ArrayList<Stack<DevelopmentCard>> getCards() {
		return cards;
	}
	
}
