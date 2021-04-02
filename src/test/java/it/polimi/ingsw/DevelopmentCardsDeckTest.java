package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DevelopmentCardsDeckTest {


	@Test
	public void instantiateDeck() {

		String fileName = "game_configuration_complete.xml";
		XMLParserDraft parser = new XMLParserDraft();
		ArrayList<DevelopmentCard> arrayCards = parser
				.readDevCards(fileName);
		DevelopmentCardsDeck deck = new DevelopmentCardsDeck(createCommonCardsDeck(arrayCards));

		for(int i = 0; i < arrayCards.size(); i++){
			System.out.println("card "+(i+1)+"\nlevel: "+arrayCards.get(i).getLevel()+"\ncolor: "+arrayCards.get(i).getColor()+
					"\nrequirements: "+arrayCards.get(i).getResourcesRequirement()+ "\nvictory points: "+ arrayCards.get(i).getVictoryPoints()
					+"\ninput: " +arrayCards.get(i).getProductionInput()+"\noutput: " +arrayCards.get(i).produce() +
					"\nfaith output: " +arrayCards.get(i).returnFaithPoints()+ "\n");
		}


	}

	//method stolen to GameMechanics
	public ArrayList<DevelopmentCard>[][] createCommonCardsDeck(ArrayList<DevelopmentCard> developmentCards) {
		//TODO: create cards with parameters read from the XML configuration file and instantiate all the DevCards
		ArrayList<DevelopmentCard>[][] matrixDeck = new ArrayList[3][4]; //fixed matrix size
		//NOTE: the warning above cannot be fixed in any way but the code is absolutely correct
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				matrixDeck[i][j] = new ArrayList<DevelopmentCard>();
			}
		}

		assert developmentCards.size() % 12 == 0; // in order to create a grid
		int level, color;
		for (DevelopmentCard card : developmentCards) {
			level = card.getLevel() - 1;
			color = card.getColor().getColorNumber();
			matrixDeck[level][color].add(card);
		}

		return matrixDeck;
	}
}