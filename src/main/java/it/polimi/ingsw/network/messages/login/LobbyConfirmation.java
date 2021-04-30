package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server communicates if the lobby joining has been successful or not
 */
public class LobbyConfirmation extends Message {
	
	private final boolean confirmed;
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public LobbyConfirmation(boolean confirm){
		super("Server", MessageType.LOGIN_CONFIRMATION);
		this.confirmed = confirm;
	}
	
}
