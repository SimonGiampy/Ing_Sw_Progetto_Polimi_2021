package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.DevelopmentCardsDeck;

public class DiscardToken implements Token{

	private final int cardsNumber;
	private final Colors color;
	private final DevelopmentCardsDeck cardsDeck;

	/**
	 * Initialize the parameters
	 * @param cardsNumber number of cards to discard
	 * @param color color of the cards to discard
	 * @param cardsDeck deck of development cards
	 */
	public DiscardToken(int cardsNumber, Colors color, DevelopmentCardsDeck cardsDeck){
		this.cardsNumber = cardsNumber;
		this.color = color;
		this.cardsDeck = cardsDeck;
	}

	/**
	 * Discards the two cards of the designated color from the deck
	 * @return false because this type of token doesn't activate the shuffling
	 */
	@Override
	public boolean applyEffect() {
		for(int i = 0; i < cardsNumber || cardsDeck.lowestCardLevelAvailable(color) > 0; i++)
			cardsDeck.claimCard(cardsDeck.lowestCardLevelAvailable(color), color);
		return false;
	}
	@Override
	public void showToken(){
		System.out.println("Discard token");
	}
}