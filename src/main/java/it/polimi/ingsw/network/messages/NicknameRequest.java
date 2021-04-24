package it.polimi.ingsw.network.messages;

/**
 * the server asks the client to choose a nickname
 */
public class NicknameRequest extends Message {
	
	public NicknameRequest(){
		super("server", MessageType.NICKNAME_REQUEST);
	}
	
	
}
