package it.polimi.ingsw.network.messages;

public class ErrorMessage extends Message{

	private final String errorMessage;
	ErrorMessage(String nickname, String errorMessage) {
		super(nickname, MessageType.ERROR_MESSAGE);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
