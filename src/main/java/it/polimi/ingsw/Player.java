package it.polimi.ingsw;

/**
 * Player class represent a single player in the game. Handles all the instances of the classes in the game, since the player interacts with all of
 * them
 */
public class Player {
	
	WarehouseDepot personalDepot;
	Strongbox personalStrongbox;
	ResourceDeck deck;
	Market commonMarket;
	DevelopmentCardsDeck commonCardsDeck;
	FaithTrack faithTrack;
	CardManagement cardManagement;

	
	public Player(Market market, DevelopmentCardsDeck developmentCardsDeck) {
		personalDepot = new WarehouseDepot();
		deck = new ResourceDeck(personalDepot);
		commonMarket = market;
		commonCardsDeck = developmentCardsDeck;
		personalStrongbox = new Strongbox();
		
		faithTrack = new FaithTrack();
		cardManagement = new CardManagement();
	}
	
	/**
	 * prototype function that sends the command to the market
	 * @param which must be equal to row or col
	 * @param where indicates the number of the row or the column to move
	 */
	protected void interactWithMarket(String which, int where) {
		if (which.equals("col")) {
			deck.addResources(commonMarket.shiftCol(where));
		} else if (which.equals("row")) {
			deck.addResources(commonMarket.shiftRow(where));
		}
		
		faithTrack.moveMarker(deck.getFaithPoint());
		//TODO: wait for controller and movement of resources in the warehouse
	}
	
	protected void interactWithWarehouse() {
		//TODO: user interaction for moving the resources from the deck to the warehouse
		personalDepot.processNewMove();
	}
	
	protected void buyNewDevCard() {
	
	
	}
	
	

}
