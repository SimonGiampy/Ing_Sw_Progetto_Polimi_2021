package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 *  the server asks the player to choose the action in the turn
 */
public class ActionRequest extends Message {

	private final ArrayList<PlayerActions> availableAction;


	public ActionRequest(ArrayList<PlayerActions> availableAction) {
		super("lobby", MessageType.ACTION_REQUEST);
		this.availableAction = availableAction;
	}


	public ArrayList<PlayerActions> getAvailableAction() {
		return availableAction;
	}
}
