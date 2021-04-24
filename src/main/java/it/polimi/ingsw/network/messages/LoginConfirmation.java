package it.polimi.ingsw.network.messages;

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
