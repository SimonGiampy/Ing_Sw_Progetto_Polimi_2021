package it.polimi.ingsw.model;

import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.parser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardsDeckTest {
	
	DevelopmentCardsDeck deck;
	CardProductionsManagement management;
	Strongbox box;
	
	@BeforeEach
	void setUp() {
		String fileName = "game_configuration_standard.xml";
		XMLParser parser = new XMLParser(fileName);
		
		deck = new DevelopmentCardsDeck(new GameMechanicsMultiPlayer(1).createCommonCardsDeck(parser.readDevCards()));
		box = new Strongbox();
		management = new CardProductionsManagement(box, new WarehouseDepot(), parser.parseBaseProductionFromXML());
	}
	
	@Test
	void getPriceDevCard() {
		assertFalse(deck.getPriceDevCard(2, Colors.BLUE).isEmpty());
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

		// all the cards of level 1 and color blue are finished, so the blue colored cards are available only for level 2 and 3
		assertEquals(1, deck.lowestCardLevelAvailable(Colors.BLUE));
		
		// since the dev cards in the selected stack are finished, the stack is empty thus the method returns null
		assertNotNull(deck.claimCard(1, Colors.BLUE));
		
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

		ArrayList<DevelopmentCard> cards = deck.buyableCards(resources, management); // the player has not bought any development cards yet
		assertFalse(cards.isEmpty());
		
	}
}