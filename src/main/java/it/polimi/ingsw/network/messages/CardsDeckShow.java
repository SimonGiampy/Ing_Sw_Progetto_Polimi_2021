package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;

public class CardsDeckShow extends Message{

	private ReducedDevelopmentCardsDeck cardsDeck;

	public CardsDeckShow(ReducedDevelopmentCardsDeck cardsDeck){
		super("server",MessageType.CARDS_DECK_SHOW);
		this.cardsDeck=cardsDeck;
	}

	public ReducedDevelopmentCardsDeck getCardsDeck() {
		return cardsDeck;
	}
}
