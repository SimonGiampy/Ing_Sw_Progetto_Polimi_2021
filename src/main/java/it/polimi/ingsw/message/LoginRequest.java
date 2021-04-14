package it.polimi.ingsw.message;

public class LoginRequest extends Message {

	public LoginRequest(String nickname){
		super(nickname,MessageType.LOGIN_REQUEST);
	}

	@Override
	public String toString(){
		return "LoginRequest{ "+
				"nickname: "+ getNickname()+
				" }";
	}
}
