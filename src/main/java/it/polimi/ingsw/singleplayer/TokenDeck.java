package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.Colors;

import java.util.ArrayList;
import java.util.Collections;

// This class is totally USELESS, the code will go in GameMechanicsSinglePlayer 100%

public class TokenDeck{

	ArrayList<Token> tokenList;

	public TokenDeck(){
		tokenList = new ArrayList<>();

		//Two BlackCrossToken +2
		tokenList.add(new BlackCrossToken(2));
		tokenList.add(new BlackCrossToken(2));

		//One BlackCrossToken +1
		tokenList.add(new BlackCrossToken(1));

		//One DiscardToken for each color
		tokenList.add(new DiscardToken(2, Colors.BLUE));
		tokenList.add(new DiscardToken(2, Colors.GREEN));
		tokenList.add(new DiscardToken(2, Colors.PURPLE));
		tokenList.add(new DiscardToken(2, Colors.YELLOW));

		Collections.shuffle(tokenList);
	}



	/**
	 * Shuffles the deck
	 */
	protected void shuffleDeck(){
		Collections.shuffle(tokenList);
	}
}
