package it.polimi.ingsw.singleplayer;

import it.polimi.ingsw.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the implementation of the single player mode for the game. Extends the multiplayer one since
 * most of the methods and attributes are shared and common.
 */
public class GameMechanicsSinglePlayer extends GameMechanicsMultiPlayer {

	private ArrayList<Token> tokenList;
	private FaithTrack lorenzoFaithTrack;

	public GameMechanicsSinglePlayer(){
		super(1);
	}
	
	/**
	 * constructor for creating the single player instances and Lorenzo's personal Faith track
	 * @param allDevelopmentCards the list of all the development cards read from the xml file. Will be shuffled
	 * @param allLeaderCards a list of all the leader cards present in the XML. Will be shuffled
	 * @param rules the base production rule
	 * @param xmlTiles the list of tiles for the creation of the faith tracks
	 * @param reportPoints points to earn from each report zone
	 */
	@Override
	public void instantiateGame(ArrayList<DevelopmentCard> allDevelopmentCards, ArrayList<LeaderCard> allLeaderCards, ProductionRules rules,
								ArrayList<Tile> xmlTiles, ArrayList<Integer> reportPoints) {
		
		lorenzoFaithTrack = new FaithTrack(xmlTiles, reportPoints);
		
		super.instantiateGame(allDevelopmentCards, allLeaderCards, rules, xmlTiles, reportPoints);

		tokenList = new ArrayList<>();
		//Two BlackCrossToken +2
		tokenList.add(new BlackCrossToken(2, lorenzoFaithTrack));
		tokenList.add(new BlackCrossToken(2, lorenzoFaithTrack));
		//One BlackCrossToken +1
		tokenList.add(new BlackCrossShuffleToken(1, lorenzoFaithTrack));
		//One DiscardToken for each color
		tokenList.add(new DiscardToken(2, Colors.BLUE, super.getGameDevCardsDeck()));
		tokenList.add(new DiscardToken(2, Colors.GREEN, super.getGameDevCardsDeck()));
		tokenList.add(new DiscardToken(2, Colors.PURPLE, super.getGameDevCardsDeck()));
		tokenList.add(new DiscardToken(2, Colors.YELLOW, super.getGameDevCardsDeck()));

		Collections.shuffle(tokenList);

	}
	
	/**
	 * Reveals the top token of the deck, removes it, appends it and returns it
	 * @return the first token of the deck
	 */
	public Token revealTop() {
		Token topToken;
		topToken = tokenList.get(0);
		tokenList.remove(0);
		tokenList.add(topToken);
		return topToken;
	}
	
	/**
	 * getter for the Player object
	 * @return single instance of player
	 */
	public Player getPlayer() {
		return super.getPlayers()[0];
	}

	/**
	 * Shuffles the token deck
	 */
	public void shuffleTokenDeck(){
		Collections.shuffle(tokenList);
	}
}
