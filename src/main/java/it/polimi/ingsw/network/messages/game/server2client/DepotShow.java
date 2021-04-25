package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the server sends the depot to the player
 */
public class DepotShow extends Message {

	 private final ReducedWarehouseDepot depot;
	 private final int action; // 0: show, 1: ask action

	public DepotShow(ReducedWarehouseDepot depot, int action){
		super("server", MessageType.DEPOT_SHOW);
		this.depot=depot;
		this.action = action;
	}

	public ReducedWarehouseDepot getDepot() {
		return depot;
	}

	public int getAction() {
		return action;
	}
}
