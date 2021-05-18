package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class CommonBoard extends ViewObservable implements SceneController{

	@FXML
	GridPane marketGUI;

	@FXML ImageView market00;
	@FXML ImageView market01;
	@FXML ImageView market02;
	@FXML ImageView market03;
	@FXML ImageView market10;
	@FXML ImageView market11;
	@FXML ImageView market12;
	@FXML ImageView market13;
	@FXML ImageView market20;
	@FXML ImageView market21;
	@FXML ImageView market22;
	@FXML ImageView market23;
	@FXML ImageView extraBall;

	ImageView[][] marketImages;

	@FXML ImageView cards00;
	@FXML ImageView cards01;
	@FXML ImageView cards02;
	@FXML ImageView cards03;
	@FXML ImageView cards10;
	@FXML ImageView cards11;
	@FXML ImageView cards12;
	@FXML ImageView cards13;
	@FXML ImageView cards20;
	@FXML ImageView cards21;
	@FXML ImageView cards22;
	@FXML ImageView cards23;

	ImageView[][] cardsImages;


	@FXML
	public void initialize(){

		cardsImages= new ImageView[3][4];
		cardsImages[0][0]= cards00;
		cardsImages[0][1]= cards01;
		cardsImages[0][2]= cards02;
		cardsImages[0][3]= cards03;
		cardsImages[1][0]= cards10;
		cardsImages[1][1]= cards11;
		cardsImages[1][2]= cards12;
		cardsImages[1][3]= cards13;
		cardsImages[2][0]= cards20;
		cardsImages[2][1]= cards21;
		cardsImages[2][2]= cards22;
		cardsImages[2][3]= cards23;

		marketImages= new ImageView[3][4];
		marketImages[0][0]= market00;
		marketImages[0][1]= market01;
		marketImages[0][2]= market02;
		marketImages[0][3]= market03;
		marketImages[1][0]= market10;
		marketImages[1][1]= market11;
		marketImages[1][2]= market12;
		marketImages[1][3]= market13;
		marketImages[2][0]= market20;
		marketImages[2][1]= market21;
		marketImages[2][2]= market22;
		marketImages[2][3]= market23;




	}

	public void setMarket(ReducedMarket market){
		Marbles marble;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				marble=market.getMarket()[i][j];
				marketImages[i][j].setImage( new Image(marble.path));
			}

		}
		marble=market.getExtraBall();
		extraBall.setImage(new Image(marble.path));
	}

	public void setDeck(ReducedDevelopmentCardsDeck deck){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				DevelopmentCard card=deck.getCardStackStructure()[i][j].get(0);
				cardsImages[i][j].setImage(new Image("/assets/devCards/"+card.getCardNumber()+".png"));

			}
		}
	}


}
