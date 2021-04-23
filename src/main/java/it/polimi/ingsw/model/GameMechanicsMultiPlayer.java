package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameDemo;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.singleplayer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GameMechanicsMultiPlayer {
	
	GameController gameDemo;
	
	private Market market;
	private DevelopmentCardsDeck gameDevCardsDeck;
	private Player[] players;
	
	private final int numberOfPlayers;
	private int lastReportClaimed;
	
	int startingPlayer; //index of starting player: from 0 to 3 maximum
	
	public GameMechanicsMultiPlayer(GameController controller, int players) {
		numberOfPlayers = players;
		this.gameDemo = controller;
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
		
		for (int i = 0; i < numberOfPlayers; i++) {
			playersTracks[i] = new FaithTrack(xmlTiles, reportPoints);
			players[i] = instantiatePlayer(gameLeaders[i], playersTracks[i], rules, i);
		}
		
		startingPlayer = (int) Math.floor(Math.random() * numberOfPlayers);
		
	}
	
	
	/**
	 * to be called once for each player to create in the game
	 * @param playersLeaders the array of 4 leader cards to assign to each player in the game
	 * @param track the player's faith track
	 * @param rules common base production rules object
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
	
	public void assignInitialAdvantage() {
		// add starting resources and faith points to the players
		int round = (startingPlayer + 1) % numberOfPlayers;
		if (numberOfPlayers >= 2) {
			Resources[] resources = gameDemo.requestInitialResource(round,1);
			
			players[round].getPlayersWarehouseDepot().assignInitialResources(resources[0]); //needs to be changed into the chosen resource
		}
		round = (round + 1) % numberOfPlayers;
		if (numberOfPlayers >= 3) {
			Resources[] resources = gameDemo.requestInitialResource(round,1);
			
			players[round].getPlayersWarehouseDepot().assignInitialResources(resources[0]); //needs to be changed into the chosen resource
			players[round].getPlayerFaithTrack().moveMarker(1);
		}
		round = (round + 1) % numberOfPlayers;
		if (numberOfPlayers == 4) {
			Resources[] resources = gameDemo.requestInitialResource(round,2);
			players[round].getPlayersWarehouseDepot().assignInitialResources(resources[0], resources[1]); //needs to be changed into the chosen
			// resource
			players[round].getPlayerFaithTrack().moveMarker(1);
		}
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

	public void winner() {
		ArrayList<Player> winnerPlayers;
		Player winner;
		winner = Arrays.stream(players).max(Comparator.comparing(Player::totalScore)).orElse(null);
		Player finalWinner = winner;
		winnerPlayers=Arrays.stream(players).filter(i -> i.totalScore() == finalWinner.totalScore()).collect(Collectors.toCollection(ArrayList::new));
		if (winnerPlayers.size()==1){
			System.out.println("The Winner is Player " + finalWinner.getPlayerIndex()); //+winner.
		}
		else if (winnerPlayers.size()>1) {
			winner = winnerPlayers.stream().max(Comparator.comparing(Player::numberOfResources)).orElse(null);
			Player finalWinner1 = winner;
			winnerPlayers = Arrays.stream(players).filter(i -> i.totalScore() == finalWinner1.totalScore()).collect(Collectors.toCollection(ArrayList::new));
			if(winnerPlayers.size()==1){
				System.out.println("The Winner is Player "+ finalWinner.getPlayerIndex());
			}
			else System.out.println("Draw");
		}
	}

	public FaithTrack getLorenzoFaithTrack() {
		return null;
	}

	public Token revealTop(){
		return null;
	}

}
