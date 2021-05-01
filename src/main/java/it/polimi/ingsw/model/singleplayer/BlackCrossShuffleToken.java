package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.util.TokenType;

public class BlackCrossShuffleToken extends Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	/**
	 * Initialize the attributes
	 * @param tileNumber number of tiles Lorenzo's marker has to move when this token is revealed
	 * @param lorenzoTrack faith track of Lorenzo
	 */
	public BlackCrossShuffleToken(int tileNumber, FaithTrack lorenzoTrack) {
		super(TokenType.BLACK_CROSS_SHUFFLE_TOKEN);
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
}
