package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.util.TokenType;

public class BlackCrossToken extends Token{

	private final int tileNumber;
	private final FaithTrack lorenzoTrack;

	/**
	 * Initialize the attributes
	 * @param lorenzoTrack faith track of Lorenzo
	 */
	protected BlackCrossToken(FaithTrack lorenzoTrack){

		super(TokenType.BLACK_CROSS_TOKEN,null);
		this.tileNumber = 2;
		this.lorenzoTrack = lorenzoTrack;

	}

	/**
	 * Move the marker of Lorenzo of tileNumber positions
	 * @return false because this token doesn't trigger the shuffling of the token deck
	 */
	@Override
	public boolean applyEffect(){
		lorenzoTrack.moveMarker(tileNumber);
		setEndGame(lorenzoTrack.isTrackFinished());
		return false;
	}
}
