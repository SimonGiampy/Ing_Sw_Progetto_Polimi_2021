package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

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
	
	
	@FXML
	public void initialize() {
		System.out.println(getClass().getClassLoader().getResource("assets/font/Caveat-Regular.ttf").toString());
		Font caveat = Font.loadFont(getClass().getClassLoader().getResourceAsStream("assets/font/Caveat-Regular.ttf"), 40);
		numCoin.setFont(caveat);
	}
	
	
}
