package it.polimi.ingsw.network.messages;

public class DepotInteractionMessage extends Message{
	private final String where;
	private final int from;
	private final int destination;

	public DepotInteractionMessage(String nickname, String where, int from, int destination) {
		super(nickname, MessageType.DEPOT_INTERACTION_MESSAGE);
		this.where = where;
		this.from = from;
		this.destination = destination;
	}
}
