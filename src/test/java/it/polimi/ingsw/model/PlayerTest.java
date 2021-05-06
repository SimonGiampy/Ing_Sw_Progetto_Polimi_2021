package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

	Player player;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	DevelopmentCard developmentCard1;
	DevelopmentCard developmentCard2;
	DevelopmentCard developmentCard3;
	DevelopmentCard developmentCard4;
	DevelopmentCard developmentCard5;
	DevelopmentCard developmentCard6;
	DevelopmentCard developmentCard7;

	@BeforeEach
	void setUp() {

		System.setOut(new PrintStream(outputStreamCaptor));
		
		GameMechanicsMultiPlayer mechanics = new GameMechanicsMultiPlayer(2);
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> allLeaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		DevelopmentCardsDeck gameDevCardsDeck = new DevelopmentCardsDeck(mechanics.createCommonCardsDeck(devCards));
		int numberOfPlayers = 2;
		Market commonMarket= new Market();
		Marbles[][] market= new Marbles[3][4];

		ArrayList<Marbles> marblesList = new ArrayList<>(13);
		marblesList.add(Marbles.RED);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.WHITE);
		marblesList.add(Marbles.BLUE);
		marblesList.add(Marbles.BLUE);
		marblesList.add(Marbles.GREY);
		marblesList.add(Marbles.GREY);
		marblesList.add(Marbles.YELLOW);
		marblesList.add(Marbles.YELLOW);
		marblesList.add(Marbles.PURPLE);
		marblesList.add(Marbles.PURPLE);
		Marbles extraBall = marblesList.get(0);
		int k = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				market[i][j] = marblesList.get(k);
				k++;
			}
		}

		Player[] players = new Player[numberOfPlayers];



		//Collections.shuffle(allLeaderCards);
		// matrix of 4 columns (one per leader card) and a number of rows
		LeaderCard[][] gameLeaders = new LeaderCard[numberOfPlayers][4];
		FaithTrack[] playersTracks = new FaithTrack[numberOfPlayers];

		for (int i = 0; i < numberOfPlayers * 4; i++) {
			gameLeaders[i / 4][i % 4] = allLeaderCards.get(i);
		}

		for (int i = 0; i < numberOfPlayers; i++) {
			playersTracks[i] = new FaithTrack(tiles, report, false);
			players[i] = mechanics.instantiatePlayer(gameLeaders[i], playersTracks[i], baseProduction, i);
		}

		this.player = players[0];

		player.setCommonCardsDeckForDebugging(gameDevCardsDeck);
		commonMarket.setMarketForDebugging(market);
		commonMarket.setExtraBallForDebugging(extraBall);
		player.setCommonMarketForDebugging(commonMarket);


		developmentCard1 = new DevelopmentCard(1, Colors.GREEN,1,new ArrayList<>(), baseProduction);
		developmentCard2 = new DevelopmentCard(1,Colors.YELLOW,3, new ArrayList<>(), baseProduction);
		developmentCard3 = new DevelopmentCard(1,Colors.BLUE,2, new ArrayList<>(), baseProduction);
		developmentCard4 = new DevelopmentCard(2,Colors.YELLOW,4, new ArrayList<>(), baseProduction);
		developmentCard5 = new DevelopmentCard(2,Colors.PURPLE,5, new ArrayList<>(), baseProduction);
		developmentCard6 = new DevelopmentCard(2,Colors.GREEN,6, new ArrayList<>(), baseProduction);
		developmentCard7 = new DevelopmentCard(3,Colors.BLUE,10, new ArrayList<>(), baseProduction);


	}

	/**
	 * it checks if the selected leader cards in the first part of the game are correct.
	 */
	@Test
	void chooseTwoLeaders() {
		LeaderCard[] cards = player.getLeaderCards();
		player.chooseTwoLeaders(2,4);
		assertSame(player.getLeaderCards()[0], cards[1]);
		assertSame(player.getLeaderCards()[1], cards[3]);
	}

	/**
	 * it checks if discard leader card action is correct. it also checks if the marker is moved in the faith track.
	 */
	@Test
	void discardLeaderCard() {
		player.chooseTwoLeaders(2,4);

		player.discardLeaderCard(1);
		assertTrue(player.isDiscardedLeader1());
		assertSame(player.getPlayerFaithTrack().getCurrentPosition(), 1);

		player.discardLeaderCard(2);
		assertTrue(player.isDiscardedLeader2());
		assertSame(player.getPlayerFaithTrack().getCurrentPosition(), 2);
	}

	
	@Test
	void interactWithMarket_ROW(){
		Marbles[] check= new Marbles[]{Marbles.BLUE,Marbles.BLUE,Marbles.GREY,Marbles.GREY};
		assertEquals(Arrays.asList(check),Arrays.asList(player.getCommonMarket().shiftRow(1)));
		player.interactWithMarket("ROW",2);
		
		try {
			player.convertMarketOutput(0, 0); // just for testing
		} catch (InvalidUserRequestException e) {
			System.out.println("Invalid input");
		}
	}

	
	@Test
	void interactWithMarket_COL(){
		Marbles[] check= new Marbles[]{Marbles.WHITE,Marbles.BLUE,Marbles.YELLOW};
		assertEquals(Arrays.asList(check),Arrays.asList(player.getCommonMarket().shiftCol(1)));
		player.interactWithMarket("COL",2);
	}

	/**
	 * checks the available leader actions when the player chooses arbitrarily first and third leader
	 */
	@Test
	void checkAvailableLeaderActions(){

		player.chooseTwoLeaders(1,3);
		ArrayList<PlayerActions> check=new ArrayList<>();
		check.add(PlayerActions.DISCARD_LEADER_1);
		check.add(PlayerActions.DISCARD_LEADER_2);
		assertEquals(check,player.checkAvailableLeaderActions());
		check.clear();
		player.getPlayersCardManager().addCard(developmentCard1,1);
		player.getPlayersCardManager().addCard(developmentCard2,2);
		player.getPlayersCardManager().addCard(developmentCard3,3);
		check.add(PlayerActions.PLAY_LEADER_1);
		check.add(PlayerActions.PLAY_LEADER_2);
		check.add(PlayerActions.DISCARD_LEADER_1);
		check.add(PlayerActions.DISCARD_LEADER_2);
		assertEquals(check,player.checkAvailableLeaderActions());
		check.clear();
		player.activateLeaderCard(0);
		player.activateLeaderCard(1);
		assertEquals(check,player.checkAvailableLeaderActions());

	}

	@Test
	void checkAvailableActions(){
		player.chooseTwoLeaders(1,3);
		ArrayList<PlayerActions> check=new ArrayList<>();
		check.add(PlayerActions.MARKET);
		check.add(PlayerActions.LEADER);
		assertEquals(check,player.checkAvailableActions());

	}

	@Test
	void checkAvailableNormalAction(){

		ArrayList<PlayerActions> check=new ArrayList<>();
		ArrayList<Resources> resources=new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				resources.add(Resources.values()[i]);
			}
		}
		player.getMyStrongbox().storeResources(resources);

		check.add(PlayerActions.MARKET);
		check.add(PlayerActions.BUY_CARD);
		check.add(PlayerActions.PRODUCTIONS);

		assertEquals(check,player.checkAvailableNormalActions());

	}

	@Test
	void isEndgameStarted_SevenCards(){
		player.getPlayersCardManager().addCard(developmentCard1,1);
		player.getPlayersCardManager().addCard(developmentCard2,2);
		player.getPlayersCardManager().addCard(developmentCard3,3);

		assertFalse(player.isEndgameStarted());

		player.getPlayersCardManager().addCard(developmentCard4,1);
		player.getPlayersCardManager().addCard(developmentCard5,2);
		player.getPlayersCardManager().addCard(developmentCard6,3);
		player.getPlayersCardManager().addCard(developmentCard7,3);

		assertTrue(player.isEndgameStarted());

	}

	@Test
	void isEndgameStarted_FaithTrackFinished(){
		player.getPlayerFaithTrack().moveMarker(10);
		assertFalse(player.isEndgameStarted());

		player.getPlayerFaithTrack().moveMarker(14);
		assertTrue(player.isEndgameStarted());
	}

	@Test
	void totalScore() {

		player.chooseTwoLeaders(1,3);
		player.getPlayersCardManager().addCard(developmentCard1,1);
		player.getPlayersCardManager().addCard(developmentCard2,2);
		player.getPlayersCardManager().addCard(developmentCard3,3);
		player.getPlayersCardManager().addCard(developmentCard4,1);
		player.getPlayersCardManager().addCard(developmentCard5,2);
		player.getPlayersCardManager().addCard(developmentCard6,3);
		player.getPlayersCardManager().addCard(developmentCard7,3);

		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.SERVANT);
		resources.add(Resources.COIN);
		player.getMyStrongbox().storeResources(resources);

		player.activateLeaderCard(0);
		player.activateLeaderCard(1);

		assertEquals(37,player.totalScore());

	}


	@Test
	void buyNewDevCard() {
	}

	@Test
	void activateProduction() throws InvalidInputException {
		ArrayList<Productions> playerInput = new ArrayList<>();
		playerInput.add(Productions.BASE_PRODUCTION);
		int[] input= new int[]{2,0,0,0};
		int[] output= new int[]{0,1,0,0};
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		player.getMyStrongbox().storeResources(resources);
		player.activateProduction(playerInput,input,output);
		resources.clear();
		resources.add(Resources.SERVANT);
		assertEquals(resources,player.getMyStrongbox().getContent());
	}

	@Test
	void activateLeaderCard() {
		player.activateLeaderCard(0);
		assertTrue(player.isActiveAbilityLeader1());
		assertFalse(player.isDiscardedLeader1());
		player.activateLeaderCard(1);
		assertTrue(player.isActiveAbilityLeader2());
		assertFalse(player.isDiscardedLeader2());

	}

	@Test
	void setDiscount() {
		ArrayList<Resources> discount=new ArrayList<>();
		discount.add(Resources.COIN);
		discount.add(Resources.COIN);
		discount.add(Resources.STONE);
		discount.add(Resources.STONE);
		discount.add(Resources.STONE);
		discount.add(Resources.SHIELD);
		discount.add(Resources.SHIELD);
		discount.add(Resources.SERVANT);
		player.setDiscount(discount);
		assertEquals(2,player.getCoinDiscount());
		assertEquals(3,player.getStoneDiscount());
		assertEquals(2,player.getShieldDiscount());
		assertEquals(1,player.getServantDiscount());
	}

	@AfterEach
	public void tearDown() {
		System.setOut(standardOut);
	}
	
	/**
	 * Fundamental Test
	 */
	@Test
	void nickname() {
		player.setNickname("Giorgio Mastrota");
		assertEquals("Giorgio Mastrota", player.getNickname());
	}
}