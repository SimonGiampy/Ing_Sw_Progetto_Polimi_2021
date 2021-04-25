package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the server sends cards deck
 */
public class CardsDeckShow extends Message {

	private final ReducedDevelopmentCardsDeck cardsDeck;

	public CardsDeckShow(ReducedDevelopmentCardsDeck cardsDeck){
		super("server", MessageType.CARDS_DECK_SHOW);
		this.cardsDeck=cardsDeck;
	}

	public ReducedDevelopmentCardsDeck getCardsDeck() {
		return cardsDeck;
	}
}
