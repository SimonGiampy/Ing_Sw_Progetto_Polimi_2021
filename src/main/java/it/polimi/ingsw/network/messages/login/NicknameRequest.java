package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server asks the client to choose a nickname
 */
public class NicknameRequest extends Message {
	
	public NicknameRequest(){
		super("server", MessageType.NICKNAME_REQUEST);
	}
	
	
}
