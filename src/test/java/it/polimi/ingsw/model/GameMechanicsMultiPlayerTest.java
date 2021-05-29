package it.polimi.ingsw.model;

import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameMechanicsMultiPlayerTest {
	
	GameMechanicsMultiPlayer game;
	
	@BeforeEach
	void setUp() {
		String fileName = "game_configuration_standard.xml";
		XMLParser parser = new XMLParser(fileName);
		
		game = new GameMechanicsMultiPlayer(4);
		game.instantiateGame(parser.readDevCards(), parser.readLeaderCards(), parser.parseBaseProductionFromXML(),
				parser.readTiles(), parser.readReportPoints());
		
		assertEquals(4, game.getNumberOfPlayers());
		assertEquals(0, game.getLastReportClaimed());
		
	}
	
	
	@Test
	void assignInitialAdvantage() {
		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.STONE);
		game.assignInitialAdvantage(resources, 1);
		game.assignInitialAdvantage(resources, 2);
		
		resources.add(Resources.SHIELD);
		game.assignInitialAdvantage(resources, 0);
	}
	
	@Test
	void winningPlayersDraw() {
		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.STONE);
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		
		// all the players have 1 points since they didn't move from their initial spot
		
		// same resources assigned to everyone
		game.getPlayers()[0].getMyStrongbox().storeResources(resources);
		game.getPlayers()[1].getMyStrongbox().storeResources(resources);
		game.getPlayers()[2].getMyStrongbox().storeResources(resources);
		
		// calculation of winning players
		int[] winners = game.winningPlayers();
		
		int[] expected = new int[]{1, 0, 1, 2};
		assertArrayEquals(expected, winners);
		
		printWinners(winners);
	}
	
	@Test
	void singleWinnerWithPoints() {
		game.getPlayer(0).getPlayersCardManager().addCard(game.getGameDevCardsDeck().getCardStackStructure()[2][3].get(0), 3);
		assertTrue(game.getPlayer(0).totalScore() >= 9 && game.getPlayer(0).totalScore() <= 12);
		
		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.STONE);
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		
		// same resources assigned to these 3 players
		game.getPlayer(0).getMyStrongbox().storeResources(resources);
		game.getPlayer(1).getMyStrongbox().storeResources(resources);
		game.getPlayer(2).getMyStrongbox().storeResources(resources);
		
		// calculation of winning players
		int[] winners = game.winningPlayers();
		assertEquals(0, winners[1]); // winner
		assertEquals(2, winners.length);
		
		printWinners(winners);
	}
	
	@Test
	void singleWinnerWithResources() {
		game.getPlayer(1).getPlayersCardManager().addCard(game.getGameDevCardsDeck().getCardStackStructure()[2][3].get(0), 3);
		game.getPlayer(2).getPlayersCardManager().addCard(game.getGameDevCardsDeck().getCardStackStructure()[2][3].get(0), 3);
		assertTrue(game.getPlayer(1).totalScore() >= 9 && game.getPlayer(0).totalScore() <= 12);
		assertTrue(game.getPlayer(2).totalScore() >= 9 && game.getPlayer(0).totalScore() <= 12);
		
		// same victory points, different resources
		
		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.STONE);
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		
		game.getPlayer(1).getMyStrongbox().storeResources(resources);
		
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		game.getPlayer(2).getMyStrongbox().storeResources(resources);
		
		int[] winners = game.winningPlayers();
		assertEquals(2, winners[1]); // winner
		assertEquals(2, winners.length);
		
		printWinners(game.winningPlayers());
	}
	
	private void printWinners(int[] winners) {
		System.out.print("Winning players' indexes with " + winners[0] + " points are: ");
		for (int i = 1; i < winners.length; i++) {
			System.out.print(winners[i] + "; ");
		}
		System.out.print("\n");
	}
	
	
	@Test
	void increaseLastReportClaimed() {
		game.increaseLastReportClaimed();
	}
	
	@Test
	void getStartingPlayer() {
		assertTrue(game.getStartingPlayer() >= 0 && game.getStartingPlayer() <= 4);
	}
	
	@Test
	void getMarket() {
		CLI cli = new CLI();
		cli.showMarket(new ReducedMarket(game.getMarket()));
	}
}