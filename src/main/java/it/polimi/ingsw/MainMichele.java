package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParserDraft;

public class MainMichele {
	
	public static void main(String[] args) {

		XMLParserDraft parser = new XMLParserDraft();

		parser.readTiles("tiles template.xml");
		

/*
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles;
		marbles = new Marbles[]{Marbles.BLUE, Marbles.YELLOW, Marbles.WHITE, Marbles.GREY, Marbles.WHITE, Marbles.WHITE};
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.COIN);
		fromWhiteMarble2.add(Resources.SERVANT);
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);


		deck.setFromWhiteMarble(fromWhiteMarble1, 2);
		//deck.setFromWhiteMarble(fromWhiteMarble2, 2);

		deck.addResources(marbles, 0,1);

		ArrayList<Resources> resourceList = deck.getResourceList();
		for(Resources r: resourceList)
			System.out.println(r);

 */
	}

}
