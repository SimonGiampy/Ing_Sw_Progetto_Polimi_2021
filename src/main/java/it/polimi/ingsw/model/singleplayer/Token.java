package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.util.TokenType;

public abstract class Token {

	private final TokenType tokenType;


	public Token(TokenType tokenType){
		this.tokenType=tokenType;
	}
	/**
	 * Apply the effect of the drawn token
	 * @return true if the token is the one that triggers the shuffling of the deck, false otherwise
	 */
	 public abstract boolean applyEffect();

	public TokenType getTokenType() {
		return tokenType;
	}
}
