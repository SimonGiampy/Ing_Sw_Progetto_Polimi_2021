package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Stack;

public class Board extends ViewObservable implements SceneController {
	
	@FXML private Button actProductions; // used for activating a production
	
	// papal zones visible after a vatican report
	@FXML private ImageView papal1;
	@FXML private ImageView papal2;
	@FXML private ImageView papal3;
	@FXML private ImageView leftCornerImage;
	
	// 3 development cards per slot. 3 slots present on the main board
	@FXML private ImageView img11;
	@FXML private ImageView img12;
	@FXML private ImageView img13;
	@FXML private ImageView img21;
	@FXML private ImageView img22;
	@FXML private ImageView img23;
	@FXML private ImageView img31;
	@FXML private ImageView img32;
	@FXML private ImageView img33;
	
	// leader card with the relative buttons for the interaction
	@FXML private ImageView leader1;
	@FXML private ImageView leader2;
	@FXML private Button act1;
	@FXML private Button dis1;
	@FXML private Button act2;
	@FXML private Button dis2;
	
	//strongbox text boxes with the quantities of the resources stored
	@FXML private Label numCoin;
	@FXML private Label numStone;
	@FXML private Label numShield;
	@FXML private Label numServant;
	
	// warehouse depot images
	@FXML private ImageView depot1;
	@FXML private ImageView depot2;
	@FXML private ImageView depot3;
	@FXML private ImageView depot4;
	@FXML private ImageView depot5;
	@FXML private ImageView depot6;
	
	// additional depot resources images
	@FXML private ImageView res11;
	@FXML private ImageView res12;
	@FXML private ImageView res21;
	@FXML private ImageView res22;
	
	// resource deck with the resources incoming from the market
	@FXML private ImageView deckContainer;
	@FXML private ImageView deck1;
	@FXML private ImageView deck2;
	@FXML private ImageView deck3;
	@FXML private ImageView deck4;
	
	// controls used for the depot interaction
	@FXML private Button confirmationDepot;
	@FXML private Label invalidMove;
	
	// faith track marker
	@FXML private ImageView redCross;
	@FXML private ImageView blackCross;
	private ArrayList<Coordinates> coordinates; // list of coordinates for the cross in the faith track
	private int redPosition, blackPosition;

	// image representing the base production, serves as clicking spot
	@FXML private ImageView baseProduction;
	
	
	
	//references to the dev cards in the 3 slots
	private ImageView[] slot1;
	private ImageView[] slot2;
	private ImageView[] slot3;
	private int sizeSlot1;
	private int sizeSlot2;
	private int sizeSlot3;
	
	// used for handling production on the main player board
	private ArrayList<Productions> selectedProduction;
	private ArrayList<Productions> availableProduction;
	private boolean productionAble;
	
	// integer flags indicating the corresponding leader card (ordinal integer number) to the list of extra depot leaders
	//TODO: add code that modifies these parameters when the player decides to activate a leader card with the extra depot ability
	private int extraDepotLeader1Activation; // = 0 if not active, = 1 if it's the first leader, =2 if it's the second leader
	private int extraDepotLeader2Activation; // = 0 if not active, = 1 if it's the first leader, =2 if it's the second leader
	
	@FXML
	public void initialize() {
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 10);
		
		// coordinates of the faith cross positions
		coordinates = new ArrayList<>();
		coordinates.add(new Coordinates(0, 61, 212));
		coordinates.add(new Coordinates(1, 136, 212));
		coordinates.add(new Coordinates(2, 209, 212));
		coordinates.add(new Coordinates(3, 209, 140));
		coordinates.add(new Coordinates(4, 209, 66));
		coordinates.add(new Coordinates(5, 284, 66));
		coordinates.add(new Coordinates(6, 358, 66));
		coordinates.add(new Coordinates(7, 431, 66));
		coordinates.add(new Coordinates(8, 504, 66));
		coordinates.add(new Coordinates(9, 581, 66));
		coordinates.add(new Coordinates(10, 579, 138));
		coordinates.add(new Coordinates(11, 579, 214));
		coordinates.add(new Coordinates(12, 655, 214));
		coordinates.add(new Coordinates(13, 728, 214));
		coordinates.add(new Coordinates(14, 802, 214));
		coordinates.add(new Coordinates(15, 877, 214));
		coordinates.add(new Coordinates(16, 950, 214));
		coordinates.add(new Coordinates(17, 950, 137));
		coordinates.add(new Coordinates(18, 950, 66));
		coordinates.add(new Coordinates(19, 1025, 66));
		coordinates.add(new Coordinates(20, 1098, 66));
		coordinates.add(new Coordinates(21, 1172, 66));
		coordinates.add(new Coordinates(22, 1245, 66));
		coordinates.add(new Coordinates(23, 1320, 66));
		coordinates.add(new Coordinates(24, 1394, 66));

