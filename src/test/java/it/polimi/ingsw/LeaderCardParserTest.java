package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;


public class LeaderCardParserTest {
	
	/**
	 * tests the parsing of the parameters from the xml configuration file for the leader cards
	 */
	@Test
	public void parseLeaderCardsFromXML(){

		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParser parser = new XMLParser(fullPath);
		
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
	
		for(int i = 0; i < leaderCards.size(); i++ ){
			System.out.println("\nleader " +(i+1)+": "+"\nvictory points: " + leaderCards.get(i).getVictoryPoints() +
					"\nresources requirements: " + leaderCards.get(i).getResourceRequirements() + "\ndev cards requirements: "+
					leaderCards.get(i).getCardRequirements() );
			for (int j = 0; j < leaderCards.get(i).getEffectsActivation().size(); j++) {
				System.out.println("ability " + j + ": " + leaderCards.get(i).getEffectActivation(j).toString());
			}

			
		}
		System.out.println("\n\n");
	leaderCards.get(10).showLeader();

	}
	
	
	@Test
	public void parseBaseProductionFromXML() {
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParser parser = new XMLParser(fullPath);
		
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		
		System.out.println("base production:\ninput resources = " +
				baseProduction.getInputCopy().toString() + "\noutput resources = " + baseProduction.getOutputCopy().toString()
		);
	}

}