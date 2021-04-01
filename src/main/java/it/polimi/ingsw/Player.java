package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * Player class represents a unique player in the game. Handles all the instances of the classes in the game, since the player interacts with all of
 * them at different points of the gameplay
 */
public class Player {

	private final WarehouseDepot myWarehouseDepot;
	private final Strongbox myStrongbox;
	private final ResourceDeck myResourceDeck;
	private final FaithTrack myFaithTrack;
	private final CardManagement cardManager;
	
	private final Market commonMarket;
	private final DevelopmentCardsDeck commonCardsDeck;
	
	private LeaderCard[] leaderCards;
	
	int coinDiscount;
	int servantDiscount;
	int shieldDiscount;
	int stoneDiscount;

	public Player(Market market, DevelopmentCardsDeck developmentCardsDeck, WarehouseDepot warehouseDepot, Strongbox strongbox,
	              ResourceDeck resourceDeck, FaithTrack faithTrack, CardManagement cardManagement, LeaderCard[] leaderCards) {
		
		commonMarket = market;
		commonCardsDeck = developmentCardsDeck;
		this.leaderCards = leaderCards;
		
		myWarehouseDepot = warehouseDepot;
		myFaithTrack = faithTrack;
		myResourceDeck = resourceDeck;
		myStrongbox = strongbox;
		cardManager = cardManagement;
		
		coinDiscount = 0;
		servantDiscount = 0;
		shieldDiscount = 0;
		stoneDiscount = 0;

	}


	/**
	 * discards two leader cards out of the 4 given at the beginning of the game
	 * @param indexCard1 index of the first card to keep
	 * @param indexCard2 index of the second card to keep
	 */
	public void discardTwoLeaders(int indexCard1, int indexCard2){
		LeaderCard[] keptCards = new LeaderCard[];
		keptCards[0] = leaderCards[indexCard1];
		keptCards[1] = leaderCards[indexCard2];
		leaderCards = keptCards;
	}
	/**
	 * prototype function that sends the command to the market
	 *
	 * @param which must be equal to row or col
	 * @param where indicates the number of the row or the column to move
	 */
	protected void interactWithMarket(String which, int where) {
		if (which.equals("col")) {
			myResourceDeck.addResources(commonMarket.shiftCol(where));
		} else if (which.equals("row")) {
			myResourceDeck.addResources(commonMarket.shiftRow(where));
		}

		myFaithTrack.moveMarker(myResourceDeck.getFaithPoint());
		//TODO: wait for controller and movement of resources in the warehouse
	}

	protected void interactWithWarehouse() {
		//TODO: user interaction for moving the resources from the deck to the warehouse
		myWarehouseDepot.processNewMove();
	}

	protected boolean isBuyMoveAvailable(){
		myStrongbox.getContent().addAll(myWarehouseDepot.getAllResources());
		return commonCardsDeck.canBuyDevCard(gatherAllResources(), cardManager);
	}
	
	private ArrayList<Resources> gatherAllResources() {
		ArrayList<Resources> total = myStrongbox.getContent();
		total.addAll(myWarehouseDepot.getAllResources());
		return total;
	}

	protected void buyNewDevCard(int level, Colors color) {
		
		if (commonCardsDeck.isCardBuyable(level, color, gatherAllResources(), cardManager)) {

			int selectedSlot = 0;

			ArrayList <Resources> price = commonCardsDeck.getPriceDevCard(level, color);

			//TODO: ask where to put the card (which slot)
			cardManager.checkStackLevel(selectedSlot);

			price = myWarehouseDepot.payResources(price);

			if(!(price.isEmpty()))
				price = myStrongbox.payResources(price);

			cardManager.addCard(commonCardsDeck.claimCard(level, color), selectedSlot);

		}
	}
	
	/**
	 * this function tells the leader to activate its ability. Passes as value itself so that the ability knows who is the player referring to it
	 * @param whichLeader the index in the array corresponding to the leader to be activated
	 */
	public void activateLeaderCard(int whichLeader) {
		leaderCards[whichLeader].activateAbility(this);
	}

	/**
	 * Setter for the leader card's discounts on each resource
	 * @param resources arraylist of the single discounts got from the leader card
	 */
	protected void setDiscount(ArrayList<Resources> resources) {
		for (Resources res : resources) {
			switch (res) {
				case COIN -> coinDiscount++;
				case STONE -> stoneDiscount++;
				case SHIELD -> shieldDiscount++;
				case SERVANT -> servantDiscount++;

				default -> throw new IllegalStateException("Value EMPTY is not correct");
			}
		}
	}
	
	public WarehouseDepot getPlayersWarehouseDepot() {
		return myWarehouseDepot;
	}
	
	public ResourceDeck getPlayersResourceDeck() {
		return myResourceDeck;
	}
	
	public CardManagement getPlayersCardManager() {
		return cardManager;
	}
}
