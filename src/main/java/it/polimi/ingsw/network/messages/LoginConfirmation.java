package it.polimi.ingsw.network.messages;

/**
 * the server communicates if the lobby joining has been successful or not
 */
public class LoginConfirmation extends Message {
	
	private final boolean confirmed;
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public LoginConfirmation(boolean confirm){
		super("Server", MessageType.LOGIN_CONFIRMATION);
		this.confirmed = confirm;
	}
	
}
