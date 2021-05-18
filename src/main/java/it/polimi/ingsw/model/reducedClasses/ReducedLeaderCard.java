package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.CardRequirement;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.util.Resources;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedLeaderCard implements Serializable {
	private final int victoryPoints;
	private final ArrayList<Resources> resourceRequirements; // list of resources required for the activation
	private final ArrayList<CardRequirement> cardRequirements; // list of development card requirements
	private final ArrayList<AbilityEffectActivation> effectsActivation; // a single leader card supports multiple abilities
	private final boolean abilitiesActivated;
	private final boolean discarded;
	private final boolean playable;
	private final int idNumber;

	public ReducedLeaderCard(LeaderCard leaderCard, boolean abilitiesActivated,boolean discarded, boolean playable, int idNumber){
		victoryPoints=leaderCard.getVictoryPoints();
		resourceRequirements= leaderCard.getResourceRequirements();
		cardRequirements=leaderCard.getCardRequirements();
		effectsActivation= leaderCard.getEffectsActivation();
		this.abilitiesActivated=abilitiesActivated;
		this.discarded=discarded;
		this.playable=playable;
		this.idNumber = idNumber;
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

	public boolean isDiscarded() {
		return discarded;
	}

	public boolean isPlayable() {
		return playable;
	}

	public int getIdNumber() {
		return idNumber;
	}
}
