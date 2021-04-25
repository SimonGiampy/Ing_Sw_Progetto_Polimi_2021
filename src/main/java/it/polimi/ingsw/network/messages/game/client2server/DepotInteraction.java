package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the client sends depot interaction info ( where, from, destination)
 */
public class DepotInteraction extends Message {
	private final String where;
	private final int from;
	private final int destination;

	public DepotInteraction(String nickname, String where, int from, int destination) {
		super(nickname, MessageType.DEPOT_INTERACTION);
		this.where = where;
		this.from = from;
		this.destination = destination;
	}

	public String getWhere() {
		return where;
	}

	public int getFrom() {
		return from;
	}

	public int getDestination() {
		return destination;
	}
}
