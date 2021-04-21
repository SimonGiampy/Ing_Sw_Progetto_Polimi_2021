package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;

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
