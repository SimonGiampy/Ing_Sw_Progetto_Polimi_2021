package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class ActionRequest extends Message{

	private final ArrayList<Integer> availableAction;


	public ActionRequest(ArrayList<Integer> availableAction) {
		super("lobby", MessageType.ACTION_REQUEST);
		this.availableAction = availableAction;
	}


	public ArrayList<Integer> getAvailableAction() {
		return availableAction;
	}
}
