package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.Colors;
import it.polimi.ingsw.DevelopmentCardsDeck;

public class DiscardToken implements Token{

	private final int cardsNumber;
	private final Colors color;
	private final DevelopmentCardsDeck cardsDeck;

	public DiscardToken(int cardsNumber, Colors color, DevelopmentCardsDeck cardsDeck){
		this.cardsNumber = cardsNumber;
		this.color = color;
		this.cardsDeck = cardsDeck;
	}

	@Override
	public boolean applyEffect(){
		cardsDeck.claimCard(cardsDeck.lowestCardLevelAvailable(color), color);
		return false;
	}
}
