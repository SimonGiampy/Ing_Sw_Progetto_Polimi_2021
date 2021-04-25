package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the faith track to the player
 */
public class FaithTrackShow extends Message {

	private final ReducedFaithTrack faithTrack;

	public FaithTrackShow(ReducedFaithTrack faithTrack){
		super("server", MessageType.FAITH_TRACK_SHOW);
		this.faithTrack=faithTrack;
	}

	public ReducedFaithTrack getFaithTrack() {
		return faithTrack;
	}

}
