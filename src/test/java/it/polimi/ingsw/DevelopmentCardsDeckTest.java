package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParserDraft;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class DevelopmentCardsDeckTest {
	
	
	/**
	 * development cards deck instantiation with the parameters read from the XML configuration file
	 */
	@Test
	public void instantiateDeck() {

		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		//System.out.println(fullPath);
		
		XMLParserDraft parser = new XMLParserDraft();
		ArrayList<DevelopmentCard> arrayCards = parser.readDevCards(fullPath);
		GameMechanicsMultiPlayer mec = new GameMechanicsMultiPlayer(1);
		DevelopmentCardsDeck deck = new DevelopmentCardsDeck(mec.createCommonCardsDeck(arrayCards));

		for(int i = 0; i < arrayCards.size(); i++){
			System.out.println("card "+(i+1)+"\nlevel: "+arrayCards.get(i).getLevel()+"\ncolor: "+arrayCards.get(i).getColor()+
					"\nrequirements: "+arrayCards.get(i).getResourcesRequirement()+ "\nvictory points: "+ arrayCards.get(i).getVictoryPoints()
					+"\ninput: " +arrayCards.get(i).getProductionInput()+"\noutput: " +arrayCards.get(i).produce() +
					"\nfaith output: " +arrayCards.get(i).returnFaithPoints()+ "\n");
		}


	}
	
}