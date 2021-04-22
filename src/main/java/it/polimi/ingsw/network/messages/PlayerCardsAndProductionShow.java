package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;

public class PlayerCardsAndProductionShow extends Message{

	private ReducedCardProductionManagement cardProductionManagement;

	public PlayerCardsAndProductionShow(ReducedCardProductionManagement cardProductionManagement){
		super("server",MessageType.PLAYER_CARDS_AND_PRODUCTION_SHOW);
		this.cardProductionManagement=cardProductionManagement;
	}

	public ReducedCardProductionManagement getCardProductionManagement() {
		return cardProductionManagement;
	}
}