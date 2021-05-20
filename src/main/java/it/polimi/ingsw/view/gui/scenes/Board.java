package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	// faith track marker
	@FXML private ImageView cross;
	
	ArrayList<Coordinates> coordinates;

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
		
		updateCrossCoords(0); // initial position when the game starts
		
		//TODO: buttons used for user interaction must be disabled by default, and enabled only when the user can interact with them
		
		//TODO: add incoming resources UI management and drag-and-drop functionality for using the depot
	}
	
	public void setSinglePlayerMode(boolean niBBaCross) {
		// if game mode is single player
		if (niBBaCross) cross.setImage(new Image("/assets/board/black_cross.png", 73, 64, true, false));
	}
	
	public void updateCrossCoords(int pos) {
		cross.setLayoutX(coordinates.get(pos).getX());
		cross.setLayoutY(coordinates.get(pos).getY());
	}
	
	public void activatePapalZone(int num) {
		switch (num) {
			case 1 -> papal1.setVisible(true);
			case 2 -> papal2.setVisible(true);
			case 3 -> papal3.setVisible(true);
		}
	}

	public void setStrongbox(ReducedStrongbox strongbox){
		ArrayList<Resources> content = strongbox.getContent();
		setNumerosity(Resources.COIN, ListSet.count(content, Resources.COIN));
		setNumerosity(Resources.SERVANT, ListSet.count(content, Resources.SERVANT));
		setNumerosity(Resources.SHIELD, ListSet.count(content, Resources.SHIELD));
		setNumerosity(Resources.STONE, ListSet.count(content, Resources.STONE));
	}
	
	private void setNumerosity(Resources res, int num) {
		switch (res) {
			case STONE -> numStone.setText(String.valueOf(num));
			case SHIELD -> numShield.setText(String.valueOf(num));
			case SERVANT -> numServant.setText(String.valueOf(num));
			case COIN -> numCoin.setText(String.valueOf(num));
		}
	}
	
	public void setDepot(ReducedWarehouseDepot depot) {
		Resources[] resources = depot.getDepot();
		ImageView[] images = new ImageView[]{depot1, depot2, depot3, depot4, depot5, depot6};
		for (int i = 0; i < 6; i++) {
			if (resources[i] == Resources.EMPTY) {
				images[i].setImage(null);
			} else
				images[i].setImage(new Image(resources[i].path));
		}
	}
	
	public void setCardManager(ReducedCardProductionManagement cardManager) {
		ImageView[] slot1 = new ImageView[]{img11, img12, img13};
		ImageView[] slot2 = new ImageView[]{img21, img22, img23};
		ImageView[] slot3 = new ImageView[]{img31, img32, img33};
		ArrayList<Stack<DevelopmentCard>> cards = cardManager.getCards();
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

	/**
	 * sets leader card of the player
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
			}else if (leaderCards.get(i).isDiscarded()) { // card is less bright to indicate that it has been discarded
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
	 * sets the calamaio visible if this is the first's player board
	 */
	public void setStartingPlayer(){
		calamaio.setVisible(false);
	}

	private class Coordinates {
		private double x;
		private double y;
		
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
	
}
