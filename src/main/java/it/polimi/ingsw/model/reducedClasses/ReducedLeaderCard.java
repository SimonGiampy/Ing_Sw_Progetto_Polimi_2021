package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.CardRequirement;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;
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
	private boolean discarded;
	private boolean playable;

	public ReducedLeaderCard(LeaderCard leaderCard, boolean abilitiesActivated,boolean discarded, boolean playable){
		victoryPoints=leaderCard.getVictoryPoints();
		resourceRequirements= leaderCard.getResourceRequirements();
		cardRequirements=leaderCard.getCardRequirements();
		effectsActivation= leaderCard.getEffectsActivation();
		this.abilitiesActivated=abilitiesActivated;
		this.discarded=discarded;
		this.playable=playable;
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
}
