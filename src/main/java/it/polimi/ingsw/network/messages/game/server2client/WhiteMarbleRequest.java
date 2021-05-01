package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class WhiteMarbleRequest extends Message {

	private final ArrayList<Resources> fromWhiteMarble1;
	private final ArrayList<Resources> fromWhiteMarble2;
	private final int whiteMarblesInput1;
	private final int whiteMarblesInput2;
	private final int howMany;

	public WhiteMarbleRequest(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2,
							  int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
		super("Server", MessageType.WHITE_MARBLE_REQUEST);
		this.fromWhiteMarble1 = fromWhiteMarble1;
		this.fromWhiteMarble2 = fromWhiteMarble2;
		this.whiteMarblesInput1 = whiteMarblesInput1;
		this.whiteMarblesInput2 = whiteMarblesInput2;
		this.howMany = howMany;
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

	public int getHowMany() {
		return howMany;
	}
}
