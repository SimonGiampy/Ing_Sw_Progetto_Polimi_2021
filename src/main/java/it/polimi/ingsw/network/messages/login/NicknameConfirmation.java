package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server communicates if the nickname is valid or already chosen
 */
public class NicknameConfirmation  extends Message {
	
	private final boolean confirmed;
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public NicknameConfirmation(boolean confirmed) {
		super("Server", MessageType.NICKNAME_CONFIRMATION);
		this.confirmed = confirmed;
	}
}
