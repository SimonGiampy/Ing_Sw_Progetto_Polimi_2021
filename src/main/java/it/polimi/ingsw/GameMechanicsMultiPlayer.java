package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Collections;

public class GameMechanicsMultiPlayer {
	
	//TODO: instantiates the leader cards (4 for each player). Passes the object reference to each Player in the game, who in turn needs to choose
	//      just 2 among the 4 of them
	
	//TODO: integrate this class with the XML parser classes, and pass the data to this class
	
	private Market market;
	private DevelopmentCardsDeck gameDevCardsDeck;
	private Player[] players;
	
	private final int numberOfPlayers;
	
	public GameMechanicsMultiPlayer(int players) {
		numberOfPlayers = players;
	}
	
	
	/* this method is a draft of what the builder will do, before calling instantiateGame
	public void xmlBuilding() {
		TODO: every leader can be instantiated after creating its ability effect instance
		leaderCards[0] = new LeaderCard();
		leaderCards[1] = new LeaderCard();
		leaderCards[2] = new LeaderCard();
		leaderCards[3] = new LeaderCard();

		}
	 */
	
	/**
	 * create common instances shared by every player. Then instantiate all the players in the game (from 2 to 4).
	 * All the data in input is received from the XML parser, since they represent the base components of the game data.
	 * @param allLeaderCards a list of all the leader cards present in the XML. Will be shuffled
	 * @param rules the base production rule
	 * @param faithTrack the same faith track for every player
	 */
	/*
	public void instantiateGame(ArrayList<LeaderCard> allLeaderCards, ProductionRules rules, FaithTrack[] faithTrack) {
		gameDevCardsDeck = new DevelopmentCardsDeck(createCommonCardsDeck());
		market = new Market();
		players = new Player[numberOfPlayers];
		
		Collections.shuffle(allLeaderCards);
		// matrix of 4 columns (one per leader card) and a number of rows
		LeaderCard[][] gameLeaders = new LeaderCard[numberOfPlayers][4];
		
		for (int i = 0; i < numberOfPlayers * 4; i++) {
			gameLeaders[i / 4][i % 4] = allLeaderCards.get(i);
		}
		
		for (int i = 0; i < numberOfPlayers; i++) {
			//TODO: check if the rules apply to all the players, or if the object must be cloned
			players[i] = instantiatePlayer(gameLeaders[i], faithTrack[i], rules);
		}
	}

	/*This will be updated
	public FaithTrack[] getFaithTrackAttributes(ArrayList<Tile> tiles) {
		FaithTrack[] tracks = new FaithTrack[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers * 4; i++) {
			tracks[i] = new FaithTrack(tiles);
		}
		return tracks;
	}

	 */



	
	/** TODO: this is a draft for the player instantiation
	 * to be called once for each player to create in the game
	 * @return Player instance created
	 */
	/*
	public Player instantiatePlayer(LeaderCard[] playersLeaders, FaithTrack track, ProductionRules rules) {
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck resourceDeck = new ResourceDeck(depot);
		FaithTrack track = new FaithTrack(); //TODO: read faith track parameters from XML and input them here
		Strongbox strongbox = new Strongbox();
		ProductionRules productionRules = new ProductionRules(); //TODO: read base production parameters from XML and input them here
		CardManagement cardManagement = new CardManagement(strongbox, depot, productionRules);
		
		AbilityEffectActivation sampleEffect1 = new AdditionalDepotAbility(new ArrayList<>());
		AbilityEffectActivation sampleEffect2 = new WhiteMarbleAbility(new ArrayList<>(), 1);
		
		LeaderCard[] leaderCards = playersLeaders; //TODO: read leader cards parameters from XML and input them here
		
		
		Player player = new Player(market, gameDevCardsDeck, depot, strongbox, resourceDeck, track, cardManagement, leaderCards);
		
		return player;
	}

	
	/**
	 * read all the development cards from the XML and group them by color and level.
	 * Then shuffle them in piles of 4 (standard number of cards) and create the cards deck matrix
	 * @return the matrix representing the piles of development cards
	 */
	/*
	public ArrayList<DevelopmentCard>[][] createCommonCardsDeck() {
		ArrayList<DevelopmentCard>[][] matrixDeck = new ArrayList[3][4]; //fixed matrix size
		for (int i = 0; i < 3; i++) {
			for (Colors j: Colors.values()) {
				//TODO: create cards with parameters read from the XML configuration file and instantiate all the DevCards
				//TODO: order the cards by color and level before passing them to the gameDevCardsDeck
				//matrixDeck[i][j] = new DevelopmentCard();
			}
		}
		return matrixDeck;
	}
	
	/*TODO: create LeaderBuilder class for creating the instances of the leader cards from the data read in the xml parser
	
	public LeaderBuilder xmlDataFromLeader(int leaderCardNumber, int victoryPoints, ArrayList<Resources> requirements,
	                              ArrayList<CardRequirement> cardRequirements) {
		
	}
	
	public LeaderBuilder xmlDataFromLeader(String ability, ArrayList<Resources> list) {
	
	}
	 */
	
	
}
