package it.polimi.ingsw;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	private ArrayList<LeaderCard> leaderCards;
	
	int coinDiscount;
	int servantDiscount;
	int shieldDiscount;
	int stoneDiscount;

	public Player(Market market, DevelopmentCardsDeck developmentCardsDeck, WarehouseDepot warehouseDepot, Strongbox strongbox,
	              ResourceDeck resourceDeck, FaithTrack faithTrack, CardManagement cardManagement, LeaderCard[] leaderCards) {
		
		commonMarket = market;
		commonCardsDeck = developmentCardsDeck;

		this.leaderCards = (ArrayList<LeaderCard>) Arrays.asList(leaderCards);
		
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
	 * @param indexCard1 index of the first card to discard
	 * @param indexCard2 index of the second card to discard
	 */
	public void discardTwoLeaders(int indexCard1, int indexCard2){
		leaderCards.remove(indexCard1);
		leaderCards.remove(indexCard2);
	}

	/**
	 * discards one of the two leader cards owned by the player and give him one faith point
	 * @param indexCard index of the leader card to discard
	 * @return true if the player triggers one report, gameMechanics will have to call the checks on the report
	 */
	public boolean discardLeaderCard(int indexCard){
		leaderCards.remove(indexCard);
		return myFaithTrack.moveMarker(1);
	}
	/**
	 * prototype function that sends the command to the market
	 *
	 * @param which must be equal to row or col
	 * @param where indicates the number of the row or the column to move
	 * @return true if the player triggers one vatican report
	 */
	protected boolean interactWithMarket(String which, int where) {
		if (which.equals("col")) {
			myResourceDeck.addResources(commonMarket.shiftCol(where));
		} else if (which.equals("row")) {
			myResourceDeck.addResources(commonMarket.shiftRow(where));
		}

		return myFaithTrack.moveMarker(myResourceDeck.getFaithPoint());
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

	/**
	 * buy a dev card from the commonDeck and pay the price
	 * @param level level of the card
	 * @param color color fo the card
	 * @param selectedSlot number of the slot where the player wants to put the card
	 */
	protected void buyNewDevCard(int level, Colors color, int selectedSlot) {

		//Check if the player has enough resources and at least one eligible slot for the card
		if (commonCardsDeck.isCardBuyable(level, color, gatherAllResources(), cardManager)) {

			//Get the price
			ArrayList <Resources> price = commonCardsDeck.getPriceDevCard(level, color);

			//Check that the slot is an eligible one
			if(cardManager.checkStackLevel(selectedSlot) == level - 1) {

				//Apply the discounts of the leader cards
				for(int i = 0; i < coinDiscount; i++)
					if(!price.remove(Resources.COIN))
						break;
				for(int i = 0; i < servantDiscount; i++)
					if(!price.remove(Resources.COIN))
						break;
				for(int i = 0; i < stoneDiscount; i++)
					if(!price.remove(Resources.COIN))
						break;

				for(int i = 0; i < shieldDiscount; i++)
					if(!price.remove(Resources.COIN))
						break;

				//Pay with what there's in the depot or in the additional depots
				price = myWarehouseDepot.payResources(price);
				//Pay the remaining resources with the ones in the strongbox
				if (!(price.isEmpty()))
					myStrongbox.retrieveResources(price);
				//Add card the slot
				cardManager.addCard(commonCardsDeck.claimCard(level, color), selectedSlot);

			} else{
				throw new InvalidParameterException("slot not available");
			}

		}
	}
	
	/**
	 * this function tells the leader to activate its ability. Passes as value itself so that the ability knows who is the player referring to it
	 * @param whichLeader the index in the array corresponding to the leader to be activated
	 */
	public void activateLeaderCard(int whichLeader) {
		leaderCards.get(whichLeader).activateAbility(this);
		leaderCards.remove(whichLeader);
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
