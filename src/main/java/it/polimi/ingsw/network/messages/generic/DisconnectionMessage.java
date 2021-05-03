package it.polimi.ingsw.network.messages.generic;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends info about a disconnection on the client side to the other players in the match
 */
public class DisconnectionMessage extends Message {

	private final String text;
	private final boolean termination; // signals that the entire lobby must be closed abruptly after the game started

	public DisconnectionMessage(String text, boolean termination) {
		super("Lobby", MessageType.DISCONNECTION_MESSAGE);
		this.text = text;
		this.termination = termination;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isTermination() {
		return termination;
	}
}
