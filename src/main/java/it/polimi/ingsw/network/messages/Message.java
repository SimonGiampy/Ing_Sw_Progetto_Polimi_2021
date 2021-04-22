package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private final String nickname;
	private final MessageType messageType;

	Message(String nickname, MessageType messageType ){
		this.nickname=nickname;
		this.messageType=messageType;
	}


	public String getNickname() {
		return nickname;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	@Override
	public String toString(){
		return "MessageTest{ "+
				"nickname: "+ nickname+
				", messageType: "+ messageType+
				" }";
	}
}