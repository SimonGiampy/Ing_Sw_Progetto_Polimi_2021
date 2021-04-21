package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class ReducedCardProductionManagement implements Serializable {
	private final ArrayList<Stack<DevelopmentCard>> cards;
	private final boolean[] numberOfProduction;
	private final ProductionRules[] productions;

	ReducedCardProductionManagement(CardProductionsManagement productionsManagement){
		cards=productionsManagement.getCards();
		numberOfProduction= productionsManagement.getNumberOfProduction();
		productions=productionsManagement.getProductions();
	}

	public ArrayList<Stack<DevelopmentCard>> getCards() {
		return cards;
	}

	public boolean[] getNumberOfProduction() {
		return numberOfProduction;
	}

	public ProductionRules[] getProductions() {
		return productions;
	}
}
