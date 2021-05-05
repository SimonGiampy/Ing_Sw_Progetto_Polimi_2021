package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.ServerSideController;
import it.polimi.ingsw.controller.GameDemo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest { //TODO: this needs to be corrected

	Player player;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeEach
	void setUp() {

		System.setOut(new PrintStream(outputStreamCaptor));

		GameDemo demo = new GameDemo();
		GameMechanicsMultiPlayer mechanics = new GameMechanicsMultiPlayer(2);
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> allLeaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		DevelopmentCardsDeck gameDevCardsDeck = new DevelopmentCardsDeck(mechanics.createCommonCardsDeck(devCards));
		int numberOfPlayers = 2;
		Market market = new Market();
		Player[] players = new Player[numberOfPlayers];

		Collections.shuffle(allLeaderCards);
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
	}

	@Test
	void chooseTwoLeaders() {

		LeaderCard[] cards = player.getLeaderCards();
		for(int i = 0; i < 4; i++){
			//cards[i].showLeader();
		}
		player.chooseTwoLeaders(2,4);
		assertSame(player.getLeaderCards()[0], cards[1]);
		assertSame(player.getLeaderCards()[1], cards[3]);
		//player.getLeaderCards()[0].showLeader();
		//player.getLeaderCards()[1].showLeader();
	}

	@Test
	void discardLeaderCard() {

		player.chooseTwoLeaders(2,4);
		//player.getLeaderCards()[0].showLeader();
		//player.getLeaderCards()[1].showLeader();

		//player.discardLeaderCard(1);
		//assertFalse(player.isPlayableLeader1());
		assertSame(player.getPlayerFaithTrack().getCurrentPosition(), 1);

		player.discardLeaderCard(2);
		//assertFalse(player.isPlayableLeader2());
		assertSame(player.getPlayerFaithTrack().getCurrentPosition(), 2);
	}
/*
	@Test
	void interactWithMarket() {
		//This method is really hard to test because of the instantiation of player and the call to
		//addIncomingResources in the deck
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Exception e = assertThrows(InvalidUserRequestException.class, () ->
				player.interactWithMarket("a", 2));
		assertEquals(e.getMessage(), "Command for using market is not correct");

		//Test with no white marble leader activated
		try {
			Marbles[][] marbles = player.getCommonMarket().getMarket();
			player.interactWithMarket("row", 1);
			ArrayList<Resources> list= player.getPlayersWarehouseDepot().getIncomingResources();
		} catch (InvalidUserRequestException invalidUserRequestException) {
			invalidUserRequestException.printStackTrace();
		}

		//assertEquals(outputStreamCaptor.toString(), );

	}

 */

	@Test
	void checkWhatThisPlayerCanDo() {
	}

	@Test
	void isEndgameStarted() {
	}

	@Test
	void totalScore() {
	}

	@Test
	void isBuyMoveAvailable() {
	}

	@Test
	void numberOfResources() {
	}

	@Test
	void buyNewDevCard() {
	}

	@Test
	void activateProduction() {
	}

	@Test
	void activateLeaderCard() {
	}

	@Test
	void setDiscount() {
	}

	@AfterEach
	public void tearDown() {
		System.setOut(standardOut);
	}


}