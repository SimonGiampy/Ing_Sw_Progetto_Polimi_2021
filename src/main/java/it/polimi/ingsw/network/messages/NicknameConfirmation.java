package it.polimi.ingsw.network.messages;

public class NicknameConfirmation  extends Message {
	
	private boolean confirmed;
	
	public boolean isConfirmed() {
		return confirmed;
	}
	
	public NicknameConfirmation(boolean confirmed) {
		super("Server", MessageType.NICKNAME_CONFIRMATION);
		this.confirmed = confirmed;
	}
}
