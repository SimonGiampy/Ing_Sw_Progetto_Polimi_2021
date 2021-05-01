package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the server sends the depot to the player, and information regarding the status of the player's previous interaction
 */
public class DepotReply extends Message {

	private final ReducedWarehouseDepot depot;
	private final boolean initialMove; // signals the view to show the instructions and syntax
	private final boolean confirmationAvailable; //signals the view that the server accepts confirmation
	private final boolean validMove; //signals if the server validated and applied the requested move

	public DepotReply(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean validMove){
		super("server", MessageType.DEPOT_CONFIRMATION);
		this.depot = depot;
		this.initialMove = initialMove;
		this.confirmationAvailable = confirmationAvailable;
		this.validMove = validMove;
	}

	public ReducedWarehouseDepot getDepot() {
		return depot;
	}
	public boolean isInitialMove() {
		return initialMove;
	}
	
	public boolean isConfirmationAvailable() {
		return confirmationAvailable;
	}
	
	public boolean isMoveValid() {
		return validMove;
	}
}
