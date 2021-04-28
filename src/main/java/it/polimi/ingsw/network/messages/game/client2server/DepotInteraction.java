package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the client sends depot interaction info and a flag indicating the confirmation of the positioning
 */
public class DepotInteraction extends Message {
	
	private final String fromWhere;
	private final String toWhere;
	private final int origin;
	private final int destination;
	private final boolean confirmation;

	public DepotInteraction(String nickname, String fromWhere, String toWhere, int origin, int destination, boolean confirmation) {
		super(nickname, MessageType.DEPOT_INTERACTION);
		this.fromWhere = fromWhere;
		this.toWhere = toWhere;
		this.origin = origin;
		this.destination = destination;
		this.confirmation = confirmation;
	}

	public String getToWhere() {
		return toWhere;
	}
	
	public String getFromWhere() {
		return fromWhere;
	}
	
	public int getOrigin() {
		return origin;
	}

	public int getDestination() {
		return destination;
	}
	
	public boolean isConfirmed() {
		return confirmation;
	}
}
