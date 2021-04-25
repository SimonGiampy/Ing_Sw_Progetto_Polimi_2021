package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends cards and productions to the player
 */
public class PlayerCardsAndProductionShow extends Message {

	private final ReducedCardProductionManagement cardProductionManagement;

	public PlayerCardsAndProductionShow(ReducedCardProductionManagement cardProductionManagement){
		super("server", MessageType.PLAYER_CARDS_AND_PRODUCTION_SHOW);
		this.cardProductionManagement=cardProductionManagement;
	}

	public ReducedCardProductionManagement getCardProductionManagement() {
		return cardProductionManagement;
	}
}
