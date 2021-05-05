package it.polimi.ingsw.xml_parsers;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.util.ListSet;
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
		ArrayList<Integer> report = parser.readReportPoints();
		ReducedFaithTrack faithTrack = new ReducedFaithTrack(new FaithTrack(tiles, report, false));
		System.out.println("Faith Track");
		cli.showFaithTrack(faithTrack);
		System.out.println("\n");
		cli.helpFaithTrack();
		System.out.println("\n");
	}
	

	@Test
	void readDevCards() {
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		int i = 1;
		for(DevelopmentCard card: devCards) {
			System.out.println("Card #" + i);
			cli.showDevCard(card);
			System.out.print("\n");
			i++;
		}
	}

	@Test
	void readLeaderCards() {
		System.out.println("Showing Leader Cards");
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		int i = 1;
		for(LeaderCard leader: leaderCards) {
			System.out.println("Card #" + i);
			cli.showLeaderCard(new ReducedLeaderCard(leader, false, false, true));
			System.out.print("\n");
			i++;
		}
		
	}

	@Test
	void parseBaseProductionFromXML() {
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		System.out.println("Base Production:");
		System.out.println("Input -> " + ListSet.listMultiplicityToString(baseProduction.getInputCopy()));
		System.out.println("Output -> " + ListSet.listMultiplicityToString(baseProduction.getOutputCopy()));
		System.out.println("Faith Output -> " + baseProduction.getFaithOutput() + "\n");
	}
}