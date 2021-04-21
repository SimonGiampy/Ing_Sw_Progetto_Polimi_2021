package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.DevelopmentCardsDeck;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedDevelopmentCardsDeck implements Serializable {
	private final ArrayList<DevelopmentCard>[][] cardStackStructure;

	public ReducedDevelopmentCardsDeck(DevelopmentCardsDeck deck){
		cardStackStructure=deck.getCardStackStructure();
	}

	public ArrayList<DevelopmentCard>[][] getCardStackStructure() {
		return cardStackStructure;
	}
}
