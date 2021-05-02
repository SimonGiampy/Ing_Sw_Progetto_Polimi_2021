package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.TokenType;

public abstract class Token {

	private final TokenType tokenType;
	private final Colors color;
	private boolean endGame;

	public Token(TokenType tokenType, Colors color){
		this.tokenType=tokenType;
		this.color=color;
		this.endGame=false;
	}
	/**
	 * Apply the effect of the drawn token
	 * @return true if the token is the one that triggers the shuffling of the deck, false otherwise
	 */
	 public abstract boolean applyEffect();

	public TokenType getTokenType() {
		return tokenType;
	}

	public Colors getColor() {
		return color;
	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}
}
