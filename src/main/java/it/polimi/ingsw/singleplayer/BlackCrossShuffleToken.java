package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.FaithTrack;

public class BlackCrossShuffleToken implements Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	/**
	 * Initialize the attributes
	 * @param tileNumber number of tiles Lorenzo's marker has to move when this token is revealed
	 * @param lorenzoTrack faith track of Lorenzo
	 */
	public BlackCrossShuffleToken(int tileNumber, FaithTrack lorenzoTrack) {
		this.tileNumber = tileNumber;
		this.lorenzoTrack = lorenzoTrack;
	}

	/**
	 * Move Lorenzo's marker of tileNumber positions
	 * @return true because triggers the shuffling of the deck
	 */
	@Override
	public boolean applyEffect(){
		lorenzoTrack.moveMarker(tileNumber);
		return true;
	}

	@Override
	public void showToken(){
		System.out.println("Black Cross Shuffle Token +1 for Lorenzo");
	}
}
