package it.polimi.ingsw.network.messages;

public class DepotInteraction extends Message{
	private final String where;
	private final int from;
	private final int destination;

	public DepotInteraction(String nickname, String where, int from, int destination) {
		super(nickname, MessageType.DEPOT_INTERACTION);
		this.where = where;
		this.from = from;
		this.destination = destination;
	}
}
