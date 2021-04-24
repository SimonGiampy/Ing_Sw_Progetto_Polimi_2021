package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class ActionMessage extends Message{

	private final ArrayList<Integer> availableAction;


	public ActionMessage(ArrayList<Integer> availableAction) {
		super("lobby", MessageType.ACTION_MESSAGE);
		this.availableAction = availableAction;
	}


	public ArrayList<Integer> getAvailableAction() {
		return availableAction;
	}
}
