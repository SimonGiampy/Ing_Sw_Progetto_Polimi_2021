package it.polimi.ingsw.network.messages;

public class ErrorMessage extends Message{

	private final String errorMessage;
	public ErrorMessage( String errorMessage) {
		super("Server", MessageType.ERROR_MESSAGE);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
