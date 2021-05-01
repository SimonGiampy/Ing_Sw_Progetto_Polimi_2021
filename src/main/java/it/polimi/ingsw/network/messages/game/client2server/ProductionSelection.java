package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the client sends selected production
 */
public class ProductionSelection extends Message {

	private final ArrayList<Integer> selectedProductions;


	public ProductionSelection(String nickname, ArrayList<Integer> selectedProductions) {
		super(nickname, MessageType.PRODUCTION_SELECTION);
		this.selectedProductions = selectedProductions;


	}

	public ArrayList<Integer> getSelectedProductions() {
		return selectedProductions;
	}
}
