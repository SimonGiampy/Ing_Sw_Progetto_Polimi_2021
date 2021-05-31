package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;

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
	private final CardProductionsManagement cardManager;
	
	private  Market commonMarket;
	
	private  DevelopmentCardsDeck commonCardsDeck;
	
	private LeaderCard[] leaderCards;
	private boolean activeAbilityLeader1; // indicates whether the first leader card ability is activated or not
	private boolean activeAbilityLeader2; // indicates whether the second leader card ability is activated or not
	private boolean discardedLeader1;
	private boolean discardedLeader2;

	private final int playerIndex;

	private int coinDiscount;
	private int servantDiscount;
	private int shieldDiscount;
	private int stoneDiscount;
	
	private int extraProdLeader1;
	private int extraProdLeader2;
	
	/**
	 * constructor for the Player
	 * @param market common market shared by all the players in the game
	 * @param developmentCardsDeck the deck of development cards shared by all the players
	 * @param warehouseDepot the player's Warehouse depot and the additional depots
	 * @param strongbox the player's Strongbox
	 * @param resourceDeck the player's resource deck instance
	 * @param faithTrack player's personal faith track (equal to all the other players)
	 * @param cardProductionsManagement card manager for all the cards
	 * @param leaderCards the 4 leader cards to be assigned to the player
	 */
	public Player(Market market, DevelopmentCardsDeck developmentCardsDeck, WarehouseDepot warehouseDepot, Strongbox strongbox,
	              ResourceDeck resourceDeck, FaithTrack faithTrack, CardProductionsManagement cardProductionsManagement,
				  LeaderCard[] leaderCards, int playerIndex) {
		
		commonMarket = market;
		commonCardsDeck = developmentCardsDeck;

		this.leaderCards = leaderCards;
		
		myWarehouseDepot = warehouseDepot;
		myFaithTrack = faithTrack;
		myResourceDeck = resourceDeck;
		myStrongbox = strongbox;
		cardManager = cardProductionsManagement;
		
		coinDiscount = 0;
		servantDiscount = 0;
		shieldDiscount = 0;
		stoneDiscount = 0;
		
		extraProdLeader1 = 0;
		extraProdLeader2 = 0;

		activeAbilityLeader1 = false;
		activeAbilityLeader2 = false;

		this.playerIndex = playerIndex;
	}


	/**
	 * choose two leader cards out of the 4 given at the beginning of the game
	 * @param indexCard1 index of the first card to choose
	 * @param indexCard2 index of the second card to choose
	 */
	public void chooseTwoLeaders(int indexCard1, int indexCard2){
		LeaderCard[] temp = new LeaderCard[2];
		temp[0] = leaderCards[indexCard1 - 1];
		temp[1] = leaderCards[indexCard2 - 1];
		leaderCards = temp;

		discardedLeader1 = false;
		discardedLeader2 = false;
	}
	
	
	/**
	 * discards one of the two leader cards owned by the player and give him one faith point
	 * @param indexCard index of the leader card to discard
	 */
	public void discardLeaderCard(int indexCard) {
		if (indexCard == 0 && !activeAbilityLeader1) {
			discardedLeader1 = true;
		} else if (indexCard == 1 && !activeAbilityLeader2) {
			discardedLeader2 = true;
		}
		myFaithTrack.moveMarker(1);
	}

	/**
	 * shifts the selected row/col of the market and sets the array of marbles in the deck
	 * @param which row or col
	 * @param where number of the row/col
	 */
	public void interactWithMarket(String which, int where){
		Marbles[] output;
		if (which.equals("COL")) {
			output = commonMarket.shiftCol(where - 1);
		} else {
			output = commonMarket.shiftRow(where - 1);
		}
		myResourceDeck.setMarblesFromMarket(output);
	}
	
	/**
	 * converts the marbles in resources after the choice of the player regarding white marbles leaders
	 */
	public void convertMarketOutput(int quantity1, int quantity2) throws InvalidUserRequestException {
		myResourceDeck.addResources(quantity1 ,quantity2);
	}

	/**
	 * checks which action are possible in the current situation
	 * @return list of possible actions
	 */
	public ArrayList<PlayerActions> checkAvailableActions(){
		ArrayList<PlayerActions> actions = new ArrayList<>(checkAvailableNormalActions());
		if(!checkAvailableLeaderActions().isEmpty())
			actions.add(PlayerActions.LEADER);
		return actions;
	}

	/**
	 * Checks which main actions the player can do in the current turn
	 * @return a list of player actions that contains the available actions the player can do
	 */
	public ArrayList<PlayerActions> checkAvailableNormalActions() {
		ArrayList<PlayerActions> actions = new ArrayList<>();
		actions.add(PlayerActions.MARKET);
		if (isBuyMoveAvailable()) {
			actions.add(PlayerActions.BUY_CARD);
		}
		if (cardManager.isAtLeastOneProductionAvailable()) {
			actions.add(PlayerActions.PRODUCTIONS);
		}
		return actions;
	}

	/**
	 * Checks which leader actions the player can do in the current turn
	 * @return a list of player actions that contains the available leader actions the player can do
	 */
	public ArrayList<PlayerActions> checkAvailableLeaderActions(){

		ArrayList<PlayerActions> actions = new ArrayList<>();
		if(!activeAbilityLeader1 && !discardedLeader1 && leaderCards[0].checkCards(cardManager.getPlayerCardsRequirements()) &&
				leaderCards[0].checkResources(gatherAllPlayersResources())) {
			actions.add(PlayerActions.PLAY_LEADER_1);
		}
		if(!activeAbilityLeader2 && !discardedLeader2 && leaderCards[1].checkCards(cardManager.getPlayerCardsRequirements()) &&
				leaderCards[1].checkResources(gatherAllPlayersResources())) {
			actions.add(PlayerActions.PLAY_LEADER_2);
		}
		if(!activeAbilityLeader1 && !discardedLeader1){
			actions.add(PlayerActions.DISCARD_LEADER_1);
		}
		if(!activeAbilityLeader2 && !discardedLeader2){
			actions.add(PlayerActions.DISCARD_LEADER_2);
		}

		return actions;
	}
	
	/**
	 * it checks if the player has reached endgame
	 * @return true if the player owns 7 cards or has reached the end of the faith track
	 */
	public boolean isEndgameStarted() {
		return (cardManager.numberOfCards() == 7 || myFaithTrack.isTrackFinished());
	}

	/**
	 * it calculates player's victory points
	 * @return player's score
	 */
	public int totalScore() {
		int resourcesVictoryPoints = gatherAllPlayersResources().size() / 5;
		int cardVictoryPoints = cardManager.totalVictoryPoints();
		int faithTrackPoints = myFaithTrack.countFaithTrackVictoryPoints();
		int leaderVictoryPoints = 0;
		if(activeAbilityLeader1)
			leaderVictoryPoints = leaderVictoryPoints + leaderCards[0].getVictoryPoints();
		if(activeAbilityLeader2)
			leaderVictoryPoints = leaderVictoryPoints + leaderCards[1].getVictoryPoints();
		return resourcesVictoryPoints + cardVictoryPoints + faithTrackPoints + leaderVictoryPoints;
	}
	
	/**
	 * checks whether the player can buy a new dev card
	 * @return true if a new dev card can be bought, false otherwise
	 */
	public boolean isBuyMoveAvailable() {
		return commonCardsDeck.canBuyAnyDevCard(gatherResourcesWithDiscounts(), cardManager);
	}
	
	/**
	 * gathers all player's resources
	 * @return a list of all the resources of the player, from warehouse depot and the strongbox
	 */
	public ArrayList<Resources> gatherAllPlayersResources() {
		ArrayList<Resources> total = myStrongbox.getContent();
		total.addAll(myWarehouseDepot.gatherAllResources());
		return total;
	}
	
	/**
	 * calculates a list of resources, adding more resources based on the discount values. Method called when buying dev cards
	 * @return a list of user resources with more resources, faking the fact that the player has actually less resources
	 */
	public ArrayList<Resources> gatherResourcesWithDiscounts() {
		ArrayList<Resources> payback = new ArrayList<>(gatherAllPlayersResources());
		//Apply the discounts of the leader cards, adding resources instead of removing them, faking a bigger availability of resources
		for(int i = 0; i < coinDiscount; i++)
			payback.add(Resources.COIN);
		for(int i = 0; i < servantDiscount; i++)
			payback.add(Resources.SERVANT);
		for(int i = 0; i < stoneDiscount; i++)
			payback.add(Resources.STONE);
		for(int i = 0; i < shieldDiscount; i++)
			payback.add(Resources.SHIELD);
		return payback;
	}

	/**
	 * count the resources of the player
	 * @return number of all the resources owned by the player
	 */
	public int numberOfResources(){
		return gatherAllPlayersResources().size();
	}

	/**
	 * buy a dev card from the commonDeck and pay the price
	 * @param level level of the card
	 * @param color color fo the card
	 * @param selectedSlot number of the slot where the player wants to put the card
	 */
	public void buyNewDevCard(int level, Colors color, int selectedSlot) {
		
		//Get the price, which is the sum of all the resources needed for buying the dev card
		ArrayList <Resources> price = commonCardsDeck.getPriceDevCard(level, color);

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

	}

	/**
	 * activates the selected productions and moves the faithTrack marker accordingly
	 * @param playerInput is a list of selected production
	 * @param resourcesInput is an array of number of Resources [#COIN,#SERVANT,#SHIELD,#STONE].
	 *                       When the production does not have ?, the caller set it to [0,0,0,0].
	 * @param resourcesOutput is an array of number of Resources [#COIN,#SERVANT,#SHIELD,#STONE].
	 *                       When the production does not have ?, the caller set it to [0,0,0,0].
	 * @return true if the marker has been moved after a production
	 */
	public boolean activateProduction(ArrayList<Productions> playerInput, int[] resourcesInput, int[] resourcesOutput) {
		cardManager.takeSelectedResources(playerInput,resourcesInput);
		int moves = cardManager.activateSelectedProduction(playerInput,resourcesOutput);
		myFaithTrack.moveMarker(moves);
		return moves > 0;
	}


	
	/**
	 * this function tells the leader to activate its ability. Passes as value itself so that the ability knows who is the player referring to it
	 * @param whichLeader the index in the array corresponding to the leader to be activated (0 or 1)
	 */
	public void activateLeaderCard(int whichLeader) {
		//passes this as object parameter so that the ability know to which player to refer to for the activation
		if (leaderCards[whichLeader].getIdNumber() >= 13 && leaderCards[whichLeader].getIdNumber() <= 16) {
			if (extraProdLeader1 > 0) {
				extraProdLeader2 = whichLeader + 1 + leaderCards[whichLeader].getIdNumber() * 10;
			} else {
				extraProdLeader1 = whichLeader + 1 + leaderCards[whichLeader].getIdNumber() * 10;
			}
		}
	
		leaderCards[whichLeader].activateAbility(this);
		if (whichLeader == 0) {
			activeAbilityLeader1 = true;
			discardedLeader1 = false;
		} else if (whichLeader == 1) {
			activeAbilityLeader2 = true;
			discardedLeader2 = false;
		}
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
	
	public CardProductionsManagement getPlayersCardManager() {
		return cardManager;
	}
	
	public FaithTrack getPlayerFaithTrack() {
		return myFaithTrack;
	}
	public Strongbox getMyStrongbox() {
		return myStrongbox;
	}


	public int getPlayerIndex() {
		return playerIndex;
	}

	public LeaderCard[] getLeaderCards() {
		return leaderCards;
	}

	public boolean isActiveAbilityLeader1() {
		return activeAbilityLeader1;
	}

	public boolean isActiveAbilityLeader2() {
		return activeAbilityLeader2;
	}
	
	public int getCoinDiscount() {
		return coinDiscount;
	}

	public int getServantDiscount() {
		return servantDiscount;
	}

	public int getShieldDiscount() {
		return shieldDiscount;
	}

	public int getStoneDiscount() {
		return stoneDiscount;
	}

	public boolean isDiscardedLeader1() {
		return discardedLeader1;
	}

	public boolean isDiscardedLeader2() {
		return discardedLeader2;
	}

	public void setCommonCardsDeckForDebugging(DevelopmentCardsDeck commonCardsDeck) {
		this.commonCardsDeck = commonCardsDeck;
	}

	public Market getCommonMarket() {
		return commonMarket;
	}
	
	protected void setCommonMarketForDebugging(Market commonMarket) {
		this.commonMarket = commonMarket;
	}
	
	/**
	 * returns the corresponding ordinal number of leader card
	 * @param cardId id read from the xml
	 * @return the leader card number for the extra production
	 */
	public int getExtraProdLeader(int cardId) {
		if (extraProdLeader1 / 10 == cardId) {
			return extraProdLeader1 % 10;
		} else {
			return extraProdLeader2 % 10;
		}
	}
	
}
