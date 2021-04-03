package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.*;

import java.util.ArrayList;
import java.util.Collections;

public class GameMechanicsSinglePlayer extends GameMechanicsMultiPlayer{

	private ArrayList<Token> tokenList;
	private FaithTrack lorenzoFaithTrack;

	public GameMechanicsSinglePlayer(){
		super(1);
	}

	@Override
	public void instantiateGame(ArrayList<DevelopmentCard> allDevelopmentCards, ArrayList<LeaderCard> allLeaderCards, ProductionRules rules,
								ArrayList<Tile> xmlTiles, ArrayList<Integer> reportPoints) {

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


		gameDevCardsDeck = new DevelopmentCardsDeck(super.createCommonCardsDeck(allDevelopmentCards));
		market = new Market();

		Collections.shuffle(allLeaderCards);
		// matrix of 4 columns (one per leader card) and a number of rows
		LeaderCard[][] gameLeaders = new LeaderCard[numberOfPlayers][4];
		FaithTrack[] playersTracks = new FaithTrack[numberOfPlayers];

		for (int i = 0; i < numberOfPlayers * 4; i++) {
			gameLeaders[i / 4][i % 4] = allLeaderCards.get(i);
			playersTracks[i / 4] = new FaithTrack(xmlTiles, reportPoints);
		}

		for (int i = 0; i < numberOfPlayers; i++) {
			players[i] = instantiatePlayer(gameLeaders[i], playersTracks[i], rules);
		}


	}
	/**
	 * Reveals the top token of the deck, removes it, appends it and returns it
	 * @return the first token of the deck
	 */
	public Token revealTop(){

		Token topToken;
		topToken = tokenList.get(0);
		tokenList.remove(0);
		tokenList.add(topToken);
		return topToken;

	}
}
