package it.polimi.ingsw.network.messages;

public class GenericMessage extends Message{

	private final String message;


	public GenericMessage(String nickname, String message) {
		super(nickname, MessageType.GENERIC_MESSAGE);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
