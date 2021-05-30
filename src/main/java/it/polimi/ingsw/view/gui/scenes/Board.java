package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
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
	private boolean producingState;

	private ArrayList<ReducedLeaderCard> leaderCards;
	
	// integer flags indicating the corresponding leader card (ordinal integer number) to the list of leaders
	private int extraDepotLeader1Activation; // = 0 if not active, = 1 if it's the first leader, =2 if it's the second leader
	private int extraDepotLeader2Activation; // = 0 if not active, = 1 if it's the first leader, =2 if it's the second leader
	
	private boolean mainActionDone; //flag needed for the end turn move
	
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
		
		// faith crosses values
		blackPosition = -1;
		redPosition = 1;
		updateCrossCoords(false, 0); // initial position when the game starts
		
		//mouse click handlers for the series of productions available on the board
		baseProduction.setOnMouseClicked(event -> productionSelectionHandler(Productions.BASE_PRODUCTION, baseProduction));
		leader1.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_1_PRODUCTION, leader1));
		leader2.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_2_PRODUCTION, leader2));

		act1.setOnMouseClicked(event -> leaderActionHandler(1,1));
		act2.setOnMouseClicked(event -> leaderActionHandler(2,1));
		dis1.setOnMouseClicked(event -> leaderActionHandler(1,2));
		dis2.setOnMouseClicked(event -> leaderActionHandler(2,2));

		// flags for the extra depot activations
		extraDepotLeader1Activation = 0;
		extraDepotLeader2Activation = 0;
		
		mainActionDone = false;
		
		leaderCards = new ArrayList<>();
		
		setActProductions(false); // disables the productions at the start
	}
	
	/**
	 * getter needed by the common board class
	 * @return true if the main action of a turn has been completed
	 */
	public boolean isMainActionDone() {
		return mainActionDone;
	}
	
	/**
	 * when the turn has ended
	 */
	public void resetMainActionDone() {
		mainActionDone = false;
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
	 * sets the inkwell visible if this is the first's player board. This method is also called when the mode is single player
	 */
	public void setStartingPlayer(){
		leftCornerImage.setVisible(true); // shows the inkwell
	}
	
	/*------------------------------------------------- SINGLE PLAYER -------------------------------------------------------------------*/
	
	/**
	 * if the mode is single player, adds a new black cross to the faith track. Also places the tokens in the same place where the inkwell is.
	 */
	public void setSinglePlayerMode() {
		leftCornerImage.setImage(new Image("/assets/token/0.png")); // applies first token
		blackCross.setVisible(true);
		updateCrossCoords(true, 0);
	}
	
	/**
	 * used for the single player mode, updates the token stack shown on the left corner of the screen
	 * @param type of token
	 * @param color of the token
	 */
	public void updateToken(TokenType type,Colors color){
		if(type.equals(TokenType.DISCARD_TOKEN))
			leftCornerImage.setImage(new Image("/assets/token/"+(color.colorNumber+1)+".png"));
		else if(type.equals(TokenType.BLACK_CROSS_TOKEN))
			leftCornerImage.setImage(new Image("/assets/token/5.png"));
		else
			leftCornerImage.setImage(new Image("/assets/token/6.png"));

	}
	
	/*-------------------------------------------------- FAITH TRACK related code -----------------------------------------------------------*/
	
	/**
	 * updates the faith track content
	 * @param track reduced class
	 */
	public void updateFaithTrack(ReducedFaithTrack track){
		updateCrossCoords(track.isLorenzosTrack(), track.getCurrentPosition());
		activatePapalZone(track.getVaticanReports(), track.getLastReportClaimed());
	}

	/**
	 * updates the position of the red cross on the faith track
	 * @param lorenzo indicating if the cross to be moved is the Lorenzo's one
	 * @param pos new position on the track
	 */
	public void updateCrossCoords(boolean lorenzo, int pos) {
		if (lorenzo) { // moving the black cross
			if (pos != blackPosition) {
				drawCrosses(redCross, blackCross, redPosition, pos);
				blackPosition = pos;
			}
		} else { // moving the red cross
			if (pos != redPosition) {
				drawCrosses(blackCross, redCross, blackPosition, pos);
				redPosition = pos;
			}
		}
	}
	
	/**
	 * utility method that handles the disableCommonBoardButtons of the positions of the crosses on the faith track.
	 * It keeps track of the position so it resizes and moves them if they happen to overlap
	 * @param c1 first cross
	 * @param c2 second cross
	 * @param crossPosition Planck's constant
	 * @param pos Avogadro's constant
	 */
	private void drawCrosses(ImageView c1, ImageView c2, int crossPosition, int pos) {
		if (redPosition == blackPosition) {
			c1.setLayoutX(coordinates.get(crossPosition).getX());
			c1.setLayoutY(coordinates.get(crossPosition).getY());
			c1.setFitWidth(73);
			c1.setFitHeight(64);
			c2.setLayoutX(coordinates.get(pos).getX());
			c2.setLayoutY(coordinates.get(pos).getY());
			c2.setFitWidth(73);
			c2.setFitHeight(64);
		} else if (crossPosition == pos) { // intercepts the red cross
			c1.setFitWidth(52);
			c1.setFitHeight(46);
			c2.setLayoutX(coordinates.get(pos).getX() + 27);
			c2.setLayoutY(coordinates.get(pos).getY() + 24);
			c2.setFitWidth(52);
			c2.setFitHeight(46);
		} else {
			c2.setLayoutX(coordinates.get(pos).getX());
			c2.setLayoutY(coordinates.get(pos).getY());
			c2.setFitWidth(73);
			c2.setFitHeight(64);
		}
	}
	
	/**
	 * sets the visibility of the vatican reports
	 * @param vaticanReports arraylist of vatican reports
	 */
	public void activatePapalZone(ArrayList<Boolean> vaticanReports, int lastReportClaimed) {
		if(papal1.getImage() == null) {
			if (vaticanReports.get(0)) {
				papal1.setImage(new Image("/assets/board/vatican_1.png"));
				papal1.setVisible(true);
			}
			else if (lastReportClaimed > 0){
				papal1.setImage(new Image("/assets/board/vatican_1_failed.png"));
				papal1.setVisible(true);
			}
		}
		if(papal2.getImage() == null) {
			if (vaticanReports.get(1)) {
				papal2.setImage(new Image("/assets/board/vatican_2.png"));
				papal2.setVisible(true);
			}
			else if (lastReportClaimed > 1){
				papal2.setImage(new Image("/assets/board/vatican_2_failed.png"));
				papal2.setVisible(true);
			}
		}
		if(papal3.getImage() == null) {
			if (vaticanReports.get(2)) {
				papal3.setImage(new Image("/assets/board/vatican_3.png"));
				papal3.setVisible(true);
			}
			else if (lastReportClaimed > 2) {
				papal3.setImage(new Image("/assets/board/vatican_3_failed.png"));
				papal3.setVisible(true);
			}
		}
	}
	
	/*------------------------------------------------------DEV CARDS AND PRODUCTIONS --------------------------------------------------*/
	
	/**
	 * changes the production status
	 * @param value activate or deactivate the possibility to make productions
	 */
	public void setActProductions(boolean value) {
		actProductions.setVisible(value);
		actProductions.setDisable(!value);
	}
	
	/**
	 * when the user clicks on the button for doing the productions
	 */
	public void activateProduction(){
		if(actProductions.getText().equals("Activate Productions")) { // pressed activation
			notifyObserver(obs -> obs.onUpdateAction(PlayerActions.PRODUCTIONS));
			actProductions.setDisable(true);
			actProductions.setText("Confirm Productions");
			producingState = true;
		} else { // pressed confirmation
			notifyObserver(obs->obs.onUpdateProductionAction(new ArrayList<>(selectedProduction)));
			selectedProduction.clear();
			actProductions.setText("Activate Productions");
			actProductions.setDisable(true);
			producingState = false;
			setNormalLuminosity();
			mainActionDone = true;
		}
	}
	
	/**
	 * mouse click handler for the productions on the main board
	 * @param production selected by the user
	 * @param image where the user clicked
	 */
	public void productionSelectionHandler(Productions production, ImageView image) {
		if(producingState && availableProduction.contains(production)) {
			if (!selectedProduction.contains(production)) {
				image.setEffect(new Glow(0.5));
				selectedProduction.add(production);
				actProductions.setDisable(false);
			} else {
				setShadow(image);
				selectedProduction.remove(production);
				if(selectedProduction.size()==0)
					actProductions.setDisable(true);
			}
		}
	}
	
	/**
	 * sets a shadow where a production can be done
	 * @param productions list of available productions sent from the server
	 */
	public void showAvailableProductions(ArrayList<Productions> productions){
		availableProduction = productions;
		if (productions.contains(Productions.BASE_PRODUCTION))
			setShadow(baseProduction);
		if (productions.contains(Productions.STACK_1_CARD_PRODUCTION)) {
			setShadow(slot1[sizeSlot1-1]);
		}
		if (productions.contains(Productions.STACK_2_CARD_PRODUCTION)){
			setShadow(slot2[sizeSlot2-1]);
		}
		if (productions.contains(Productions.STACK_3_CARD_PRODUCTION)){
			setShadow(slot3[sizeSlot3-1]);
		}
		if (productions.contains(Productions.LEADER_CARD_1_PRODUCTION)) { // first extra depot production
			setShadow(leader1);
		}
		if (productions.contains(Productions.LEADER_CARD_2_PRODUCTION)){ // second extra depot production
			setShadow(leader2);
		}
	}
	
	/**
	 * sets a nice looking shadow on an image, with default green-ish color
	 * @param img to be applied
	 */
	public void setShadow(ImageView img){
		setShadow(img, false);
	}
	
	/**
	 * sets a nice looking shadow on an image, with a flag specific for the activated leaders
	 * @param img generic image
	 * @param leaderActive if it refers to a leader card image
	 */
	public void setShadow(ImageView img, boolean leaderActive) {
		DropShadow dropShadow= new DropShadow();
		if (!leaderActive) {
			dropShadow.setSpread(0.5);
			dropShadow.setColor(Color.SPRINGGREEN);
		} else {
			dropShadow.setSpread(0.9);
			dropShadow.setColor(Color.GOLD);
		}
		img.setEffect(dropShadow);
	}
	
	public void setProducingState(boolean producingState) {
		this.producingState = producingState;
	}
	
	/**
	 * applies normal luminosity to all production-based elements
	 */
	public void setNormalLuminosity(){
		baseProduction.setEffect(new Glow(0));
		if(!leaderCards.get(0).isDiscarded())
			leader1.setEffect(new Glow(0));
		if(!leaderCards.get(1).isDiscarded())
			leader2.setEffect(new Glow(0));
		
		if(sizeSlot1>0) slot1[sizeSlot1-1].setEffect(new Glow(0));
		if(sizeSlot2>0)	slot2[sizeSlot2-1].setEffect(new Glow(0));
		if(sizeSlot3>0) slot3[sizeSlot3-1].setEffect(new Glow(0));
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
				mainActionDone = true;
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
				mainActionDone = true;
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
				mainActionDone = true;
			}
			for (int i = 0; i < sizeSlot3 - 1; i++) {
				slot3[i].setOnMouseClicked(null);
			}
		}
		
	}
	
	
	/*----------------------------------------------------------- LEADER CARDS ----------------------------------------------------*/
	
	/**
	 * sets leader card of the player. Also activates or deactivates the buttons relative to the leader cards based on the game status and
	 * who is the player in this board.
	 * @param leaderCards player's leader cards
	 */
	public void setMyLeaderCards(ArrayList<ReducedLeaderCard> leaderCards) {
		setMyLeaderCards(leaderCards.get(0), act1, dis1, leader1);
		setMyLeaderCards(leaderCards.get(1), act2, dis2, leader2);
		this.leaderCards=leaderCards;

	}
	
	/**
	 * updates the leader card images and the buttons according to the available actions
	 * @param leaderCard reduced class
	 * @param act activation button
	 * @param dis discard button
	 * @param leader image of the leader
	 */
	public void setMyLeaderCards(ReducedLeaderCard leaderCard, Button act, Button dis, ImageView leader) {
		if (leaderCard.isPlayable()) {
			act.setDisable(false);
			dis.setDisable(false);
		} else if (leaderCard.isAbilitiesActivated()) {
			act.setDisable(true);
			dis.setDisable(true);
			setShadow(leader, true);
		} else if (leaderCard.isDiscarded()) {
			// card is less bright to indicate that it has been discarded
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(-0.5);
			leader.setEffect(colorAdjust);
			act.setDisable(true);
			dis.setDisable(true);
		} else if (this.leaderCards.isEmpty()) {

			leader.setImage(new Image("/assets/leaderCards/" + leaderCard.getIdNumber() + ".png"));
			act.setDisable(true);
			dis.setDisable(true);
		} else {
			dis.setDisable(false);
		}
	}
	
	/**
	 * disables the leaders interaction buttons when the current turn ends
	 */
	public void setEndTurn() {
		act1.setDisable(true);
		act2.setDisable(true);
		dis1.setDisable(true);
		dis2.setDisable(true);
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
	
	/**
	 * handles the click on a leader related button
	 * @param card 1 or 2, representing the ordinal number of the leader card
	 * @param action 1 -> activation; 2 -> discarding
	 */
	public void leaderActionHandler(int card, int action) {
		notifyObserver(obs -> obs.onUpdateLeaderAction(card,action));
		
		if (action == 1) { // leader card activation
			//extra depot leader card check
			if (leaderCards.get(card - 1).getIdNumber() >= 5 && leaderCards.get(card - 1).getIdNumber() <= 8) {
				if (extraDepotLeader1Activation > 0) {
					extraDepotLeader2Activation = card;
				} else {
					extraDepotLeader1Activation = card;
				}
			}
		}
	}
	
	
	
	/*-------------------------------------------------- DEPOT and STRONGBOX related code ----------------------------------------------*/
	
	/**
	 * updates the strongbox contents
	 * @param strongbox reduced class
	 */
	public void updateStrongbox(ReducedStrongbox strongbox) {
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
	 * Updates the images on the warehouse depots and the additional depots (if the relative ability is activated).
	 * Sets the incoming resources deposit if there are any and makes it visible.
	 * @param depot reduced class containing the incoming resources, warehouse depots and additional depots
	 */
	public void updateDepots(ReducedWarehouseDepot depot) {
		// updates the warehouse depots
		Resources[] resources = depot.getDepot();
		ImageView[] images = new ImageView[]{depot1, depot2, depot3, depot4, depot5, depot6};
		for (int i = 0; i < 6; i++) {
			if (resources[i] == Resources.EMPTY) {
				images[i].setImage(null);
			} else {
				images[i].setImage(new Image(resources[i].path));
			}
		}
		
		//updates the additional depots if they're set and the abilities are activated (only if the image hasn't been set yet)
		if (depot.isLeaderActivated(0)) {
			changeExtraDepotImage(1, 1, depot.getExtraDepotContents().get(0).get(0),
					depot.getExtraDepotResources().get(0).get(0).path);
			changeExtraDepotImage(1, 2, depot.getExtraDepotContents().get(0).get(1),
					depot.getExtraDepotResources().get(0).get(1).path);
		}
		if (depot.isLeaderActivated(1)) {
			changeExtraDepotImage(2, 1, depot.getExtraDepotContents().get(1).get(0),
					depot.getExtraDepotResources().get(1).get(0).path);
			changeExtraDepotImage(2, 2, depot.getExtraDepotContents().get(1).get(1),
					depot.getExtraDepotResources().get(1).get(1).path);
		}
		
		// disableCommonBoardButtons incoming resource deck contents. Deck must not contain any empty resources
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
	
	/**
	 * changes the images corresponding to the resources in the extra depot slots
	 * @param leaderNumber 1 or 2
	 * @param number of the resources in order (1 or 2)
	 * @param present if the resource is present or not
	 * @param imagePath image resource to be assigned
	 */
	private void changeExtraDepotImage(int leaderNumber, int number, boolean present, String imagePath) {
		ImageView[][] matrix = new ImageView[][] {{res11, res12}, {res21, res22}};
		int extra = (leaderNumber == 1 ? extraDepotLeader1Activation : extraDepotLeader2Activation);
		if (present) {
			matrix[extra - 1][number - 1].setImage(new Image(imagePath));
		} else {
			matrix[extra - 1][number - 1].setImage(null);
		}
	}
	
	/**
	 * handles depot interaction and drag and drop functionality
	 * @param depot sent from the server
	 * @param initialMove flag for setting the drag and drops
	 * @param confirmationAvailable enables the confirmation button
	 * @param inputValid if the user input was invalid
	 */
	public void depotInteraction(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
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
				
				for (int i = 1; i <= 6; i++) {
					disableDragAndDrop(pyramid[i-1]);
				}
				for (int i = 1; i <= 4; i++) {
					disableDragAndDrop(decks[i-1]);
				}
				
				mainActionDone = true;
			});
		}
		
	}
	
	/**
	 * applies the drag functionality on an imageview
	 * @param image where to start dragging
	 * @param origin text representing the value to drop
	 */
	private void dragOrigin(ImageView image, String origin) {
		image.setOnDragDetected(event -> {
			Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(origin);
			db.setContent(content);
		});
		image.setOnMouseEntered(this::onMouseHover);
	}
	
	/**
	 * applies the drop functionality on an imageview
	 * @param image where to drop the text
	 * @param notFromDecks if the drag cannot arrive from the resources deck positions
	 * @param destination text representing the drop area
	 */
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
	
	/**
	 * applies a hand cursor where the user can start dragging
	 * @param event of hovering
	 */
	private void onMouseHover(MouseEvent event) {
		((Node) event.getSource()).setCursor(Cursor.HAND);
	}
	
	/**
	 * tells the server which move the user did
	 * @param origin text representing the place of origin of the movement
	 * @param destination text representing the place of destination of the movement
	 */
	private void notifyMove(String origin, String destination) {
		int originPos = Character.getNumericValue(origin.charAt(0));
		String fromWhere = origin.substring(1);
		int destinationPos = Character.getNumericValue(destination.charAt(0));
		String toWhere = destination.substring(1);
		notifyObserver(obs -> obs.onUpdateDepotMove(fromWhere, toWhere, originPos, destinationPos, false));
	}
	
	/**
	 * disables drag and drop functionality when the user clicked confirm
	 * @param image where to disable everything
	 */
	private void disableDragAndDrop(ImageView image) {
		image.setOnMouseEntered(event -> ((Node) event.getSource()).setCursor(Cursor.DEFAULT));
		image.setOnDragDetected(null);
		image.setOnDragOver(null);
		image.setOnDragDropped(null);
	}
	
	/**
	 * utility record class for the memorization of the positions of the crosses on the faith track
	 */
	private record Coordinates(int pos, double x, double y) {
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
	}
	
}
