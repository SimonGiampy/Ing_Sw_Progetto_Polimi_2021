package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * // the server sends buyable cards to the player
 */
public class CardsShow extends Message {

	private ArrayList<DevelopmentCard> cards;

	public CardsShow(ArrayList<DevelopmentCard> cards){
		super("server", MessageType.CARDS_SHOW);
		this.cards=cards;
	}

	public ArrayList<DevelopmentCard> getCards() {
		return cards;
	}
}
