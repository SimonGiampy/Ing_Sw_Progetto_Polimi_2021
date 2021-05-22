package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends available production to the player
 */
public class ProductionsAvailable extends Message {
	private final ArrayList<Productions> availableProduction;

	public ProductionsAvailable(String nickname,ArrayList<Productions> availableProduction){
		super(nickname, MessageType.PRODUCTION_SHOW);
		this.availableProduction =availableProduction;
	}

	public ArrayList<Productions> getAvailableProduction() {
		return availableProduction;
	}
}
