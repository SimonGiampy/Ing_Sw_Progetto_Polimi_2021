package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.CLIUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLParserTest {

	private XMLParser parser;
	private CLI cli;
	
	@BeforeEach
	void instantiateParser(){
		String path;
		String fileName = "game_configuration_standard.xml";
		parser = new XMLParser(fileName);
		cli= new CLI();
	}

	@Test
	void readTiles() {
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ReducedFaithTrack faithTrack = new ReducedFaithTrack(new FaithTrack(tiles, report, false));
		System.out.println("Faith Track");
		cli.showFaithTrack("test", faithTrack);
		System.out.println("\n");
		CLIUtils.helpFaithTrack();
		System.out.println("\n");
	}
	

	@Test
	void readDevCards() {
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		int i = 1;
		for(DevelopmentCard card: devCards) {
			System.out.println("Card #" + i);
			CLIUtils.showDevCard(card);
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
			CLIUtils.showLeaderCard(new ReducedLeaderCard(leader, false, false, true, leader.getIdNumber()));
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