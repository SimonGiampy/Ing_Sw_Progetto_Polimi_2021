package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;

/**
 * the server sends the faith track to the player
 */
public class FaithTrackShow extends Message{

	private ReducedFaithTrack faithTrack;

	public FaithTrackShow(ReducedFaithTrack faithTrack){
		super("server", MessageType.FAITH_TRACK_SHOW);
		this.faithTrack=faithTrack;
	}

	public ReducedFaithTrack getFaithTrack() {
		return faithTrack;
	}

}
