package it.polimi.ingsw.network.messages;

/**
 * the server sends info about a disconnection
 */
public class DisconnectionMessage extends Message{

	private final String nicknameDisconnected;
	private final String message;

	public DisconnectionMessage(String nicknameDisconnected, String message) {
		super("lobby", MessageType.DISCONNECTION_MESSAGE);
		this.nicknameDisconnected = nicknameDisconnected;
		this.message = message;
	}

	public String getNicknameDisconnected() {
		return nicknameDisconnected;
	}

	public String getMessage() {
		return message;
	}
}
