package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;
import java.util.Collections;

public class GameMechanicsMultiPlayer {
	
	private Market market;
	private DevelopmentCardsDeck gameDevCardsDeck;
	private Player[] players;

	private final int numberOfPlayers;
	private int lastReportClaimed;
	
	int startingPlayer; //index of starting player: from 0 to 3 maximum

	/**
	 * constructor that sets the number if players
	 * @param players number of players
	 */
	public GameMechanicsMultiPlayer(int players) {
		numberOfPlayers = players;
	}
	
	
	/**
	 * create common instances shared by every player. Then instantiate all the players in the game (from 2 to 4).
	 * All the data in input is received from the XML parser, since they represent the base components of the game data.
	 * @param allDevelopmentCards the list of all the development cards read from the xml file. Will be shuffled
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
		lastReportClaimed = 0;
		
		Collections.shuffle(allLeaderCards);
		// matrix of 4 columns (one per leader card) and a number of rows
		LeaderCard[][] gameLeaders = new LeaderCard[numberOfPlayers][4];
		FaithTrack[] playersTracks = new FaithTrack[numberOfPlayers];
		
		for (int i = 0; i < numberOfPlayers * 4; i++) {
			gameLeaders[i / 4][i % 4] = allLeaderCards.get(i);
		}
		//boolean single = numberOfPlayers == 1;
		for (int i = 0; i < numberOfPlayers; i++) {
			playersTracks[i] = new FaithTrack(xmlTiles, reportPoints, false);
			players[i] = instantiatePlayer(gameLeaders[i], playersTracks[i], rules, i);
		}
		
		startingPlayer = (int) Math.floor(Math.random() * numberOfPlayers);
		
	}
	
	
	/**
	 * to be called once for each player to create in the game
	 * @param playersLeaders the array of 4 leader cards to assign to each player in the game
	 * @param track the player's faith track
	 * @param rules common base production rules object
	 * @param playerIndex index of the player to be created, from 0 up to 3
	 * @return Player instance created
	 */
	public Player instantiatePlayer(LeaderCard[] playersLeaders, FaithTrack track, ProductionRules rules, int playerIndex) {
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck resourceDeck = new ResourceDeck(depot);
		Strongbox strongbox = new Strongbox();
		CardProductionsManagement cardProductionsManagement = new CardProductionsManagement(strongbox, depot, rules);
		
		return new Player(market, gameDevCardsDeck, depot, strongbox, resourceDeck, track, cardProductionsManagement,
				playersLeaders, playerIndex);
	}

	
	/**
	 * read all the development cards from the XML and group them by color and level.
	 * Then shuffle them in piles of 4 (standard number of cards) and create the cards deck matrix
	 * @param developmentCards list of development cards read from xml
	 * @return the matrix representing the piles of development cards
	 */
	public ArrayList<DevelopmentCard>[][] createCommonCardsDeck(ArrayList<DevelopmentCard> developmentCards) {
		ArrayList<DevelopmentCard>[][] matrixDeck = new ArrayList[3][4]; //fixed matrix size
		//NOTE: the warning above cannot be fixed in any way but the code is absolutely correct
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				matrixDeck[i][j] = new ArrayList<>();
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


	/**
	 * handles the assigning of the initial resources and faith points based on the order of the players
	 * @param resources resources chosen by the player
	 * @param playerIndex index of the current player
	 */
	public void assignInitialAdvantage(ArrayList<Resources> resources, int playerIndex) {
		if (playerIndex==1)
			players[playerIndex].getPlayersWarehouseDepot().assignInitialResources(resources.get(0));
		else if (playerIndex==2) {
			players[playerIndex].getPlayersWarehouseDepot().assignInitialResources(resources.get(0));
			players[playerIndex].getPlayerFaithTrack().moveMarker(1);
		}
		else{
			players[playerIndex].getPlayersWarehouseDepot().assignInitialResources(resources.get(0),resources.get(1));
			players[playerIndex].getPlayerFaithTrack().moveMarker(1);
		}
	}
	
	/**
	 * Decides the winner or winners of the match, by calculating the points and the number of resources gained
	 * @return an array of integers where:
	 *          the first number is the total score achieved by the player or players
	 *          the second number is the index of the winning player
	 *          the other numbers (if present) are the indexes of the other winning players (Draw)
	 */
	public int[] winningPlayers() {
		ArrayList<Player> winnersPoints = new ArrayList<>();
		int[] winners;
		int maxScore = 0, maxRes = 0;
		
		for (int i = 0; i < numberOfPlayers; i++) {
			if (getPlayer(i).totalScore() > maxScore) {
				maxScore = getPlayer(i).totalScore();
			}
		}
		for (int i = 0; i < numberOfPlayers; i++) {
			if (getPlayer(i).totalScore() == maxScore) {
				winnersPoints.add(getPlayer(i));
			}
		}
		
		// calculation based on the number of resources
		if (winnersPoints.size() == 1) { // calculation based on the total points gained
			winners = new int[2];
			winners[0] = winnersPoints.get(0).totalScore();
			winners[1] = winnersPoints.get(0).getPlayerIndex();
		} else {
			// calculation based on the number of resources accumulated among the players with top score
			ArrayList<Player> winnersResources = new ArrayList<>();
			for (Player winnersPoint : winnersPoints) {
				if (winnersPoint.numberOfResources() > maxRes) {
					maxRes = winnersPoint.numberOfResources();
				}
			}
			for (Player winnersPoint : winnersPoints) {
				if (winnersPoint.numberOfResources() == maxRes) {
					winnersResources.add(winnersPoint);
				}
			}
			
			if (winnersResources.size() == 1) { // unique winner
				winners = new int[2];
				winners[0] = winnersResources.get(0).totalScore();
				winners[1] = winnersResources.get(0).getPlayerIndex();
			} else {
				// since the number of resources is the same for the winners, then there is a draw
				winners = new int[winnersResources.size() + 1];
				winners[0] = winnersResources.get(0).totalScore();
				for (int i = 0; i < winnersResources.size(); i++) {
					winners[i + 1] = winnersResources.get(i).getPlayerIndex();
				}
			}
		}
		
		return winners;
	}

	public Market getMarket() {
		return market;
	}

	public DevelopmentCardsDeck getGameDevCardsDeck() {
		return gameDevCardsDeck;
	}

	public Player[] getPlayers() {
		return players;
	}

	public Player getPlayer(int which) {
		return players[which];
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public int getLastReportClaimed() {
		return lastReportClaimed;
	}

	public void increaseLastReportClaimed(){
		lastReportClaimed++;
	}

	public int getStartingPlayer() {
		return startingPlayer;
	}

	
}
