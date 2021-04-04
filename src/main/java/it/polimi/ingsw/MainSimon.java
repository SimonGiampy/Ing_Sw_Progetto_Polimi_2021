package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Scanner;

public class MainSimon {
	
	public static void main(String[] args) {
		
		System.out.println("\uD83D\uDE8C"); // bus emoji test
		
		
		ArrayList<Resources> subList = new ArrayList<>();
		subList.add(Resources.STONE);
		subList.add(Resources.COIN);
		subList.add(Resources.STONE);
		subList.add(Resources.COIN);
		subList.add(Resources.SERVANT);
		
		ArrayList<Resources> set = new ArrayList<>();
		set.add(Resources.STONE);
		set.add(Resources.COIN);
		set.add(Resources.SHIELD);
		set.add(Resources.SERVANT);
		set.add(Resources.COIN);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		
		System.out.println("check: = " + ListSet.subset(subList, set));
		set = ListSet.removeSubSet(set, subList);
		System.out.println("remainder = " + set.toString());
		
		
		//Player player = new Player(depot);
		
		/*
		Scanner scanner = new Scanner(System.in);
		String in = "no";
		do {
			if (player.processNewMove()) {
				depot.showIncomingDeck();
				depot.showDepot();
				System.out.println("Do you want to confirm (yes / no)? Resources in the deck will be automatically discarded");
				in = scanner.next();
			} else {
				System.out.println("write new move");
			}
		} while (!in.equals("yes") && !in.equals("y"));
		*/
		
	}
}
