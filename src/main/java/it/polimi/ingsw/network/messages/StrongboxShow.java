package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Strongbox;
import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;

public class StrongboxShow extends Message{

	private ReducedStrongbox strongbox;

	public StrongboxShow(ReducedStrongbox strongbox){
		super("server",MessageType.STRONGBOX_SHOW);
		this.strongbox=strongbox;
	}

	public ReducedStrongbox getStrongbox() {
		return strongbox;
	}
}
