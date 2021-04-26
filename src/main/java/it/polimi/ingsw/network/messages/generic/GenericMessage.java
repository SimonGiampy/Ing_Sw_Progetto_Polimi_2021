package it.polimi.ingsw.network.messages.generic;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends a generic message
 */
public class GenericMessage extends Message {

	private final String message;


	public GenericMessage(String message) {
		super("lobby", MessageType.GENERIC_MESSAGE);
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "GenericMessage: { " + message + "}";
	}
	
	public String getMessage() {
		return message;
	}
}
