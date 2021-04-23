package it.polimi.ingsw.network.messages;

public class LoginReply extends Message {
	
	private final boolean nicknameAccepted;

	public LoginReply(boolean nicknameAccepted) {
		super("Server", MessageType.LOGIN_REPLY);
		this.nicknameAccepted=nicknameAccepted;
	}
	
	public boolean isNicknameAccepted() {
		return nicknameAccepted;
	}

	
}
