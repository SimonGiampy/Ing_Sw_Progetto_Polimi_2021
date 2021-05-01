package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the server sends a list of buyable dev cards to the player, alongside with a flag indicating if the previous request was valid or not
 */
public class BuyableDevCards extends Message {

	private final ArrayList<DevelopmentCard> cards;
	private final boolean wrongSlot; // true if the chosen slot was incorrect

	public BuyableDevCards(ArrayList<DevelopmentCard> cards, boolean wrongSlot){
		super("server", MessageType.CARDS_SHOW);
		this.cards = cards;
		this.wrongSlot = wrongSlot;
	}

	public ArrayList<DevelopmentCard> getCards() {
		return cards;
	}
	
	public boolean isWrongSlot() {
		return wrongSlot;
	}
}
