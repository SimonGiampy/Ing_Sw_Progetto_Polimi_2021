package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the number of free choice resources
 */
public class ResourceChoice extends Message {

	private final int number;
	private final int action; // 0: initial choice, 1: input choice, 2: output choice
	public ResourceChoice(int number, int action) {
		super("Lobby", MessageType.RESOURCE_CHOICE);
		this.number = number;
		this.action = action;
	}

	public int getNumber() {
		return number;
	}

	public int getAction() {
		return action;
	}
}
