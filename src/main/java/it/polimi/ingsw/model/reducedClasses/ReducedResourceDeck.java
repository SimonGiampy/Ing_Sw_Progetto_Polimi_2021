package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.ResourceDeck;
import it.polimi.ingsw.model.util.Resources;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedResourceDeck implements Serializable {
	private final ArrayList<Resources> resourceList;
	private final ArrayList<Resources> fromWhiteMarble1;
	private final ArrayList<Resources> fromWhiteMarble2;
	private final int whiteMarblesInput1;
	private final int whiteMarblesInput2;
	private final int faithPoint;

	ReducedResourceDeck(ResourceDeck resourceDeck){
		resourceList= resourceDeck.getResourceList();
		fromWhiteMarble1= resourceDeck.getFromWhiteMarble1();
		fromWhiteMarble2= resourceDeck.getFromWhiteMarble2();
		whiteMarblesInput1= resourceDeck.getWhiteMarblesInput1();
		whiteMarblesInput2= resourceDeck.getWhiteMarblesInput2();
		faithPoint= resourceDeck.getFaithPoint();
	}


	public ArrayList<Resources> getResourceList() {
		return resourceList;
	}

	public ArrayList<Resources> getFromWhiteMarble1() {
		return fromWhiteMarble1;
	}

	public ArrayList<Resources> getFromWhiteMarble2() {
		return fromWhiteMarble2;
	}

	public int getWhiteMarblesInput1() {
		return whiteMarblesInput1;
	}

	public int getWhiteMarblesInput2() {
		return whiteMarblesInput2;
	}

	public int getFaithPoint() {
		return faithPoint;
	}
}
