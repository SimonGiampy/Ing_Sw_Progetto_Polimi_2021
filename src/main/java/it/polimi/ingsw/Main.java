package it.polimi.ingsw;

import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
<<<<<<< Updated upstream
		/*
		Market market = new Market();
		market.showMarket();
		market.shiftRow(2);
		market.showMarket();
		market.shiftCol(3);
		market.showMarket();
		
		System.out.println(ANSI_RESET);
		*/
		
		System.out.println("\uD83D\uDE8C"); // bus emoji test
		
		WarehouseDepot depot = new WarehouseDepot();
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.STONE);
		list.add(Resources.COIN);
		depot.addResource(list);
		
		Scanner scanner = new Scanner(System.in);
		String in = "no";
		do {
			if (depot.processNewMove()) {
				depot.showIncomingDeck();
				depot.showDepot();
				System.out.println("Do you want to confirm (yes / no)? Resources in the deck will be automatically discarded");
				in = scanner.next();
			} else {
				System.out.println("write new move");
			}
		} while (!in.equals("yes") && !in.equals("y"));
		
		
		//depot.insertResource("stone"); // servants move up and 3 stones on the bottom
		//depot.showWarehouse();
=======

		ArrayList<Marbles> list;
		list = new ArrayList<Marbles>();

		ResourceDeck deck;
		deck = new ResourceDeck();
		ArrayList<Resources> resourceList;
		resourceList = new ArrayList<Resources>();
		list.add(Marbles.BLUE);
		list.add(Marbles.YELLOW);
		list.add(Marbles.BLUE);
		list.add(Marbles.GREY);


		deck.addResources(list);
		resourceList = deck.getResourceList();
		for(Resources r: resourceList)
			System.out.println(r + "\n");
>>>>>>> Stashed changes
	}
}
