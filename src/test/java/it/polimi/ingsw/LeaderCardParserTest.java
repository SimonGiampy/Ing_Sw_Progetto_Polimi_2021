package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParserDraft;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderCardParserTest {

	@Test
	public void instantiateLeaderCards(){

		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParserDraft parser = new XMLParserDraft();

		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards(fullPath);

		for(int i = 0; i < leaderCards.size(); i++){
			System.out.println("leader " +(i+1)+": "+"\nvictory points: " + leaderCards.get(i).getVictoryPoints()+
					"\nresource requirements: "+leaderCards.get(i).getResourceRequirements()+ "\ncards requirements: "+
					leaderCards.get(i).getCardRequirements() + "\npower: "+ leaderCards.get(i).getEffectActivation());
		}
	}

}