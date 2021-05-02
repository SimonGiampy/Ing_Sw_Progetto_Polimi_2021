package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.DevelopmentCardsDeck;
import it.polimi.ingsw.model.util.TokenType;

public class DiscardToken extends Token{

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
		super(TokenType.DISCARD_TOKEN,color);
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
		for(int i = 0; i < 2; i++)
			cardsDeck.discard2Cards(color);
		return false;
	}

}
