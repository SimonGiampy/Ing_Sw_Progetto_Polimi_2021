package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.abilities.DiscountAbility;
import it.polimi.ingsw.model.util.Resources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {
	
	CardRequirement yellow0= new CardRequirement(Colors.YELLOW,0);
	CardRequirement blue0= new CardRequirement(Colors.BLUE,0);
	CardRequirement yellow1= new CardRequirement(Colors.YELLOW,1);
	CardRequirement green1= new CardRequirement(Colors.GREEN,1);
	CardRequirement blue1= new CardRequirement(Colors.BLUE,1);
	CardRequirement blue2= new CardRequirement(Colors.BLUE,2);

	
	@Test
	void checkResources() {
		ArrayList<Resources> resources = new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.COIN);
		resources.add(Resources.STONE);
		resources.add(Resources.SERVANT);
		resources.add(Resources.SHIELD);
		
		ArrayList<Resources> requirements = new ArrayList<>();
		requirements.add(Resources.COIN);
		requirements.add(Resources.SERVANT);
		requirements.add(Resources.SHIELD);
		
		LeaderCard card = new LeaderCard(2, requirements, new ArrayList<>(), new ArrayList<>());
		assertTrue(card.checkResources(resources));
	}

	@Test
	void checkCards_1() {
		ArrayList<CardRequirement> leaderInput=new ArrayList<>();
		ArrayList<CardRequirement> playerCards= new ArrayList<>();
		leaderInput.add(yellow0);
		leaderInput.add(yellow0);
		leaderInput.add(blue0);
		playerCards.add(yellow1);
		playerCards.add(yellow1);
		playerCards.add(blue2);
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0,new ArrayList<>(), leaderInput, effectActivations);
		assertTrue(leaderCard.checkCards(playerCards));
		
	}
	@Test
	void checkCards_2() {
		ArrayList<CardRequirement> leaderInput=new ArrayList<>();
		ArrayList<CardRequirement> playerCards= new ArrayList<>();
		leaderInput.add(yellow0);
		leaderInput.add(yellow0);
		leaderInput.add(blue0);
		playerCards.add(yellow1);
		playerCards.add(green1);
		playerCards.add(blue2);
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0,new ArrayList<>(), leaderInput, effectActivations);
		assertFalse(leaderCard.checkCards(playerCards));
	}
	@Test
	void checkCards_3() {
		ArrayList<CardRequirement> leaderInput=new ArrayList<>();
		ArrayList<CardRequirement> playerCards= new ArrayList<>();
		leaderInput.add(yellow0);
		leaderInput.add(yellow0);
		leaderInput.add(blue1);
		playerCards.add(yellow1);
		playerCards.add(yellow1);
		playerCards.add(blue1);
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0,new ArrayList<>(), leaderInput, effectActivations);
		assertTrue(leaderCard.checkCards(playerCards));
	}
	@Test
	void checkCards5() {
		ArrayList<CardRequirement> leaderInput=new ArrayList<>();
		ArrayList<CardRequirement> playerCards= new ArrayList<>();
		leaderInput.add(yellow1);
		leaderInput.add(blue1);
		playerCards.add(yellow1);
		playerCards.add(yellow1);
		playerCards.add(blue1);
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0,new ArrayList<>(), leaderInput, effectActivations);
		assertTrue(leaderCard.checkCards(playerCards));
	}

}