package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class InputSelection extends Message {
	private final ArrayList<Integer> inputSelection;

	public InputSelection(String nickname, ArrayList<Integer> inputSelection){
		super(nickname, MessageType.INPUT_SELECTION);
		this.inputSelection=inputSelection;
	}

	public ArrayList<Integer> getInputSelection() {
		return inputSelection;
	}
}
