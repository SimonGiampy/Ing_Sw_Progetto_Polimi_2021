package it.polimi.ingsw.singleplayer;

public interface Token {

	/**
	 * Apply the effect of the drawn token
	 * @return true if the token is the one that triggers the shuffling of the deck, false otherwise
	 */
	boolean applyEffect();
	void showToken();

}