		//development card slots
		slot1 = new ImageView[]{img11, img12, img13};
		slot2 = new ImageView[]{img21, img22, img23};
		slot3 = new ImageView[]{img31, img32, img33};
		selectedProduction = new ArrayList<>();
		availableProduction = new ArrayList<>();
		sizeSlot1=0;
		sizeSlot2=0;
		sizeSlot3=0;
		
		updateCrossCoords(false, 0); // initial position when the game starts
		redPosition = 0;
		blackPosition = -1;
		
		//mouse click handlers for the series of productions available on the board
		baseProduction.setOnMouseClicked(event -> productionSelectionHandler(Productions.BASE_PRODUCTION, baseProduction));
		leader1.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_1_PRODUCTION, leader1));
		leader2.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_2_PRODUCTION, leader2));

		act1.setOnMouseClicked(event -> leaderActionHandler(1,1));
		act2.setOnMouseClicked(event -> leaderActionHandler(2,1));
		dis1.setOnMouseClicked(event -> leaderActionHandler(1,2));
		dis2.setOnMouseClicked(event -> leaderActionHandler(2,2));

		extraDepotLeader1Activation = 0;
		extraDepotLeader2Activation = 0;
	}
	
	/**
	 * if the mode is single player, adds a new black cross to the faith track. Also places the tokens in the same place where the inkwell is.
	 */
	public void setSinglePlayerMode() {
		leftCornerImage.setImage(new Image("/assets/token/0.png"));
		blackCross.setVisible(true);
		updateCrossCoords(true, 0);
	}

	public void updateToken(TokenType type,Colors color){

		if(type.equals(TokenType.DISCARD_TOKEN))
			leftCornerImage.setImage(new Image("/assets/token/"+(color.colorNumber+1)+".png"));
		else if(type.equals(TokenType.BLACK_CROSS_TOKEN))
			leftCornerImage.setImage(new Image("/assets/token/5.png"));
		else
			leftCornerImage.setImage(new Image("/assets/token/6.png"));

	}
	/**
	 * updates the position of the red cross on the faith track
	 * @param pos new position on the track
	 */
	public void updateCrossCoords(boolean lorenzo, int pos) {
		
		if (lorenzo) { // moving the black cross
			if (redPosition == blackPosition) {
				redCross.setLayoutX(coordinates.get(redPosition).getX());
				redCross.setLayoutY(coordinates.get(redPosition).getY());
				redCross.setFitWidth(73);
				redCross.setFitHeight(64);
				blackCross.setLayoutX(coordinates.get(pos).getX());
				blackCross.setLayoutY(coordinates.get(pos).getY());
				blackCross.setFitWidth(73);
				blackCross.setFitHeight(64);
			} else if (redPosition == pos) { // intercepts the red cross
				redCross.setFitWidth(52);
				redCross.setFitHeight(46);
				blackCross.setLayoutX(coordinates.get(pos).getX() + 27);
				blackCross.setLayoutY(coordinates.get(pos).getY() + 24);
				blackCross.setFitWidth(52);
				blackCross.setFitHeight(46);
			} else {
				blackCross.setLayoutX(coordinates.get(pos).getX());
				blackCross.setLayoutY(coordinates.get(pos).getY());
				blackCross.setFitWidth(73);
				blackCross.setFitHeight(64);
			}
			blackPosition = pos;
		} else { // moving the red cross
			if (redPosition == blackPosition) {
				redCross.setLayoutX(coordinates.get(pos).getX());
				redCross.setLayoutY(coordinates.get(pos).getY());
				redCross.setFitWidth(73);
				redCross.setFitHeight(64);
				blackCross.setLayoutX(coordinates.get(blackPosition).getX());
				blackCross.setLayoutY(coordinates.get(blackPosition).getY());
				blackCross.setFitWidth(73);
				blackCross.setFitHeight(64);
			} else if (blackPosition == pos) { //intercepts the black cross
				blackCross.setFitWidth(52);
				blackCross.setFitHeight(46);
				redCross.setLayoutX(coordinates.get(pos).getX() + 27);
				redCross.setLayoutY(coordinates.get(pos).getY() + 24);
				redCross.setFitWidth(52);
				redCross.setFitHeight(46);
			} else {
				redCross.setLayoutX(coordinates.get(pos).getX());
				redCross.setLayoutY(coordinates.get(pos).getY());
				redCross.setFitWidth(73);
				redCross.setFitHeight(64);
			}
			redPosition = pos;
		}
		
	}
	
	/**
	 * sets the visibility of the papal zones
	 * @param num ordinal number indicating the zone to be activated
	 */
	public void activatePapalZone(int num) {
		switch (num) {
			case 1 -> papal1.setVisible(true);
			case 2 -> papal2.setVisible(true);
			case 3 -> papal3.setVisible(true);
		}
	}
	
	/**
	 * updates the strongbox contents
	 * @param strongbox reduced class
	 */
	public void updateStrongbox(ReducedStrongbox strongbox){
		ArrayList<Resources> content = strongbox.getContent();
		setNumerosity(Resources.COIN, ListSet.count(content, Resources.COIN));
		setNumerosity(Resources.SERVANT, ListSet.count(content, Resources.SERVANT));
		setNumerosity(Resources.SHIELD, ListSet.count(content, Resources.SHIELD));
		setNumerosity(Resources.STONE, ListSet.count(content, Resources.STONE));
	}
	
	/**
	 * sets the number of resources present in the depot
	 * @param res the specific resource
	 * @param num quantity
	 */
	private void setNumerosity(Resources res, int num) {
		switch (res) {
			case STONE -> numStone.setText(String.valueOf(num));
			case SHIELD -> numShield.setText(String.valueOf(num));
			case SERVANT -> numServant.setText(String.valueOf(num));
			case COIN -> numCoin.setText(String.valueOf(num));
		}
	}

	/**
	 * updates the development cards in the slots, setting the images if not set yet.
	 * @param cardManager reduces class containing the stacks of the dev cards on the player board.
	 */
	public void updateDevCardsSlots(ReducedCardProductionManagement cardManager) {
		ArrayList<Stack<DevelopmentCard>> cards = cardManager.getCards();
		sizeSlot1 = cards.get(0).size();
		sizeSlot2 = cards.get(1).size();
		sizeSlot3 = cards.get(2).size();
		
		//slot 1 old dev cards and new dev cards
		if (sizeSlot1 > 0) {
			if (slot1[sizeSlot1 - 1].getImage() == null) {
				slot1[sizeSlot1 - 1].setImage(new Image("/assets/devCards/" + cards.get(0).get(sizeSlot1 - 1).getCardNumber() + ".png"));
				slot1[sizeSlot1 - 1].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_1_CARD_PRODUCTION, slot1[sizeSlot1 - 1]));
			}
			for (int i = 0; i < sizeSlot1 - 1; i++) {
				slot1[i].setOnMouseClicked(null);
			}
		}
		
		//slot 2 old dev cards and new dev cards
		if (sizeSlot2 > 0) {
			if (slot2[sizeSlot2 - 1].getImage() == null) {
				slot2[sizeSlot2 - 1].setImage(new Image("/assets/devCards/" + cards.get(1).get(sizeSlot2 - 1).getCardNumber() + ".png"));
				slot2[sizeSlot2 - 1].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_2_CARD_PRODUCTION, slot2[sizeSlot2 - 1]));
			}
			for (int i = 0; i < sizeSlot2 - 1; i++) {
				slot2[i].setOnMouseClicked(null);
			}
		}

		//slot 3 old dev cards and new dev cards
		if (sizeSlot3 > 0) {
			if (slot3[sizeSlot3 - 1].getImage() == null) {
				slot3[sizeSlot3 - 1].setImage(new Image("/assets/devCards/" + cards.get(2).get(sizeSlot3 - 1).getCardNumber() + ".png"));
				slot3[sizeSlot3 - 1].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_3_CARD_PRODUCTION, slot3[sizeSlot3 - 1]));
			}
			for (int i = 0; i < sizeSlot3 - 1; i++) {
				slot3[i].setOnMouseClicked(null);
			}
		}
		
	}

	/**
	 * sets leader card of the player. Also activates or deactivates the buttons relative to the leader cards based on the game status and
	 * who is the player in this board.
	 * @param leaderCards player's leader cards
	 */
	public void setMyLeaderCards(ArrayList<ReducedLeaderCard> leaderCards) {
		boolean done=false;
		ImageView[] images = new ImageView[]{leader1, leader2};
		for (int i = 0; i < leaderCards.size(); i++) {
			if (leaderCards.get(i).isPlayable()) {
				done=true;
				if (i == 0) {
					act1.setDisable(false);
					dis1.setDisable(false);
				} else if (i == 1) {
					act2.setDisable(false);
					dis2.setDisable(false);
				}
			} else if(leaderCards.get(i).isAbilitiesActivated()){
				done=true;
				if(i==0) {
					act1.setDisable(true);
					dis1.setDisable(true);
				}else {
					act2.setDisable(true);
					dis2.setDisable(true);
				}
			} else if (leaderCards.get(i).isDiscarded()) {
				// card is less bright to indicate that it has been discarded
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.5);
				images[i].setEffect(colorAdjust);
				done=true;
				if(i==0) {
					act1.setDisable(true);
					dis1.setDisable(true);
				} else {
					act2.setDisable(true);
					dis2.setDisable(true);
				}

			} else {
				if(!done) {
					images[i].setImage(new Image("/assets/leaderCards/" + leaderCards.get(i).getIdNumber() + ".png"));
					act1.setDisable(true);
					act2.setDisable(true);
					dis1.setDisable(false);
					dis2.setDisable(false);
				}
			}
		}
	}

	/**
	 * sets opponent's leader cards
	 * @param leaderCards opponent's leader card
	 */
	public void setOthersLeaderCards(ArrayList<ReducedLeaderCard> leaderCards){
		ImageView[] images = new ImageView[]{leader1, leader2};
		for (int i = 0; i < leaderCards.size(); i++) {
			if(leaderCards.get(i).isAbilitiesActivated()){
				images[i].setImage(new Image("/assets/leaderCards/" + leaderCards.get(i).getIdNumber() + ".png"));
			}
			if (leaderCards.get(i).isDiscarded()) { // card is less bright to indicate that it has been discarded
				images[i].setImage(new Image("/assets/leaderCards/" + leaderCards.get(i).getIdNumber() + ".png"));
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.5);
				images[i].setEffect(colorAdjust);
			}
		}
	}

	public void leaderActionHandler(int card, int action){
			notifyObserver(obs -> obs.onUpdateLeaderAction(card,action));
	}

	/**
	 * sets the visibility of buttons for opponent's boards
	 */
	public void setOpponentBoard(){
		act1.setVisible(false);
		dis1.setVisible(false);
		act2.setVisible(false);
		dis2.setVisible(false);
		actProductions.setVisible(false);
		leader1.setImage(new Image("/assets/leaderCards/retro.png"));
		leader2.setImage(new Image("/assets/leaderCards/retro.png"));
	}

	/**
	 * sets the inkwell visible if this is the first's player board
	 */
	public void setStartingPlayer(){
		//TODO: if this is not single player mode
		leftCornerImage.setVisible(true);
	}

	

	public void setActProductions(boolean value){
		actProductions.setVisible(value);
	}

	public void activateProduction(){
		//TODO: cannot do 2 productions in a row
		if(actProductions.getText().equals("Activate Productions")) {
			notifyObserver(obs -> obs.onUpdateAction(PlayerActions.PRODUCTIONS));
			actProductions.setDisable(true);
			actProductions.setText("Confirm Productions");
			productionAble = true;
		} else {
			notifyObserver(obs->obs.onUpdateProductionAction(selectedProduction));
			actProductions.setText("Activate Productions");
			actProductions.setDisable(false); // TODO: enable this button if there are productions available
			productionAble=false;
			setNormal();
		}
	}
	
	public void productionSelectionHandler(Productions production, ImageView image) {
		if(productionAble && availableProduction.contains(production)) {
			if (!selectedProduction.contains(production)) {
				image.setEffect(new Glow(0.5));
				selectedProduction.add(production);
				actProductions.setDisable(false);
			} else {
				setShadow(0.5, image);
				selectedProduction.remove(production);
				if(selectedProduction.size()==0)
					actProductions.setDisable(true);
			}
		}
	}



	public void setAvailableProductionRed(ArrayList<Productions> productions){
		availableProduction = productions;
		if (productions.contains(Productions.BASE_PRODUCTION))
			setShadow(0.5, baseProduction);
		if (productions.contains(Productions.STACK_1_CARD_PRODUCTION)) {
			setShadow(0.5, slot1[sizeSlot1-1]);
		}
		if (productions.contains(Productions.STACK_2_CARD_PRODUCTION)){
			setShadow(0.5, slot2[sizeSlot2-1]);
		}
		if (productions.contains(Productions.STACK_3_CARD_PRODUCTION)){
			setShadow(0.5, slot3[sizeSlot3-1]);
		}
		if (productions.contains(Productions.LEADER_CARD_1_PRODUCTION)){
			setShadow(0.5, leader1);
		}
		if (productions.contains(Productions.LEADER_CARD_2_PRODUCTION)){
			setShadow(0.5, leader2);
		}

	}

	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.LIGHTCORAL);
		img.setEffect(dropShadow);
	}

	public void setProductionAble(boolean productionAble) {
		this.productionAble = productionAble;
	}

	public void setNormal(){
		baseProduction.setEffect(new Glow(0));
		leader1.setEffect(new Glow(0));
		leader2.setEffect(new Glow(0));
		int i= sizeSlot1;
		if(i>0)
			slot1[i-1].setEffect(new Glow(0));
		i= sizeSlot2;
		if(i>0)
			slot2[i-1].setEffect(new Glow(0));
		i= sizeSlot3;
		if(i>0)
			slot3[i-1].setEffect(new Glow(0));
	}
	
	
	/**
	 * Updates the images on the warehouse depots and the additional depots (if the relative ability is activated).
	 * Sets the incoming resources deposit if there are any and makes it visible.
	 * @param depot reduced class containing the incoming resources, warehouse depots and additional depots
	 */
	public void updateDepots(ReducedWarehouseDepot depot) {
		// updates the warehouse depots
		Resources[] resources = depot.getDepot();
		//System.out.println("depot arrived like this = " + Arrays.toString(resources));
		ImageView[] images = new ImageView[]{depot1, depot2, depot3, depot4, depot5, depot6};
		for (int i = 0; i < 6; i++) {
			if (resources[i] == Resources.EMPTY) {
				images[i].setImage(null);
			} else {
				images[i].setImage(new Image(resources[i].path));
			}
		}
		
		//updates the additional depots if they're set and the abilities are activated (only if the image hasn't been set yet)
		//TODO: there is 100% a bug where if the leader activated with the additional depots is the second one (visually) then
		//      the resources will be put over the first leader image. This is because the Model treats the leaders activated regardless
		//      of the order in which the leaders are displayed.
		//  Solution: use variables to identify which leader has been activated and sets the corresponding images accordingly
		//  TODO (Update) : solved the bug, but the 2 integer flags must be set when a leader is activated (read above)
		if (depot.isLeaderActivated(0)) {
			if (depot.getExtraDepotContents().get(0).get(0)) {
				if (extraDepotLeader1Activation == 1) {
					res11.setImage(new Image(depot.getExtraDepotResources().get(0).get(0).path));
				} else if (extraDepotLeader1Activation == 2) {
					res21.setImage(new Image(depot.getExtraDepotResources().get(0).get(0).path));
				}
			}
			if (depot.getExtraDepotContents().get(0).get(1)) {
				if (extraDepotLeader1Activation == 1) {
					res12.setImage(new Image(depot.getExtraDepotResources().get(0).get(1).path));
				} else if (extraDepotLeader1Activation == 2) {
					res22.setImage(new Image(depot.getExtraDepotResources().get(0).get(1).path));
				}
			}
		}
		if (depot.isLeaderActivated(1)) {
			if (depot.getExtraDepotContents().get(1).get(0)) {
				if (extraDepotLeader2Activation == 1) {
					res11.setImage(new Image(depot.getExtraDepotResources().get(1).get(0).path));
				} else if (extraDepotLeader2Activation == 2) {
					res21.setImage(new Image(depot.getExtraDepotResources().get(1).get(0).path));
				}
			}
			if (depot.getExtraDepotContents().get(1).get(1)) {
				if (extraDepotLeader2Activation == 1) {
					res12.setImage(new Image(depot.getExtraDepotResources().get(1).get(0).path));
				} else if (extraDepotLeader2Activation == 2) {
					res22.setImage(new Image(depot.getExtraDepotResources().get(1).get(0).path));
				}
			}
		}
		
		// update incoming resource deck contents. Deck must not contain any empty resources
		ArrayList<Resources> incomingDeck = depot.getIncomingResources();
		if (!incomingDeck.isEmpty()) {
			deck1.setImage(new Image(incomingDeck.get(0).path));
			if (incomingDeck.size() >= 2) {
				deck2.setImage(new Image(incomingDeck.get(1).path));
			} else {
				deck2.setImage(null);
				deck3.setImage(null);
				deck4.setImage(null);
			}
			if (incomingDeck.size() >= 3) {
				deck3.setImage(new Image(incomingDeck.get(2).path));
			} else {
				deck3.setImage(null);
				deck4.setImage(null);
			}
			if (incomingDeck.size() >= 4) {
				deck4.setImage(new Image(incomingDeck.get(3).path));
			} else {
				deck4.setImage(null);
			}
		} else {
			deck1.setImage(null);
			deck2.setImage(null);
			deck3.setImage(null);
			deck4.setImage(null);
		}
	}
	
	public void depotInteraction(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		//System.out.println("new depot reply: initial = " + initialMove + ", confirm = " + confirmationAvailable + ", valid = " + inputValid + ":");
		updateDepots(depot);
		confirmationDepot.setVisible(true);
		confirmationDepot.setDisable(!confirmationAvailable);
		invalidMove.setVisible(!inputValid);
		
		// drag and drop functionality
		if (initialMove) { //activates drag and drop
			deckContainer.setVisible(true);
			
			ImageView[] decks = new ImageView[] {deck1, deck2, deck3, deck4};
			for (int i = 1; i <= 4; i++) {
				if (depot.getIncomingResources().size() >= i) {
					dragOrigin(decks[i-1], i + "deck");
				}
				dragDestination(decks[i-1], true, i + "deck");
			}
			
			ImageView[] pyramid = new ImageView[] {depot1, depot2, depot3, depot4, depot5, depot6};
			for (int i = 1; i <= 6; i++) {
				dragOrigin(pyramid[i-1], i + "depot");
				dragDestination(pyramid[i-1], false, i + "depot");
			}
			
			confirmationDepot.setOnMouseClicked(event -> {
				invalidMove.setVisible(false);
				deckContainer.setVisible(false);
				confirmationDepot.setVisible(false);
				deck1.setImage(null);
				deck2.setImage(null);
				deck3.setImage(null);
				deck4.setImage(null);
				notifyObserver(obs -> obs.onUpdateDepotMove("", "", 0, 0, true));
				
				//TODO: disable drag and drop functionality among the deck and depot after user confirmation
				for (int i = 1; i <= 6; i++) {
					disableDragAndDrop(pyramid[i-1]);
				}
				for (int i = 1; i <= 4; i++) {
					disableDragAndDrop(decks[i-1]);
				}
			});
		}
		
	}
	
	private void dragOrigin(ImageView image, String origin) {
		image.setOnDragDetected(event -> {
			Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(origin);
			db.setContent(content);
		});
		image.setOnMouseEntered(this::onMouseHover);
	}
	
	private void dragDestination(ImageView image, boolean notFromDecks, String destination) {
		image.setOnDragOver(event -> {
			if (notFromDecks) {
				if (event.getGestureSource() != deck1 && event.getGestureSource() != deck2 && event.getGestureSource() != deck3
						&& event.getGestureSource() != deck4 && event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			} else {
				if (event.getGestureSource() != image && event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
			event.consume();
		});
		image.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			event.setDropCompleted(true);
			event.consume();
			notifyMove(db.getString(), destination);
		});
	}
	
	private void onMouseHover(MouseEvent event) {
		((Node) event.getSource()).setCursor(Cursor.HAND);
	}
	
	private void notifyMove(String origin, String destination) {
		int originPos = Character.getNumericValue(origin.charAt(0));
		String fromWhere = origin.substring(1);
		int destinationPos = Character.getNumericValue(destination.charAt(0));
		String toWhere = destination.substring(1);
		notifyObserver(obs -> obs.onUpdateDepotMove(fromWhere, toWhere, originPos, destinationPos, false));
	}

	private void disableDragAndDrop(ImageView image) {
		image.setOnMouseEntered(event -> ((Node) event.getSource()).setCursor(Cursor.DEFAULT));
		image.setOnDragDetected(null);
		image.setOnDragOver(null);
		image.setOnDragDropped(null);
	}
	
	private static class Coordinates {
		private final int pos;
		private final double x;
		private final double y;
		
		Coordinates(int pos, double x, double y) {
			this.pos = pos;
			this.x = x;
			this.y = y;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		public int getPosition() {
			return pos;
		}
	}
	
}
