package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;

public class DepotShow extends Message{

	 private final ReducedWarehouseDepot depot;
	 private final int action; // 0: show, 1: ask action

	public DepotShow(ReducedWarehouseDepot depot, int action){
		super("server",MessageType.DEPOT_SHOW);
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
