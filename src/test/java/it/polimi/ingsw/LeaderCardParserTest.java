package it.polimi.ingsw;

import it.polimi.ingsw.abilities.*;
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
		int x = 0;
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards(fullPath);
		/*
		ArrayList<Resources> array = new ArrayList<>();
		array.add(Resources.COIN);
		AbilityEffectActivation ability = new AdditionalDepotAbility(array);

		 */

		for(int i = 0; i < leaderCards.size(); i++){
			System.out.println("leader " +(i+1)+": "+"\nvictory points: " + leaderCards.get(i).getVictoryPoints()+
					"\nresource requirements: "+leaderCards.get(i).getResourceRequirements()+ "\ncards requirements: "+
					leaderCards.get(i).getCardRequirements());

			if(leaderCards.get(i).getEffectActivation() instanceof DiscountAbility)
				x = 1;
			else if(leaderCards.get(i).getEffectActivation() instanceof AdditionalDepotAbility)
				x = 2;
			else if(leaderCards.get(i).getEffectActivation() instanceof WhiteMarbleAbility)
				x = 3;
			else if(leaderCards.get(i).getEffectActivation() instanceof ExtraProductionAbility)
				x = 4;

			switch (x){
				case 1 -> {
					System.out.println("discount: "+((DiscountAbility) leaderCards.get(i).getEffectActivation())
							.getSingleDiscounts());
				}
				case 2 -> {
					System.out.println("depot: "+((AdditionalDepotAbility) leaderCards.get(i).getEffectActivation())
							.getAbilityResource());
				}
				case 3 -> {
					System.out.println("white marble:\ninput: "+((WhiteMarbleAbility) leaderCards.get(i).getEffectActivation())
							.getWhiteMarbleNumber()+ "\noutput: " +((WhiteMarbleAbility) leaderCards.get(i).getEffectActivation())
							.getAbilityResources());
				}
				case 4 -> {
					System.out.println("extra production:\ninput: "+((ExtraProductionAbility) leaderCards.get(i).getEffectActivation())
							.getInputResources() + "\noutput: "+((ExtraProductionAbility) leaderCards.get(i).getEffectActivation())
							.getOutputResources()+"\nfaith output: " +((ExtraProductionAbility) leaderCards.get(i).getEffectActivation())
							.getFaithPointsOutput());
				}

			}
		}

	}

}