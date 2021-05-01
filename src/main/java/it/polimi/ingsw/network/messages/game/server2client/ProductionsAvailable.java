package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends available production to the player
 */
public class ProductionsAvailable extends Message {
	private final ArrayList<Integer> availableProduction;

	public ProductionsAvailable(ArrayList<Integer> availableProduction){
		super("Lobby", MessageType.PRODUCTION_SHOW);
		this.availableProduction =availableProduction;
	}

	public ArrayList<Integer> getAvailableProduction() {
		return availableProduction;
	}
}
