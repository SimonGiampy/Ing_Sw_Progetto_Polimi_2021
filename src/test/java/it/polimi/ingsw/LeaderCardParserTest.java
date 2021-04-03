package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParserDraft;
import org.junit.Test;

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
		XMLParserDraft parser = new XMLParserDraft();
		int x = 0;
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards(fullPath);
	
		for(int i = 0; i < leaderCards.size(); i++ ){
			System.out.println("\nleader " +(i+1)+": "+"\nvictory points: " + leaderCards.get(i).getVictoryPoints() +
					"\nresources requirements: " + leaderCards.get(i).getResourceRequirements() + "\ndev cards requirements: "+
					leaderCards.get(i).getCardRequirements() + "\n" +
					leaderCards.get(i).getEffectActivation().toString());

			
		}

	}

}