package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.WarehouseDepot;
import it.polimi.ingsw.model.reducedClasses.ReducedCardProductionManagement;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedStrongbox;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.gui.ResourcesGui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Board extends ViewObservable implements SceneController {
	
	
	@FXML private ImageView papal1;
	@FXML private ImageView papal2;
	@FXML private ImageView papal3;
	@FXML private ImageView calamaio;
	
	@FXML private ImageView img11;
	@FXML private ImageView img12;
	@FXML private ImageView img13;
	@FXML private ImageView img21;
	@FXML private ImageView img22;
	@FXML private ImageView img23;
	@FXML private ImageView img31;
	@FXML private ImageView img32;
	@FXML private ImageView img33;
	
	
	@FXML private ImageView leader1;
	@FXML private ImageView leader2;
	@FXML private Label lblLeader2;
	@FXML private Label lblLeader1;
	
	@FXML private Label numCoin;
	@FXML private Label numStone;
	@FXML private Label numShield;
	@FXML private Label numServant;
	
	@FXML private ImageView depot1;
	@FXML private ImageView depot2;
	@FXML private ImageView depot3;
	@FXML private ImageView depot4;
	@FXML private ImageView depot5;
	@FXML private ImageView depot6;
	
	@FXML private ImageView cross;
	
	ArrayList<Coordinates> coordinates;
	
	@FXML
	public void initialize() {
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 40);
		
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
		
	}
	
	public void setStartingPlayer(boolean hasCalamaio) {
		// if the player is the starting one
		calamaio.setVisible(hasCalamaio);
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
	
	public void setSlotImage(int slot, int cardNumber) {
	
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
			if (resources[i] != Resources.EMPTY) {
				images[i].setImage(null);
				System.gc();
			} else
				images[i].setImage(new Image(resources[i].path));
		}
	}
	
	public void setCardManager(ReducedCardProductionManagement cardManager) {
	
	}
	
	public void setLeaderCards(ArrayList<ReducedLeaderCard> leaderCards) {
		ImageView[] images = new ImageView[]{leader1, leader2};
		for (int i = 0; i < leaderCards.size(); i++) {
			if (leaderCards.get(i).isPlayable()) {
				
				//TODO: set visibility for button
				
			}
			if (leaderCards.get(i).isDiscarded()) {
				images[i].setImage(null);
				//System.gc();
			} else {
				images[i].setImage(new Image("/assets/leaderCards/" +
						leaderCards.get(i).getIdNumber() + ".png"));
			}
		}
	}
	
	
	private class Coordinates {
		private int pos;
		private double x;
		private double y;
		
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
	}
	
}
