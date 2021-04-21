package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.Strongbox;
import it.polimi.ingsw.model.util.Resources;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedStrongbox implements Serializable {
	private ArrayList<Resources> content;

	public ReducedStrongbox(Strongbox strongbox){
		content= strongbox.getContent();
	}
}
