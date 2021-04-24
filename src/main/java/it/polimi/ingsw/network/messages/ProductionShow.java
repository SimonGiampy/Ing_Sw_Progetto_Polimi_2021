package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

/**
 * the server sends available production to the player
 */
public class ProductionShow extends Message {
	private final ArrayList<Integer> availableProduction;

	public ProductionShow(ArrayList<Integer> availableProduction){
		super("Lobby", MessageType.PRODUCTION_SHOW);
		this.availableProduction =availableProduction;
	}

	public ArrayList<Integer> getAvailableProduction() {
		return availableProduction;
	}
}
