package it.polimi.ingsw.network.messages;

public class LoginReply extends Message {
	private final boolean nicknameAccepted;
	private final boolean connectionSuccessful;

	public LoginReply(boolean nicknameAccepted, boolean connectionSuccessful){
		super("server",MessageType.LOGIN_REPLY);
		this.nicknameAccepted=nicknameAccepted;
		this.connectionSuccessful=connectionSuccessful;
	}


	public boolean isNicknameAccepted() {
		return nicknameAccepted;
	}

	public boolean isConnectionSuccessful() {
		return connectionSuccessful;
	}

	@Override
	public String toString(){
		return "LoginReply{ " +
				"nickname: " +getNickname()+
				", nicknameAccepted: " + nicknameAccepted+
				", connectionSuccessful: " + connectionSuccessful+
				" }";
	}
}