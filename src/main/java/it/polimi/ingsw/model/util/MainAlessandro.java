package it.polimi.ingsw.model.util;

import it.polimi.ingsw.model.CardRequirement;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Strongbox;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.abilities.DiscountAbility;

import java.util.ArrayList;

public class MainAlessandro {


	public static void main(String[] args) {
		CardRequirement yellow0 = new CardRequirement(Colors.YELLOW, 0);
		CardRequirement purple0 = new CardRequirement(Colors.PURPLE, 0);
		CardRequirement blue0 = new CardRequirement(Colors.BLUE, 0);
		CardRequirement green0 = new CardRequirement(Colors.GREEN, 0);
		CardRequirement yellow1 = new CardRequirement(Colors.YELLOW, 1);
		CardRequirement purple1 = new CardRequirement(Colors.PURPLE, 1);
		CardRequirement green1 = new CardRequirement(Colors.GREEN, 1);
		CardRequirement blue1 = new CardRequirement(Colors.BLUE, 1);
		CardRequirement yellow2 = new CardRequirement(Colors.YELLOW, 2);
		CardRequirement purple2 = new CardRequirement(Colors.PURPLE, 2);
		CardRequirement green2 = new CardRequirement(Colors.GREEN, 2);
		CardRequirement blue2 = new CardRequirement(Colors.BLUE, 2);
		CardRequirement yellow3 = new CardRequirement(Colors.YELLOW, 3);
		CardRequirement purple3 = new CardRequirement(Colors.PURPLE, 3);
		CardRequirement green3 = new CardRequirement(Colors.GREEN, 3);
		CardRequirement blue3 = new CardRequirement(Colors.BLUE, 3);
		ArrayList<CardRequirement> leaderInput = new ArrayList<>();
		ArrayList<CardRequirement> playerCards = new ArrayList<>();
		leaderInput.add(blue3);
		playerCards.add(green1);
		playerCards.add(green2);
		playerCards.add(blue3);
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0, new ArrayList<>(), leaderInput, effectActivations);

		Strongbox box = new Strongbox();
		ArrayList<Resources> prova = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			prova.add(Resources.COIN);
		}
		prova.add(Resources.COIN);
		prova.add(Resources.SHIELD);
		prova.add(Resources.SHIELD);
		prova.add(Resources.STONE);
		prova.add(Resources.SERVANT);
		box.storeResources(prova);
		box.showStrongbox();
		System.out.println(ListSet.showListMultiplicityOnConsole(prova));
		System.out.println(ListSet.showListMultiplicityOnConsole(prova).length());
		String s= "CIAO";
		System.out.println(s.length());
	}
}