package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Collections;

public class GameMechanicsMultiPlayer {
	
	//TODO: integrate this class with the XML parser classes, and pass the data to this class
	
	private Market market;
	private DevelopmentCardsDeck gameDevCardsDeck;
	private Player[] players;
	
	private final int numberOfPlayers;
	
	public GameMechanicsMultiPlayer(int players) {
		numberOfPlayers = players;
	}
	
	
	/**
	 * create common instances shared by every player. Then instantiate all the players in the game (from 2 to 4).
	 * All the data in input is received from the XML parser, since they represent the base components of the game data.
	 * @param allLeaderCards a list of all the leader cards present in the XML. Will be shuffled
	 * @param rules the base production rule
	 * @param xmlTiles the list of tiles for the creation of the faith tracks
	 * @param reportPoints points to earn from each report zone
	 */
	public void instantiateGame(ArrayList<DevelopmentCard> allDevelopmentCards, ArrayList<LeaderCard> allLeaderCards, ProductionRules rules,
	                            ArrayList<Tile> xmlTiles, ArrayList<Integer> reportPoints) {
		gameDevCardsDeck = new DevelopmentCardsDeck(createCommonCardsDeck(allDevelopmentCards));
		market = new Market();
		players = new Player[numberOfPlayers];
		
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
	 * to be called once for each player to create in the game
	 * @param playersLeaders the array of 4 leader cards to assign to each player in the game
	 * @param track the player's faith track
	 * @param rules common base production rules object
	 * @return Player instance created
	 */
	public Player instantiatePlayer(LeaderCard[] playersLeaders, FaithTrack track, ProductionRules rules) {
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck resourceDeck = new ResourceDeck(depot);
		Strongbox strongbox = new Strongbox();
		CardManagement cardManagement = new CardManagement(strongbox, depot, rules);
		
		return new Player(market, gameDevCardsDeck, depot, strongbox, resourceDeck, track, cardManagement, playersLeaders);
	}

	
	/**
	 * read all the development cards from the XML and group them by color and level.
	 * Then shuffle them in piles of 4 (standard number of cards) and create the cards deck matrix
	 * @return the matrix representing the piles of development cards
	 */
	public ArrayList<DevelopmentCard>[][] createCommonCardsDeck(ArrayList<DevelopmentCard> developmentCards) {
		//TODO: create cards with parameters read from the XML configuration file and instantiate all the DevCards
		ArrayList<DevelopmentCard>[][] matrixDeck = new ArrayList[3][4]; //fixed matrix size
		//NOTE: the warning above cannot be fixed in any way but the code is absolutely correct
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				matrixDeck[i][j] = new ArrayList<DevelopmentCard>();
			}
		}
		
		assert developmentCards.size() % 12 == 0; // in order to create a grid
		int level, color;
		for (DevelopmentCard card: developmentCards) {
			level = card.getLevel() - 1;
			color = card.getColor().getColorNumber();
			matrixDeck[level][color].add(card);
		}
		
		return matrixDeck;
	}
	
	
	
}
