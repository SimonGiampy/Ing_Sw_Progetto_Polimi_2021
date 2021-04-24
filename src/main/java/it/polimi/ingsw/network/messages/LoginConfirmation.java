package it.polimi.ingsw.network.messages;

public class LoginConfirmation extends Message {
	
	private boolean confirmed;
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public LoginConfirmation(boolean confirm){
		super("undefined", MessageType.LOGIN_CONFIRMATION);
		this.confirmed = confirm;
	}
	
}
