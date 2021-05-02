package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the client sends selected production
 */
public class ProductionSelection extends Message {

	private final ArrayList<Productions> selectedProductions;

	public ProductionSelection(String nickname, ArrayList<Productions> selectedProductions) {
		super(nickname, MessageType.PRODUCTION_SELECTION);
		this.selectedProductions = selectedProductions;
	}

	public ArrayList<Productions> getSelectedProductions() {
		return selectedProductions;
	}
}
