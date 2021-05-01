package it.polimi.ingsw.network.messages.generic;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class PingMessage extends Message {
	
	public PingMessage() {
		super("Client", MessageType.PING);
	}
}
