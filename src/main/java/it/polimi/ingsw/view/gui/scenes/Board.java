package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class Board extends ViewObservable implements SceneController {
	
	
	@FXML private BorderPane slot1;
	@FXML private ImageView img1;
	@FXML private BorderPane slot2;
	@FXML private ImageView img2;
	@FXML private BorderPane slot3;
	@FXML private ImageView img3;
	@FXML private ImageView leader1;
	@FXML private ImageView leader2;
	@FXML private Label lblLeader2;
	@FXML private Label numCoin;
	@FXML private Label numStone;
	@FXML private Label numShield;
	@FXML private Label numServant;
	@FXML private Label lblLeader1;
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
		//Font caveat = Font.loadFont(getClass().getResourceAsStream("/assets/font/Harrington.ttf"), 40);
		
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
		updateCrossCoords(0);
		
		// if game mode is single player
		cross.setImage(new Image("/assets/board/black_cross.png", 73, 64, true, false));
	}
	
	public void updateCrossCoords(int pos) {
		cross.setLayoutX(coordinates.get(pos).getX());
		cross.setLayoutY(coordinates.get(pos).getY());
	}
	
	public void setSlotImage(int slot, int cardNumber) {
	
	}
	
	public void setNumerosity(Resources res, int num) {
		switch (res) {
			case STONE -> numStone.setText(String.valueOf(num));
			case SHIELD -> numShield.setText(String.valueOf(num));
			case SERVANT -> numServant.setText(String.valueOf(num));
			case COIN -> numCoin.setText(String.valueOf(num));
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
