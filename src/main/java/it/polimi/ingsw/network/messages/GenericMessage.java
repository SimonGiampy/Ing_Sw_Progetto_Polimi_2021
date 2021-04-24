package it.polimi.ingsw.network.messages;

public class GenericMessage extends Message{

	private final String message;


	public GenericMessage(String message) {
		super("lobby", MessageType.GENERIC_MESSAGE);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
