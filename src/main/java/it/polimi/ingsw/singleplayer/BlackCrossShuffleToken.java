package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.FaithTrack;

public class BlackCrossShuffleToken implements Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	public BlackCrossShuffleToken(int tileNumber, FaithTrack lorenzoTrack) {
		this.tileNumber = tileNumber;
		this.lorenzoTrack = lorenzoTrack;
	}

	@Override
	public boolean applyEffect(){
		lorenzoTrack.moveMarker(tileNumber);
		return true;
	}
}
