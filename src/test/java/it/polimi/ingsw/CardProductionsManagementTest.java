package it.polimi.ingsw;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

public class CardProductionsManagementTest {
	String fileName = "game_configuration_complete.xml";
	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource(fileName).getFile());
	String fullPath = file.getAbsolutePath();
	XMLParser parser = new XMLParser(fullPath);
	ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
	ProductionRules baseProduction = parser.parseBaseProductionFromXML();
	Strongbox strongbox=new Strongbox();
	CardProductionsManagement cardProductionsManagement = new CardProductionsManagement(strongbox,new WarehouseDepot(),baseProduction);
	ArrayList<ArrayList<Resources>> cards_Input= new ArrayList<>();
	ArrayList<ArrayList<Resources>> cards_Output= new ArrayList<>();

	ArrayList<Resources> cardInput_1= new ArrayList<>();
	ArrayList<Resources> cardInput_2= new ArrayList<>();
	ArrayList<Resources> cardInput_3= new ArrayList<>();
	ArrayList<Resources> cardOutput_1= new ArrayList<>();
	ArrayList<Resources> cardOutput_2= new ArrayList<>();
	ArrayList<Resources> cardOutput_3= new ArrayList<>();
	ArrayList<Resources> cardInput_4= new ArrayList<>();
	ArrayList<Resources> cardOutput_4= new ArrayList<>();

	DevelopmentCard developmentCard1= new DevelopmentCard(1, Colors.GREEN,0,new ArrayList<>(),baseProduction);
	DevelopmentCard developmentCard2= new DevelopmentCard(1,Colors.YELLOW,3, new ArrayList<>(),baseProduction);
	DevelopmentCard developmentCard3= new DevelopmentCard(2,Colors.YELLOW,5, new ArrayList<>(),baseProduction);
	@Test // test for addCard and checkStackLevel && numberOfCards && totalVictoryPoints
	public void checkStackLevel(){
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		assertEquals(1,cardProductionsManagement.checkStackLevel(1));
		assertEquals(2,cardProductionsManagement.checkStackLevel(2));
		assertEquals(3,cardProductionsManagement.numberOfCards());
		assertEquals(8,cardProductionsManagement.totalVictoryPoints());
	}

	@Test // test for getPlayerCardsRequirements
	public void getPlayerCardsRequirements(){
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		ArrayList<CardRequirement> cardRequirementsTest= new ArrayList<>();
		cardRequirementsTest.add(new CardRequirement(Colors.GREEN,1));
		cardRequirementsTest.add(new CardRequirement(Colors.YELLOW,1));
		cardRequirementsTest.add(new CardRequirement(Colors.YELLOW,2));
		ArrayList<CardRequirement> test= cardProductionsManagement.getPlayerCardsRequirements();
		assertEquals(test.toString(),cardRequirementsTest.toString());
	}

	@Test // test for isSelectedProductionAvailable Active 1,2,4 Available 1,2,4 but individually only
	public void isSelectedProductionAvailable_1() throws InvalidUserRequestException {
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(1);
		inputTest.add(2);
		inputTest.add(4);
		assertFalse(cardProductionsManagement.isSelectedProductionAvailable(inputTest));
	}

	@Test // test for isSelectedProductionAvailable Active [1,2,4] Available [1,2,4]
	public void isSelectedProductionAvailable_2() throws InvalidUserRequestException {
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(1);
		inputTest.add(2);
		inputTest.add(4);
		assertTrue(cardProductionsManagement.isSelectedProductionAvailable(inputTest));
	}

	@Test // test for isSelectedProductionAvailable Active [1,2,4] Available in pairs
	public void isSelectedProductionAvailable_3() throws InvalidUserRequestException {
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		cardProductionsManagement.addCard(developmentCard3,2);
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		strongbox.storeResources(resources);
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(1);
		inputTest.add(4);
		assertTrue(cardProductionsManagement.isSelectedProductionAvailable(inputTest));
	}

	@Test // generic test 1 && 2 not active
	public void checkPlayerInput() {
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(1);
		inputTest.add(2);
		boolean test= cardProductionsManagement.checkPlayerInput(inputTest);
		assertFalse(test);
	}

	@Test // generic test 4 always active (not necessarily available)
	public void checkPlayerInput_2() {
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(4);
		boolean test= cardProductionsManagement.checkPlayerInput(inputTest);
		assertTrue(test);
	}

	@Test // test for distinct input
	public void checkPlayerInput_3(){
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(2);
		inputTest.add(2);
		boolean test= cardProductionsManagement.checkPlayerInput(inputTest);
		assertFalse(test);
	}

	@Test // test input under 1
	public void checkPlayerInput_4(){
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(-1);
		inputTest.add(2);
		boolean test= cardProductionsManagement.checkPlayerInput(inputTest);
		assertFalse(test);
	}

	@Test // test input under over 6
	public void checkPlayerInput_5(){
		ArrayList<Integer> inputTest=new ArrayList<>();
		inputTest.add(7);
		inputTest.add(2);
		boolean test= cardProductionsManagement.checkPlayerInput(inputTest);
		assertFalse(test);
	}

	@Test //
	public void activateSelectedProduction(){
		cardProductionsManagement.addCard(developmentCard1,1);
		cardProductionsManagement.addCard(developmentCard2,2);
		ArrayList<Integer> inputTest=new ArrayList<>();
	}



}