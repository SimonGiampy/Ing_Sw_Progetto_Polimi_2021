package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;

public class DepotShow extends Message{

	 private ReducedWarehouseDepot depot;

	public DepotShow(ReducedWarehouseDepot depot){
		super("server",MessageType.DEPOT_SHOW);
		this.depot=depot;
	}

	public ReducedWarehouseDepot getDepot() {
		return depot;
	}
}
