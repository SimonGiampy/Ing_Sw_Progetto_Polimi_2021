package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.parser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class CardProductionsManagementTest {
	
	private CardProductionsManagement cardProductionsManagement;
	private Strongbox strongbox;
	private DevelopmentCard developmentCard1;
	private DevelopmentCard developmentCard2;
	private DevelopmentCard developmentCard3;
	
	/**
	 * called before each method: instantiation of cards and the card manager
	 */
	@BeforeEach
	void setUp() {
		String fileName = "game_configuration_standard.xml";
		XMLParser parser = new XMLParser(fileName);
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		strongbox = new Strongbox();
		cardProductionsManagement = new CardProductionsManagement(strongbox, new WarehouseDepot(), baseProduction);
		
		developmentCard1 = new DevelopmentCard(1, Colors.GREEN,0,new ArrayList<>(), baseProduction,1);
		developmentCard2 = new DevelopmentCard(1,Colors.YELLOW,3, new ArrayList<>(), baseProduction,1);
		developmentCard3 = new DevelopmentCard(2,Colors.YELLOW,5, new ArrayList<>(), baseProduction,1);
		
	}
	
	/**
	 * Multiple tests for addCard & checkStackLevel & numberOfCards & totalVictoryPoints
	 */
	@Test
	public void checkStackLevel(){
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		
		assertEquals(1,cardProductionsManagement.checkStackLevel(1));
		assertEquals(2,cardProductionsManagement.checkStackLevel(2));
		assertEquals(3,cardProductionsManagement.numberOfCards());
		assertEquals(8,cardProductionsManagement.totalVictoryPoints());
	}
	
	/**
	 * test for getPlayerCardsRequirements after adding 3 cards
	 */
	@Test
	public void getPlayerCardsRequirements(){
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		
		ArrayList<CardRequirement> result = new ArrayList<>();
		result.add(new CardRequirement(Colors.GREEN,1));
		result.add(new CardRequirement(Colors.YELLOW,1));
		result.add(new CardRequirement(Colors.YELLOW,2));
		ArrayList<CardRequirement> test = cardProductionsManagement.getPlayerCardsRequirements();
		assertEquals(result, test);

	}
	
	/**
	 * Produce 3 productions all together with different sets of resources in the strongbox
	 */
	@Test
	public void isSelectedProductionAvailable_1() {
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		
		ArrayList<Productions> inputTest = new ArrayList<>();
		inputTest.add(Productions.STACK_1_CARD_PRODUCTION);
		inputTest.add(Productions.STACK_2_CARD_PRODUCTION);
		inputTest.add(Productions.BASE_PRODUCTION);
		
		ArrayList<Resources> resources = new ArrayList<>(); // strongbox resources
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		// not enough coins to activate all 3 productions
		assertFalse(cardProductionsManagement.isSelectedProductionAvailable(inputTest));
		
		//with more coins the productions becomes available
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		assertTrue(cardProductionsManagement.isSelectedProductionAvailable(inputTest));
	}
	
	
	/**
	 * Productions defined above are available in pairs
	 */
	@Test
	public void isSelectedProductionAvailable_3() {
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		ArrayList<Productions> inputTest=new ArrayList<>();
		inputTest.add(Productions.STACK_1_CARD_PRODUCTION);
		inputTest.add(Productions.BASE_PRODUCTION);
		assertTrue(cardProductionsManagement.isSelectedProductionAvailable(inputTest)); // can activate both productions
	}
	
	/**
	 * Adds 2 leader cards with free choices in input and output. Then proceeds to check multiple things.
	 * At the end, it takes the resources from the player (which has only resources in their strongbox) and uses them
	 * to activate the productions and retrieve the resources and faith points
	 */
	@Test
	void variousChecksWith2LeaderProductions() {
		//first leader card
		ArrayList<Resources> input1 = new ArrayList<>(); // input production resources
		input1.add(Resources.COIN);
		input1.add(Resources.STONE);
		input1.add(Resources.FREE_CHOICE);
		ArrayList<Resources> output1 = new ArrayList<>(); // output production resources
		output1.add(Resources.COIN);
		output1.add(Resources.COIN);
		output1.add(Resources.COIN);
		output1.add(Resources.COIN);
		// creates a new leader card requirements description with the list of resources above
		cardProductionsManagement.addLeaderCard(1, input1, output1, 2);
		
		
		ArrayList<Resources> inputCopy = new ArrayList<>(); // just for checking another method
		inputCopy.add(Resources.COIN);
		inputCopy.add(Resources.STONE);
		inputCopy.add(Resources.FREE_CHOICE);
		assertEquals(inputCopy, cardProductionsManagement.getProductionInput(Productions.LEADER_CARD_1_PRODUCTION));
		
		
		//stores resources in the strongbox
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
		strongbox.storeResources(resources);
		
		//second leader card
		ArrayList<Resources> input2 = new ArrayList<>(); // input production resources
		input2.add(Resources.SHIELD);
		input2.add(Resources.SHIELD);
		input2.add(Resources.FREE_CHOICE);
		input2.add(Resources.FREE_CHOICE);
		ArrayList<Resources> output2 = new ArrayList<>(); // output production resources
		output2.add(Resources.COIN);
		output2.add(Resources.COIN);
		output2.add(Resources.SERVANT);
		output2.add(Resources.SERVANT);
		output2.add(Resources.FREE_CHOICE);
		cardProductionsManagement.addLeaderCard(2, input2, output2, 3);
		
		// confirms that there is at least one production available
		assertTrue(cardProductionsManagement.isAtLeastOneProductionAvailable());
		
		// checks the list of available productions
		ArrayList<Productions> availableProductions = new ArrayList<>();
		availableProductions.add(Productions.BASE_PRODUCTION);
		availableProductions.add(Productions.LEADER_CARD_1_PRODUCTION);
		availableProductions.add(Productions.LEADER_CARD_2_PRODUCTION);
		assertEquals(availableProductions, cardProductionsManagement.availableProductions());
		
		// checks the number of free choices in these productions, both input and output choices
		ArrayList<Productions> productions = new ArrayList<>();
		productions.add(Productions.LEADER_CARD_1_PRODUCTION);
		productions.add(Productions.LEADER_CARD_2_PRODUCTION);
		assertEquals(3, cardProductionsManagement.numberOfFreeChoicesInInputProductions(productions));
		assertEquals(1, cardProductionsManagement.numberOfFreeChoicesInOutputProductions(productions));
		
		
		int[] freeChoicesInput = new int[]{2, 1, 0, 0}; //[#COIN, #SERVANT, #SHIELD, #STONE]
		assertTrue(cardProductionsManagement.checkFreeInput(productions, freeChoicesInput));
		
		cardProductionsManagement.takeSelectedResources(productions, freeChoicesInput);
		
		int[] freeChoicesOutput = new int[]{0, 0, 0, 1}; //[#COIN, #SERVANT, #SHIELD, #STONE]
		int faith = cardProductionsManagement.activateSelectedProduction(productions, freeChoicesOutput);
		assertEquals(5, faith);
		
		// checks the correctness of the productions output
		ArrayList<Resources> result = new ArrayList<>();
		result.add(Resources.STONE);
		result.add(Resources.STONE);
		result.add(Resources.STONE);
		result.add(Resources.STONE);
		result.add(Resources.SHIELD);
		result.add(Resources.COIN);
		result.add(Resources.COIN);
		result.add(Resources.COIN);
		result.add(Resources.COIN);
		result.add(Resources.COIN);
		result.add(Resources.COIN);
		result.add(Resources.SERVANT);
		result.add(Resources.SERVANT);
		
		// if the result is correct, then the difference of the resulting set and the expected result is an emtpy set of resources
		ArrayList<Resources> difference = ListSet.removeSubSet(strongbox.getContent(), result);
		assertTrue(difference.isEmpty());
	}
	
	
	@Test
	void selectedInput() {
		ArrayList<Productions> productions = new ArrayList<>();
		productions.add(Productions.LEADER_CARD_1_PRODUCTION);
		productions.add(Productions.LEADER_CARD_2_PRODUCTION);
		cardProductionsManagement.setSelectedInput(productions);
		assertEquals(productions, cardProductionsManagement.getSelectedInput());
	}
	
	@Test
	void getInputResources() {
		int[] freeChoicesInput = new int[]{2, 1, 0, 0}; //[#COIN, #SERVANT, #SHIELD, #STONE]
		cardProductionsManagement.setInputResources(freeChoicesInput);
		assertArrayEquals(freeChoicesInput, cardProductionsManagement.getInputResources());
	}
	
}