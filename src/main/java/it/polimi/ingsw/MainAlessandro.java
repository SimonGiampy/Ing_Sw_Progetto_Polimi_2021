package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;
import it.polimi.ingsw.abilities.DiscountAbility;

import java.util.ArrayList;

public class MainAlessandro {


	public static void main(String[] args) {
		CardRequirement cardRequirement1= new CardRequirement(Colors.YELLOW,0);
		CardRequirement cardRequirement2= new CardRequirement(Colors.PURPLE,0);
		CardRequirement cardRequirement3= new CardRequirement(Colors.BLUE,0);
		CardRequirement cardRequirement4= new CardRequirement(Colors.GREEN,0);
		CardRequirement cardRequirement5= new CardRequirement(Colors.YELLOW,1);
		CardRequirement cardRequirement6= new CardRequirement(Colors.GREEN,1);
		CardRequirement cardRequirement7= new CardRequirement(Colors.BLUE,1);
		ArrayList<CardRequirement> leaderInput=new ArrayList<>();
		ArrayList<CardRequirement> playerCards= new ArrayList<>();
		leaderInput.add(cardRequirement1);
		leaderInput.add(cardRequirement1);
		leaderInput.add(cardRequirement7);
		playerCards.add(cardRequirement5);
		playerCards.add(cardRequirement7);
		
		DiscountAbility disc = new DiscountAbility(new ArrayList<>());
		ArrayList<AbilityEffectActivation> effectActivations = new ArrayList<>();
		effectActivations.add(disc);
		LeaderCard leaderCard = new LeaderCard(0,new ArrayList<>(), leaderInput, effectActivations);
		System.out.println(leaderCard.checkCards(playerCards));

		ArrayList<Integer> prova1=new ArrayList<>();
		ArrayList<Integer> prova2=new ArrayList<>();
		prova1.add(1);
		prova1.add(1);
		prova1.add(2);
		prova2.add(3);
		prova2.add(3);
		System.out.println(prova1.containsAll(prova2));
	}
}
