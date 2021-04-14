package it.polimi.ingsw;

import it.polimi.ingsw.util.Colors;
import it.polimi.ingsw.util.ListSet;
import it.polimi.ingsw.util.Resources;

import java.util.ArrayList;
import java.util.Collections;

public class DevelopmentCardsDeck {
	
	//4 cards for each level (1-3) and each color (4 colors available)
	private final ArrayList<DevelopmentCard>[][] cardStackStructure;
	
	/**
	 * constructor that shuffles the piles of the development cards. Initializes the initial configuration of the cards stacks
	 * @param standardDeck sent by the XML parser from Game Mechanics. It contains the developments cards with the same order as the cards read in
	 *                        the configuration file
	 */
	public DevelopmentCardsDeck(ArrayList<DevelopmentCard>[][] standardDeck) {
		cardStackStructure = standardDeck;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				Collections.shuffle(cardStackStructure[i][j]);
			}
		}
		
	}
	
	
	/**
	 * checks if the card is buyable. Checks if the resources are enough to buy the card. Checks also if the level of the cards in the player's
	 * deck is compatible with the chosen card.
	 * @param level level of the card to choose (user input)
	 * @param color color of the chosen card
	 * @param playerResources the resources accumulated by the player (sum of all the resources in the warehouse and in the strongbox)
	 * @return true if the chosen card can be bought, otherwise false if the requirements are not met
	 */
	public boolean isSelectedDevCardBuyable(int level, Colors color, ArrayList<Resources> playerResources, CardProductionsManagement playersCard)  {
		int row = level - 1, column = color.getColorNumber();
		
		if (cardStackStructure[row][column].isEmpty()) { //must be not empty in order to get the card on the top
			return false;
		}
		
		DevelopmentCard card = cardStackStructure[row][column].get(0);
		ArrayList<Resources> required = card.getResourcesRequirement();
		
		if (!ListSet.subset(playerResources, required)) { // the player has got all the necessary resources in order to buy the card
			return false;
		}
		
		// checks if the level of the selected card is equal to at least one top card in the player's cards slots plus 1
		if (playersCard.checkStackLevel(1) == level - 1) {
			return true;
		} else if (playersCard.checkStackLevel(2) == level - 1) {
			return true;
		} else return playersCard.checkStackLevel(3) == level - 1;
		
		
	}

	/**
	 * get the price of the card
	 * @param level level of the card
	 * @param color color of the card
	 * @return arraylist of required resources
	 */
	public ArrayList<Resources> getPriceDevCard(int level, Colors color){

		int row = level - 1, column = color.getColorNumber();
		return cardStackStructure[row][column].get(0).getResourcesRequirement();

	}
	
	
	/**
	 * checks if there exists at least one dev card that can be bought from the player
	 * @param playersResources all the resources tha player has
	 * @param manager the card management instance containing the cards
	 * @return true if there is at least one card buyable
	 */
	public boolean canBuyAnyDevCard(ArrayList<Resources> playersResources, CardProductionsManagement manager) {
		for (int i = 1; i <= 3; i++) {
			for (Colors j: Colors.values()) {
				if (isSelectedDevCardBuyable(i, j, playersResources, manager)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * removes the card from the stack. Must be called only when the card is buyable
	 * @param level the chosen level
	 * @param color the chosen color
	 * @return the development card that has been removed from the stack
	 */
	public DevelopmentCard claimCard(int level, Colors color) {
		int row = level - 1, column = color.getColorNumber();
		return cardStackStructure[row][column].remove(0);
	}
	
	/**
	 * finds the card with the lowest level present in the deck, given the color
	 * @param color chosen color
	 * @return the lowest level available
	 */
	public int lowestCardLevelAvailable(Colors color) {
		int colorN = color.getColorNumber();
		for (int level = 1; level <= 3; level++) {
			if (!cardStackStructure[level][colorN].isEmpty()) return level;
		}
		return 0; // there are no more cards of this color in the cards deck
	}

	public ArrayList<DevelopmentCard>[][] getCardStackStructure() {
		return cardStackStructure;
	}

	public void showDevelopmentCardsDeck(){

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(cardStackStructure[i][j].isEmpty())
					System.out.println("EMPTY");
				cardStackStructure[i][j].get(0).showCard();
			}
			System.out.println("\n");
		}
	}

	public void showBuyableCards(ArrayList<Resources> playerResources, CardProductionsManagement playerCards){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(!cardStackStructure[i][j].isEmpty() &&
						isSelectedDevCardBuyable(cardStackStructure[i][j].get(0).getLevel(),cardStackStructure[i][j].get(0).getColor(),playerResources,playerCards))
					cardStackStructure[i][j].get(0).showCard();
			}

		}
	}
}
