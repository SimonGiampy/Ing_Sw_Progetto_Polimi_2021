package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class ReducedCardProductionManagement implements Serializable {
	
	private final ArrayList<Stack<DevelopmentCard>> cards;
	//private final ProductionRules[] productions;

	ReducedCardProductionManagement(CardProductionsManagement productionsManagement){
		cards = productionsManagement.getCards();
		//productions = productionsManagement.getProductions();
	}

	public ArrayList<Stack<DevelopmentCard>> getCards() {
		return cards;
	}

	/*
	public ProductionRules[] getProductions() {
		return productions;
	}
	 */
}
