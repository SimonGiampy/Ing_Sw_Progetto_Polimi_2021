package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the strongbox to the player
 */
public class StrongboxShow extends Message {

	private final ReducedStrongbox strongbox;

	public StrongboxShow(ReducedStrongbox strongbox){
		super("server", MessageType.STRONGBOX_SHOW);
		this.strongbox=strongbox;
	}

	public ReducedStrongbox getStrongbox() {
		return strongbox;
	}
}
