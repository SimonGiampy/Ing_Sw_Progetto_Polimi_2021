package it.polimi.ingsw;

import java.util.ArrayList;

public class LeaderCard {

	private int victoryPoints;
	private ArrayList<Resources> requirements;
	private ArrayList<CardRequirement> cardRequirements;
	private AbilityEffectActivation effectActivation;


	public LeaderCard(int victoryPoints, ArrayList<Resources> requirements, ArrayList<CardRequirement> cardRequirements,
					  AbilityEffectActivation effectActivation) {
		this.victoryPoints = victoryPoints;
		this.requirements = requirements;
		this.cardRequirements = cardRequirements;
		this.effectActivation = effectActivation;
	}

	protected boolean checkResources(){
		effectActivation.activateAbility();
	}

	protected boolean checkCards(){

	}
}
