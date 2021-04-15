package it.polimi.ingsw.message;

import java.util.ArrayList;

public class InputSelectionMessage extends Message {
	private final ArrayList<Integer> inputSelection;

	public InputSelectionMessage(String nickname, ArrayList<Integer> inputSelection){
		super(nickname, MessageType.INPUT_SELECTION_MESSAGE);
		this.inputSelection=inputSelection;
	}

	public ArrayList<Integer> getInputSelection() {
		return inputSelection;
	}
}
