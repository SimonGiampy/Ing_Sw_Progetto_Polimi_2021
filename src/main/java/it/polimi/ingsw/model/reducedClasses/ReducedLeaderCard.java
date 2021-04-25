package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.CardRequirement;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedLeaderCard implements Serializable {
	private final int victoryPoints;
	private final ArrayList<Resources> resourceRequirements; // list of resources required for the activation
	private final ArrayList<CardRequirement> cardRequirements; // list of development card requirements
	private final ArrayList<AbilityEffectActivation> effectsActivation; // a single leader card supports multiple abilities
	private boolean abilitiesActivated;

	public ReducedLeaderCard(LeaderCard leaderCard){
		victoryPoints=leaderCard.getVictoryPoints();
		resourceRequirements= leaderCard.getResourceRequirements();
		cardRequirements=leaderCard.getCardRequirements();
		effectsActivation= leaderCard.getEffectsActivation();
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public ArrayList<Resources> getResourceRequirements() {
		return resourceRequirements;
	}

	public ArrayList<CardRequirement> getCardRequirements() {
		return cardRequirements;
	}

	public ArrayList<AbilityEffectActivation> getEffectsActivation() {
		return effectsActivation;
	}

	public boolean isAbilitiesActivated() {
		return abilitiesActivated;
	}

	public void showLeader(){
		StringBuilder string= new StringBuilder();
		appendTopFrame(string);
		appendVictoryPoints(string);
		appendFirstLine(string);
		appendAbility(string);
		appendBottomFrame(string);
		System.out.println(string);
	}

	public int maxLength(){
		int max=12;
		int size= (int) (8+4*resourceRequirements.stream().distinct().count());
		StringBuilder s= new StringBuilder();

		if(s.append("  REQs "+ ListSet.showListMultiplicityOnConsole(resourceRequirements)).length()>resourceRequirements.stream().distinct().count()*15)
			size=size+(s.length()-8-(int)resourceRequirements.stream().distinct().count()*15);
		if(size>max)
			max=size;
		size= (int) (6+11*cardRequirements.stream().distinct().count());
		if(size>max)
			max=size;
		for (AbilityEffectActivation abilityEffectActivation : effectsActivation) {
			if (abilityEffectActivation.maxLength() > max)
				max = abilityEffectActivation.maxLength();
		}
		return max;
	}

	public void appendTopFrame(StringBuilder string){
		string.append(Unicode.TOP_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(Math.max(0, maxLength())));
		string.append(Unicode.TOP_RIGHT+"\n");
	}

	public void appendBottomFrame(StringBuilder string){
		string.append(Unicode.BOTTOM_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(Math.max(0, maxLength())));
		string.append(Unicode.BOTTOM_RIGHT+"\n");
	}

	public void appendFirstLine(StringBuilder string){
		if(resourceRequirements.size()!=0)
			string.append("  REQs "+ListSet.showListMultiplicityOnConsole(resourceRequirements)+"\n");
		if(cardRequirements.size()!=0)
			string.append("  REQs "+ListSet.showListMultiplicityOnConsole(cardRequirements)+"\n");
	}

	public void appendAbility(StringBuilder string){
		for (AbilityEffectActivation abilityEffectActivation : effectsActivation) {
			abilityEffectActivation.appendPower(string);
		}
	}

	public void appendVictoryPoints(StringBuilder string){
		string.append(Unicode.RED_BOLD+"  LEADER "+Unicode.RESET+getVictoryPoints()+Resources.VICTORY_POINTS+"\n");
	}

}
