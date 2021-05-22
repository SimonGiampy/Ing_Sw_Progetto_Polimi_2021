package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
	@FXML private ImageView calamaio;
	
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
	@FXML private ImageView cross;

	// image representing the base production, serves as clicking spot
	@FXML private ImageView baseProduction;
	
	private ArrayList<Coordinates> coordinates; // list of coordinates for the cross in the faith track
	
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

		slot1 = new ImageView[]{img11, img12, img13};
		slot2 = new ImageView[]{img21, img22, img23};
		slot3 = new ImageView[]{img31, img32, img33};
		selectedProduction=new ArrayList<>();
		sizeSlot1=0;
		sizeSlot2=0;
		sizeSlot3=0;
		
		updateCrossCoords(0); // initial position when the game starts
		
		baseProduction.setOnMouseClicked(event -> productionSelectionHandler(Productions.BASE_PRODUCTION, baseProduction));
		leader1.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_1_PRODUCTION, leader1));
		leader2.setOnMouseClicked(event -> productionSelectionHandler(Productions.LEADER_CARD_2_PRODUCTION, leader2));
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			slot1[i].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_1_CARD_PRODUCTION, slot1[finalI]));
			slot2[i].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_2_CARD_PRODUCTION, slot2[finalI]));
			slot3[i].setOnMouseClicked(event -> productionSelectionHandler(Productions.STACK_3_CARD_PRODUCTION, slot3[finalI]));
		}
		
	}
	
	/**
	 * if the mode is single player, adds a new black cross to the faith track. Also places the tokens in the same place where the inkwell is.
	 * @param niBBaCross flag indicating if the game mode is single player
	 */
	public void setSinglePlayerMode(boolean niBBaCross) {
		// TODO: change this so that it shows the token stack in the inkwell's position
		// TODO: add the black cross for Lorenzo's track
		if (niBBaCross) cross.setImage(new Image("/assets/board/black_cross.png", 73, 64, true, false));
	}
	
	/**
	 * updates the position of the red cross on the faith track
	 * @param pos new position on the track
	 */
	public void updateCrossCoords(int pos) {
		//TODO: if the single player mode is active, then the black cross is added. And whether the positions of both crosses are
		//      equal, then this function must reduce the sizes of both images so they can fit the same cell at the same time.
		//      Add a new parameter to this function so that it knows which cross to consider when updating its position.
		cross.setLayoutX(coordinates.get(pos).getX());
		cross.setLayoutY(coordinates.get(pos).getY());
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
		//TODO: there is 100% a bug where if the leader activated with the additional depots is the second one (visually) then
		//      the resources will be put over the first leader image. This is because the Model treats the leaders activated regardless
		//      of the order in which the leaders are displayed.
		//  Solution: use variables to identify which leader has been activated and sets the corresponding images accordingly
		if (depot.isLeaderActivated(0)) {
			if (depot.getExtraDepotContents().get(0).get(0)) {
				if (res11.getImage() == null) {
					res11.setImage(new Image(depot.getExtraDepotResources().get(0).get(0).path));
				}
			}
			if (depot.getExtraDepotContents().get(0).get(1)) {
				if (res12.getImage() == null) {
					res12.setImage(new Image(depot.getExtraDepotResources().get(0).get(1).path));
				}
			}
		}
		if (depot.isLeaderActivated(1)) {
			if (depot.getExtraDepotContents().get(1).get(0)) {
				if (res21.getImage() == null) {
					res21.setImage(new Image(depot.getExtraDepotResources().get(1).get(0).path));
				}
			}
			if (depot.getExtraDepotContents().get(1).get(1)) {
				if (res22.getImage() == null) {
					res22.setImage(new Image(depot.getExtraDepotResources().get(1).get(1).path));
				}
			}
		}
		
		// update incoming resource deck contents. Deck must not contain any empty resources
		ArrayList<Resources> incomingDeck = depot.getIncomingResources();
		if (!incomingDeck.isEmpty()) {
			deckContainer.setVisible(true);
			if (incomingDeck.size() >= 1) {
				deck1.setImage(new Image(incomingDeck.get(0).path));
			} else {
				deck1.setImage(null);
				deck2.setImage(null);
				deck3.setImage(null);
				deck4.setImage(null);
			}
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
			deckContainer.setVisible(false);
		}
	}
	
	/**
	 * updates the development cards in the slots, setting the images if not set yet.
	 * @param cardManager reduces class containing the stacks of the dev cards on the player board.
	 */
	public void updateDevCardsSlots(ReducedCardProductionManagement cardManager) {

		ArrayList<Stack<DevelopmentCard>> cards = cardManager.getCards();
		sizeSlot1=cards.get(0).size();
		sizeSlot2=cards.get(1).size();
		sizeSlot3=cards.get(2).size();
		for(int i = 0; i < cards.get(0).size(); i++){
			if(slot1[i].getImage() == null){
				slot1[i].setImage(new Image("/assets/devCards/" + cards.get(0).get(i).getCardNumber() + ".png"));
			}
		}
		for(int i = 0; i < cards.get(1).size(); i++){
			if(slot2[i].getImage() == null){
				slot2[i].setImage(new Image("/assets/devCards/" + cards.get(1).get(i).getCardNumber() + ".png"));
			}
		}
		for(int i = 0; i < cards.get(2).size(); i++){
			if(slot3[i].getImage() == null){
				slot3[i].setImage(new Image("/assets/devCards/" + cards.get(2).get(i).getCardNumber() + ".png"));
			}
		}
	}

	public void onMouseClickDis1(){
		notifyObserver(obs->obs.onUpdateLeaderAction(1,2));
	}

	public void onMouseClickDis2(){
		notifyObserver(obs->obs.onUpdateLeaderAction(2,2));
	}

	public void onMouseClickAct1(){
		notifyObserver(obs->obs.onUpdateLeaderAction(1,1));
	}

	public void onMouseClickAct2(){
		notifyObserver(obs->obs.onUpdateLeaderAction(2,2));
	}

	/**
	 * sets leader card of the player. Also activates or deactivates the buttons relative to the leader cards based on the game status and
	 * who is the player in this board.
	 * @param leaderCards player's leader cards
	 */
	public void setMyLeaderCards(ArrayList<ReducedLeaderCard> leaderCards) {
		ImageView[] images = new ImageView[]{leader1, leader2};
		for (int i = 0; i < leaderCards.size(); i++) {
			if (leaderCards.get(i).isPlayable()) {
				if (i == 0) {
					act1.setDisable(false);
					dis1.setDisable(false);
				} else if (i == 1) {
					act2.setDisable(false);
					dis2.setDisable(false);
				}
			} else if(leaderCards.get(i).isAbilitiesActivated()){
				if(i==0) {
					act1.setDisable(true);
					dis1.setDisable(true);
				}else {
					act2.setDisable(true);
					dis2.setDisable(true);
				}
			} else if (leaderCards.get(i).isDiscarded()) { // card is less bright to indicate that it has been discarded
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.5);
				images[i].setEffect(colorAdjust);
				if(i==0) {
					act1.setDisable(true);
					dis1.setDisable(true);
				} else {
					act2.setDisable(true);
					dis2.setDisable(true);
				}
			} else {
				images[i].setImage(new Image("/assets/leaderCards/" + leaderCards.get(i).getIdNumber() + ".png"));
				act1.setDisable(true);
				act2.setDisable(true);
				dis1.setDisable(false);
				dis2.setDisable(false);
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
		calamaio.setVisible(true);
	}

	private static class Coordinates {
		private final double x;
		private final double y;
		
		Coordinates(int pos, double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
	}

	public void setActProductions(boolean value){
		actProductions.setVisible(value);
	}

	public void activateProduction(){
		if(actProductions.getText().equals("Activate Productions")) {
			notifyObserver(obs -> obs.onUpdateAction(PlayerActions.PRODUCTIONS));
			actProductions.setVisible(false);
			actProductions.setText("Confirm Productions");
		}
		else{
			notifyObserver(obs->obs.onUpdateProductionAction(selectedProduction));
			actProductions.setText("Activate Productions");
			actProductions.setVisible(false);
			productionAble=false;
			setNormal();
		}
	}
	
	public void productionSelectionHandler(Productions production, ImageView image) {
		if(productionAble && availableProduction.contains(production)) {
			if (!selectedProduction.contains(production)) {
				image.setEffect(new Glow(0.5));
				selectedProduction.add(production);
				actProductions.setVisible(true);
			} else {
				setShadow(0.5, image);
				selectedProduction.remove(production);
				if(selectedProduction.size()==0)
					actProductions.setVisible(false);
			}
		}
	}

	/*
	public void onMouseClickedBaseProduction(){
		if(productionAble && availableProduction.contains(Productions.BASE_PRODUCTION)) {
			if (!selectedProduction.contains(Productions.BASE_PRODUCTION)) {
				baseProduction.setEffect(new Glow(0.5));
				selectedProduction.add(Productions.BASE_PRODUCTION);
				actProductions.setVisible(true);
			} else {
				setShadow(0.5,baseProduction);
				selectedProduction.remove(Productions.BASE_PRODUCTION);
				if(selectedProduction.size()==0)
					actProductions.setVisible(false);
			}
		}
	}

	public void onMouseClickedDevCard1(){
		if(productionAble && availableProduction.contains(Productions.STACK_1_CARD_PRODUCTION)){
			if(!selectedProduction.contains(Productions.STACK_1_CARD_PRODUCTION)){
				slot1[sizeSlot1-1].setEffect(new Glow(0.5));
				selectedProduction.add(Productions.STACK_1_CARD_PRODUCTION);
				actProductions.setVisible(true);
			}
			else {
				setShadow(0.5,slot1[sizeSlot1-1]);
				selectedProduction.remove(Productions.STACK_1_CARD_PRODUCTION);
				if(selectedProduction.size()==0){
					actProductions.setVisible(false);
				}
			}
		}
	}

	public void onMouseClickedDevCard2(){
		if(productionAble && availableProduction.contains(Productions.STACK_2_CARD_PRODUCTION)){
			if(!selectedProduction.contains(Productions.STACK_2_CARD_PRODUCTION)){
				slot2[sizeSlot2-1].setEffect(new Glow(0.5));
				selectedProduction.add(Productions.STACK_2_CARD_PRODUCTION);
				actProductions.setVisible(true);
			}
			else {
				setShadow(0.5,slot2[sizeSlot2-1]);
				selectedProduction.remove(Productions.STACK_2_CARD_PRODUCTION);
				if(selectedProduction.size()==0){
					actProductions.setVisible(false);
				}
			}
		}
	}

	public void onMouseClickedDevCard3(){
		if(productionAble && availableProduction.contains(Productions.STACK_3_CARD_PRODUCTION)){
			if(!selectedProduction.contains(Productions.STACK_3_CARD_PRODUCTION)){
				slot3[sizeSlot3-1].setEffect(new Glow(0.5));
				selectedProduction.add(Productions.STACK_3_CARD_PRODUCTION);
				actProductions.setVisible(true);
			}
			else {
				setShadow(0.5,slot3[sizeSlot3-1]);
				selectedProduction.remove(Productions.STACK_3_CARD_PRODUCTION);
				if(selectedProduction.size()==0){
					actProductions.setVisible(false);
				}
			}
		}
	}

	public void onMouseClickedLeaderCard1(){
		if(productionAble && availableProduction.contains(Productions.LEADER_CARD_1_PRODUCTION)){
			if(!selectedProduction.contains(Productions.LEADER_CARD_1_PRODUCTION)){
				leader1.setEffect(new Glow(0.5));
				selectedProduction.add(Productions.LEADER_CARD_1_PRODUCTION);
				actProductions.setVisible(true);
			}
			else {
				setShadow(0.5,leader1);
				selectedProduction.remove(Productions.LEADER_CARD_1_PRODUCTION);
				if(selectedProduction.size()==0){
					actProductions.setVisible(false);
				}
			}
		}
	}

	public void onMouseClickedLeaderCard2(){
		if(productionAble && availableProduction.contains(Productions.LEADER_CARD_2_PRODUCTION)){
			if(!selectedProduction.contains(Productions.LEADER_CARD_2_PRODUCTION)){
				leader2.setEffect(new Glow(0.5));
				selectedProduction.add(Productions.LEADER_CARD_2_PRODUCTION);
				actProductions.setVisible(true);
			}
			else {
				setShadow(0.5,leader2);
				selectedProduction.remove(Productions.LEADER_CARD_2_PRODUCTION);
				if(selectedProduction.size()==0){
					actProductions.setVisible(false);
				}
			}
		}
	}
	 */

	public void setAvailableProductionGreen(ArrayList<Productions> productions){
		availableProduction=productions;
		if(productions.contains(Productions.BASE_PRODUCTION))
			setShadow(0.5,baseProduction);
		if(productions.contains(Productions.STACK_1_CARD_PRODUCTION)) {
			int i= sizeSlot1;
			setShadow(0.5,slot1[i-1]);
		}
		if(productions.contains(Productions.STACK_2_CARD_PRODUCTION)){
			int i= sizeSlot2;
			setShadow(0.5,slot2[i-1]);
		}
		if(productions.contains(Productions.STACK_3_CARD_PRODUCTION)){
			int i= sizeSlot3;
			setShadow(0.5,slot3[i-1]);
		}
		if(productions.contains(Productions.LEADER_CARD_1_PRODUCTION)){
			setShadow(0.5,leader1);
		}
		if(productions.contains(Productions.LEADER_CARD_2_PRODUCTION)){
			setShadow(0.5,leader2);
		}

	}

	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.GREEN);
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
	
	
	public void depotInteraction(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		updateDepots(depot);
		
		confirmationDepot.setVisible(true);
		confirmationDepot.setDisable(confirmationAvailable);
		invalidMove.setVisible(inputValid);
		
		
	}
}
