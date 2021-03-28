package it.polimi.ingsw;

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
	 * @param colors color of the chosen card
	 * @param playerResources the resources accumulated by the player (sum of all the resources in the warehouse and in the strongbox)
	 * @return true if the chosen card can be bought, otherwise false if the requirements are not met
	 */
	protected boolean isCardBuyable(int level, Colors colors, ArrayList<Resources> playerResources) {
		int row = level - 1, column = mapColorToColumn(colors);
		
		if (cardStackStructure[row][column].isEmpty()) { //must be not empty in order to get the card on the top
			System.out.println("scemo");
			return false;
		}
		
		DevelopmentCard card = cardStackStructure[row][column].get(0);
		ArrayList<Resources> required = card.getResourcesRequirement();
		
		//TODO: check if the level of the selected card is equal to at least one top card in the player's cards slots plus 1
		
		
		if (playerResources.containsAll(required)) { // tha player has got all the necessary resources in order to buy the card
			System.out.println("GG");
			return true;
		} else {
			System.out.println("NOOB");
			return false;
		}
		// return playerResources.containsAll(required)
	}
	
	/**
	 * removes the card from the stack. Must be called only when the card is buyable
	 * @param level the chosen level
	 * @param color the chosen color
	 * @return the development card that has been removed from the stack
	 */
	protected DevelopmentCard claimCard(int level, Colors color) {
		int row = level - 1, column = mapColorToColumn(color);
		return cardStackStructure[row][column].remove(0);
	}
	
	/**
	 * transforms the color into the corresponding column in the deck matrix
	 * @param color the color to be mapped
	 * @return the index of the corresponding column to the input color
	 */
	protected int mapColorToColumn(Colors color) {
		switch (color) {
			case GREEN -> {
				return 0;
			}
			case BLUE -> {
				return 1;
			}
			case YELLOW -> {
				return 2;
			}
			case PURPLE -> {
				return 3;
			}
			default -> throw new IllegalStateException("Unexpected value: " + color);
		}
	}
	
}
