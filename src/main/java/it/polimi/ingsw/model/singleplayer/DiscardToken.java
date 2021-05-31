package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.DevelopmentCardsDeck;
import it.polimi.ingsw.model.util.TokenType;

public class DiscardToken extends Token{
	
	private final Colors color;
	private final DevelopmentCardsDeck cardsDeck;

	/**
	 * Initialize the parameters
	 * @param color color of the cards to discard
	 * @param cardsDeck deck of development cards
	 */
	public DiscardToken(Colors color, DevelopmentCardsDeck cardsDeck){
		super(TokenType.DISCARD_TOKEN,color);
		this.color = color;
		this.cardsDeck = cardsDeck;
	}

	/**
	 * Discards the two cards of the designated color from the deck
	 * @return false because this type of token doesn't activate the shuffling
	 */
	@Override
	public boolean applyEffect() {
		setEndGame(cardsDeck.discard2Cards(color));
		return false;
	}

}
