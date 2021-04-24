package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

/**
 * the client sends selected production
 */
public class ProductionSelection extends Message{

	private final ArrayList<Integer> selectedProductions;

	public ProductionSelection(String nickname, ArrayList<Integer> selectedProductions) {
		super(nickname, MessageType.PRODUCTION_SELECTION);
		this.selectedProductions = selectedProductions;
	}

	public ArrayList<Integer> getSelectedProductions() {
		return selectedProductions;
	}
}
