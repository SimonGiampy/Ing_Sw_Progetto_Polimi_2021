package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class DepotShow extends Message {
	
	private final ReducedWarehouseDepot depot;
	
	public DepotShow(ReducedWarehouseDepot depot) {
		super("server", MessageType.DEPOT_SHOW);
		this.depot = depot;
	}
	
	public ReducedWarehouseDepot getDepot() {
		return depot;
	}
}
