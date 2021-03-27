package it.polimi.ingsw;

import java.util.ArrayList;

public class MainMichele {
	
	public static void main(String[] args) {
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
	}
}
