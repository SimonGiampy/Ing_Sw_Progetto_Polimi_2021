package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * General message class. Can't be instantiated because it represents every message exchanged using
 * the java serialization via socket connection.
 */
public abstract class Message implements Serializable {
	
	private final String nickname;
	private final MessageType messageType;
	
	/**
	 * constructor for every message in the package
	 * @param nickname who sends the message
	 * @param messageType describes the type of message (representing its subclass)
	 */
	public Message(String nickname, MessageType messageType) {
		this.nickname=nickname;
		this.messageType=messageType;
	}
	
	public String getNickname() {
		return nickname;
	}

	public MessageType getMessageType() {
		return messageType;
	}

}
