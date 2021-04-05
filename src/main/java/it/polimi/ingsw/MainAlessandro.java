package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;
import it.polimi.ingsw.abilities.DiscountAbility;

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
		System.out.println(leaderCard.checkCards(playerCards));

		String chain = "1,2,3,4,5";  //This is your String
		ArrayList<Integer> list = new ArrayList<Integer>();  //This is the ArrayList where you want      to put the String
		String[] array = chain.split(",");  //Split the previous String for separate by commas
		for (String s : array) {  //Iterate over the previous array for put each element on the ArrayList like Integers
			list.add(Integer.parseInt(s));
		}
		list.remove(1);
		System.out.println(list);

		int[] inputResources = new int[]{1, 1, 1, 1};
		ArrayList<Resources> selectedProduction = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < inputResources[j]; j++) {
				selectedProduction.add(Resources.values()[i]);
			}

		}
		System.out.println(selectedProduction);
	}
}