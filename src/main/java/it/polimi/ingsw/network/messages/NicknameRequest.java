package it.polimi.ingsw.network.messages;

public class NicknameRequest extends Message {
	
	public NicknameRequest(){
		super("server", MessageType.NICKNAME_REQUEST);
	}
	
	
}
