package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.FaithTrack;

public class BlackCrossToken implements Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	/**
	 * Initialize the attributes
	 * @param tileNumber number of tiles Lorenzo's marker has to move when this token is revealed
	 * @param lorenzoTrack faith track of Lorenzo
	 */
	protected BlackCrossToken(int tileNumber, FaithTrack lorenzoTrack){

		this.tileNumber = tileNumber;
		this.lorenzoTrack = lorenzoTrack;

	}

	/**
	 * Move the marker of Lorenzo of tileNumber positions
	 * @return false because this token doesn't trigger the shuffling of the token deck
	 */
	@Override
	public boolean applyEffect(){
		lorenzoTrack.moveMarker(tileNumber);
		return false;
	}
}
