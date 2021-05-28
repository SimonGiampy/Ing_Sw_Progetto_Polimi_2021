package it.polimi.ingsw.model;

import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.CLIUtils;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardsDeckTest {
	
	DevelopmentCardsDeck deck;
	CardProductionsManagement management;
	Strongbox box;
	CLI cli;
	
	@BeforeEach
	void setUp() {
		String path;
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
		path = file.getAbsolutePath();
		XMLParser parser = new XMLParser(path);
		
		deck = new DevelopmentCardsDeck(new GameMechanicsMultiPlayer(1).createCommonCardsDeck(parser.readDevCards()));
		box = new Strongbox();
		management = new CardProductionsManagement(box, new WarehouseDepot(), parser.parseBaseProductionFromXML());
		cli = new CLI();
	}
	
	@Test
	void getPriceDevCard() {
		System.out.println(deck.getPriceDevCard(2, Colors.BLUE).toString());
	}
	
	@Test
	void canBuyAnyDevCard() {
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
		
		box.storeResources(resources);
		
		assertTrue(deck.canBuyAnyDevCard(resources, management)); // resources are sufficient for any cards in the deck
		// valid only for the standard configuration file
	}
	
	@Test
	void claimAllCardsInAStack() {
		DevelopmentCard card;
		int stackSize = deck.getCardStackStructure()[0][0].size();
		System.out.println("Stack of level 1, Blue colored development cards");
		for (int i = 0; i < stackSize; i++) {
			card = deck.claimCard(1, Colors.BLUE);
			CLIUtils.showDevCard(card);
		}
		
		// all the cards of level 1 and color blue are finished, so the blue colored cards are available only for level 2 and 3
		assertEquals(2, deck.lowestCardLevelAvailable(Colors.BLUE));
		
		// since the dev cards in the selected stack are finished, the stack is empty thus the method returns null
		assertNull(deck.claimCard(1, Colors.BLUE));
		
	}
	
	@Test
	void discard2CardsForSinglePlayer() {
		int stackSize = new ReducedDevelopmentCardsDeck(deck).getCardStackStructure()[0][0].size();
		
		for (int i = 0; i < stackSize / 2 * 3; i++) { // cards discarded twice at a time, for the 3 levels
			assertFalse(deck.discard2Cards(Colors.PURPLE));
		}

		assertTrue(deck.discard2Cards(Colors.PURPLE)); // cannot discard 2 cards of this color anymore
		
	}
	
	
	@Test
	void buyableCards() {
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
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.SHIELD);
		resources.add(Resources.SERVANT);
		resources.add(Resources.COIN);
		
		System.out.println("\nList of buyable dev cards with this set of resources: " + ListSet.listMultiplicityToString(resources) + "\n");
		ArrayList<DevelopmentCard> cards = deck.buyableCards(resources, management); // the player has not bought any development cards yet
		for (DevelopmentCard card: cards) {
			CLIUtils.showDevCard(card);
		}
		
	}
}