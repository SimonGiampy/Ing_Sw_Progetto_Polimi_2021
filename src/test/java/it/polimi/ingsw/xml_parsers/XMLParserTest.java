package it.polimi.ingsw.xml_parsers;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.ProductionRules;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.view.CLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLParserTest {

	private XMLParser parser;
	private CLI cli;
	@BeforeEach
	void instantiateParser(){
		String path;
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
		path = file.getAbsolutePath();
		parser = new XMLParser(path);
		cli= new CLI();
	}

	@Test
	void readTiles() {
		ArrayList<Tile> tiles = parser.readTiles();
		for(int i = 0; i < tiles.size(); i++){
			System.out.println("Tile n." + i );
			System.out.println("Inside Vatican: " + tiles.get(i).getInsideVatican());
			System.out.println("Victory points: " + tiles.get(i).getVictoryPoints());
			System.out.print("\n");
		}
	}

	@Test
	void readReportPoints() {
		ArrayList<Integer> report = parser.readReportPoints();
		for(int i = 0; i < report.size(); i++)
			System.out.println("Report n." + (i+1) + ": " + report.get(i));
	}

	@Test
	void readDevCards() {
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		for(DevelopmentCard card: devCards)
			cli.showDevCard(card);
	}

	@Test
	void readLeaderCards() {
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		for(LeaderCard leader: leaderCards)
			cli.showLeaderCard(new ReducedLeaderCard(leader, false, false, true));
	}

	@Test
	void parseBaseProductionFromXML() {
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		ArrayList<Resources> a = new ArrayList<>();
		a.add(Resources.COIN);
		a.add(Resources.STONE);
		System.out.println("Base Production:");
		System.out.println("Input -> " + baseProduction.numberOfFreeChoicesInput() + " "+ baseProduction.getInputCopy());
		System.out.println("Output -> "+ baseProduction.numberOfFreeChoicesOutput() + " " + baseProduction.getOutputCopy());
		System.out.println("Faith Output -> " + baseProduction.getFaithOutput());
		assertTrue(baseProduction.isProductionAvailable(a));
		System.out.println("Is available: " + baseProduction.isProductionAvailable(a));
	}
}