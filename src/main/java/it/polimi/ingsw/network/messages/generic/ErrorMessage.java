package it.polimi.ingsw.network.messages.generic;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends an error message
 */
public class ErrorMessage extends Message {

	private final String errorMessage;
	public ErrorMessage( String errorMessage) {
		super("Server", MessageType.ERROR_MESSAGE);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
