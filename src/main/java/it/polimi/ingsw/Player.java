package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidDevCardSlotException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

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
	private boolean activeLeader1;
	private boolean activeLeader2;
	
	int coinDiscount;
	int servantDiscount;
	int shieldDiscount;
	int stoneDiscount;
	
	/**
	 * constructor for the Player
	 * @param market common market shared by all the players in the game
	 * @param developmentCardsDeck the deck of development cards shared by all the players
	 * @param warehouseDepot the player's Warehouse depot and the additional depots
	 * @param strongbox the player's Strongbox
	 * @param resourceDeck the player's resource deck instance
	 * @param faithTrack player's personal faith track (equal to all the other players)
	 * @param cardManagement card manager for all the cards
	 * @param leaderCards the 4 leader cards to be assigned to the player
	 */
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
		
		activeLeader1 = false;
		activeLeader2 = false;

	}


	/**
	 * choose two leader cards out of the 4 given at the beginning of the game
	 * @param indexCard1 index of the first card to choose
	 * @param indexCard2 index of the second card to choose
	 */
	public void chooseTwoLeaders(int indexCard1, int indexCard2){
		LeaderCard[] temp = new LeaderCard[2];
		temp[0] = leaderCards[indexCard1];
		temp[1] = leaderCards[indexCard2];
		leaderCards = temp;
		
		activeLeader1 = true;
		activeLeader2 = false;
	}

	/**
	 * discards one of the two leader cards owned by the player and give him one faith point
	 * @param indexCard index of the leader card to discard
	 * @return true if the player triggers one report, gameMechanics will have to call the checks on the report
	 */
	public boolean discardLeaderCard(int indexCard) {
		if (indexCard == 1) activeLeader1 = false;
		else activeLeader2 = false;
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
		try {
			boolean ok = processNewMove();
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
		}
	}

	protected boolean isBuyMoveAvailable(){
		myStrongbox.getContent().addAll(myWarehouseDepot.gatherAllResources());
		return commonCardsDeck.canBuyDevCard(gatherAllResources(), cardManager);
	}
	
	private ArrayList<Resources> gatherAllResources() {
		ArrayList<Resources> total = myStrongbox.getContent();
		total.addAll(myWarehouseDepot.gatherAllResources());
		return total;
	}

	/**
	 * buy a dev card from the commonDeck and pay the price
	 * @param level level of the card
	 * @param color color fo the card
	 * @param selectedSlot number of the slot where the player wants to put the card
	 */
	protected void buyNewDevCard(int level, Colors color, int selectedSlot) throws InvalidDevCardSlotException {

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
					if(!price.remove(Resources.SERVANT))
						break;
				for(int i = 0; i < stoneDiscount; i++)
					if(!price.remove(Resources.STONE))
						break;
				for(int i = 0; i < shieldDiscount; i++)
					if(!price.remove(Resources.SHIELD))
						break;

				//Pay with what there's in the depot or in the additional depots
				price = myWarehouseDepot.payResources(price);
				//Pay the remaining resources with the ones in the strongbox
				if (!(price.isEmpty()))
					myStrongbox.retrieveResources(price);
				//Add card the slot
				cardManager.addCard(commonCardsDeck.claimCard(level, color), selectedSlot);

			} else{
				throw new InvalidDevCardSlotException("selected slot is not available");
			}

		}
	}
	
	/**
	 * this function tells the leader to activate its ability. Passes as value itself so that the ability knows who is the player referring to it
	 * @param whichLeader the index in the array corresponding to the leader to be activated (0 or 1)
	 */
	public void activateLeaderCard(int whichLeader) {
		//passes this as object parameter so that the ability know to which player to refer to for the activation
		leaderCards[whichLeader].activateAbility(this);
	}

	/**
	 * Setter for the leader card's discounts on each resource
	 * @param resources arraylist of the single discounts got from the leader card
	 */
	public void setDiscount(ArrayList<Resources> resources) {
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
	
	
	/** TODO: move this function somewhere else in the controller part of the project
	 * input management for moving things around in the warehouse
	 * @return a boolean that indicates if all the resources have been moved and if the warehouse configuration is correct
	 *         returns false if more moves are required
	 */
	protected boolean processNewMove() throws InvalidUserRequestException {
		Scanner scanner = new Scanner(System.in);
		String read;
		
		String regexGoingToWarehouse = "move\s[1-9]\sfrom\s(deck|depot)\sto\s[1-6]"; // regex pattern for reading input for moving the
		// resources to the warehouse
		String regexGoingToDeck = "move\s[1-6]\sto\sdeck"; //regex pattern for reading input for moving back to the deck
		
		myWarehouseDepot.showIncomingDeck();
		myWarehouseDepot.showDepot();
		System.out.println("write new move command");
		boolean checkGoingToWarehouse, checkGoingToDeck, ok ;
		int from = 0;
		String place = "";
		do {
			read = scanner.nextLine(); // user input
			
			checkGoingToWarehouse = Pattern.matches(regexGoingToWarehouse, read);
			if (checkGoingToWarehouse) { // process request for moving resource from the deck to the warehouse
				from = Character.getNumericValue(read.charAt(5));
				if (read.charAt(14) == 'c') { //send from deck
					place = "deck";
				} else { // send from depot
					place = "depot";
				}
				if (place.equals("deck") && from > myWarehouseDepot.getResourceDeckSize()) {
					ok = false; // invalid input: position out of deck bounds (size of list)
				} else ok = !place.equals("depot") || from <= 6; // invalid input: position out of depot bounds (from 1 to 6)
			} else { // sends the request
				checkGoingToDeck = Pattern.matches(regexGoingToDeck, read);
				ok = checkGoingToDeck;
			}
			
			if (!ok) { // user input does not match with the defined pattern
				System.out.println("input request invalid, write again");
			}
		} while (!ok); // while the input is not valid
		
		if (checkGoingToWarehouse) { // process request for moving to the warehouse
			if (place.equals("deck")) {
				return myWarehouseDepot.moveResources(place, from, Character.getNumericValue(read.charAt(20)));
			} else {
				return myWarehouseDepot.moveResources(place, from, Character.getNumericValue(read.charAt(21)));
			}
		} else { // process request for moving from the warehouse to the deck
			return myWarehouseDepot.moveResourcesBackToDeck(Character.getNumericValue(read.charAt(5)));
		}
		
	}
	
}
