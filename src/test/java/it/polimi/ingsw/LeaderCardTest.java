package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;
import it.polimi.ingsw.abilities.DiscountAbility;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {
	CardRequirement yellow0= new CardRequirement(Colors.YELLOW,0);
	CardRequirement purple0= new CardRequirement(Colors.PURPLE,0);
	CardRequirement blue0= new CardRequirement(Colors.BLUE,0);
	CardRequirement green0= new CardRequirement(Colors.GREEN,0);
	CardRequirement yellow1= new CardRequirement(Colors.YELLOW,1);
	CardRequirement purple1= new CardRequirement(Colors.PURPLE,1);
	CardRequirement green1= new CardRequirement(Colors.GREEN,1);
	CardRequirement blue1= new CardRequirement(Colors.BLUE,1);
	CardRequirement yellow2= new CardRequirement(Colors.YELLOW,2);
	CardRequirement purple2= new CardRequirement(Colors.PURPLE,2);
	CardRequirement green2= new CardRequirement(Colors.GREEN,2);
	CardRequirement blue2= new CardRequirement(Colors.BLUE,2);
	CardRequirement yellow3= new CardRequirement(Colors.YELLOW,3);
	CardRequirement purple3= new CardRequirement(Colors.PURPLE,3);
	CardRequirement green3= new CardRequirement(Colors.GREEN,3);
	CardRequirement blue3= new CardRequirement(Colors.BLUE,3);

	@Test
	void checkResources() {
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