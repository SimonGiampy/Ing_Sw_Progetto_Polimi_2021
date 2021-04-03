package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.FaithTrack;

public class BlackCrossToken implements Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	protected BlackCrossToken(int tileNumber, FaithTrack lorenzoTrack){

		this.tileNumber = tileNumber;
		this.lorenzoTrack = lorenzoTrack;

	}

	@Override
	public boolean applyEffect(){
		lorenzoTrack.moveMarker(tileNumber);
		return false;
	}
}
