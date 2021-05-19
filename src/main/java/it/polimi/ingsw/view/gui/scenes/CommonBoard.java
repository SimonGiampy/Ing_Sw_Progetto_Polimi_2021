package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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


	@FXML ImageView row1;
	@FXML ImageView row2;
	@FXML ImageView row3;

	@FXML ImageView normalRow1;
	@FXML ImageView normalRow2;
	@FXML ImageView normalRow3;


	@FXML ImageView col1;
	@FXML ImageView col2;
	@FXML ImageView col3;
	@FXML ImageView col4;

	@FXML ImageView normalCol1;
	@FXML ImageView normalCol2;
	@FXML ImageView normalCol3;
	@FXML ImageView normalCol4;

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

	@FXML ImageView greenCards00;
	@FXML ImageView greenCards01;
	@FXML ImageView greenCards02;
	@FXML ImageView greenCards03;
	@FXML ImageView greenCards10;
	@FXML ImageView greenCards11;
	@FXML ImageView greenCards12;
	@FXML ImageView greenCards13;
	@FXML ImageView greenCards20;
	@FXML ImageView greenCards21;
	@FXML ImageView greenCards22;
	@FXML ImageView greenCards23;

	ImageView[][] greenCardsImages;

	private boolean marketAble; //TODO: to be set true at the start of the turn
	private ReducedDevelopmentCardsDeck deck;


	@FXML
	public void initialize(){
		marketAble=true;


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

		greenCardsImages= new ImageView[3][4];
		greenCardsImages[0][0]= greenCards00;
		greenCardsImages[0][1]= greenCards01;
		greenCardsImages[0][2]= greenCards02;
		greenCardsImages[0][3]= greenCards03;
		greenCardsImages[1][0]= greenCards10;
		greenCardsImages[1][1]= greenCards11;
		greenCardsImages[1][2]= greenCards12;
		greenCardsImages[1][3]= greenCards13;
		greenCardsImages[2][0]= greenCards20;
		greenCardsImages[2][1]= greenCards21;
		greenCardsImages[2][2]= greenCards22;
		greenCardsImages[2][3]= greenCards23;

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

	public void onMouseEnteredRow1(){
		if(marketAble)
		row1.setVisible(true);
	}

	public void onMouseEnteredRow2(){
		if(marketAble)
		row2.setVisible(true);
	}

	public void onMouseEnteredRow3(){
		if(marketAble)
		row3.setVisible(true);
	}


	public void onMouseEnteredCol1(){
		if(marketAble)
		col1.setVisible(true);
	}

	public void onMouseEnteredCol2(){
		if(marketAble)
		col2.setVisible(true);
	}

	public void onMouseEnteredCol3(){
		if(marketAble)
		col3.setVisible(true);
	}

	public void onMouseEnteredCol4(){
		if(marketAble)
		col4.setVisible(true);
	}

	public void onMouseExitedRow1(){
		row1.setVisible(false);
	}

	public void onMouseExitedRow2(){
		row2.setVisible(false);
	}

	public void onMouseExitedRow3(){
		row3.setVisible(false);
	}

	public void onMouseExitedCol1(){
		col1.setVisible(false);
	}

	public void onMouseExitedCol2(){
		col2.setVisible(false);
	}

	public void onMouseExitedCol3(){
		col3.setVisible(false);
	}

	public void onMouseExitedCol4(){
		col4.setVisible(false);
	}

	public void onMouseClickedRow1(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("row", 1));
			marketAble=false;
		}
	}

	public void onMouseClickedRow2(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("row", 2));
			marketAble=false;
		}
	}

	public void onMouseClickedRow3(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("row", 3));
			marketAble = false;
		}
	}

	public void onMouseClickedCol1(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("COL", 1));
			marketAble = false;
		}
	}

	public void onMouseClickedCol2(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("COL", 2));
			marketAble = false;
		}
	}

	public void onMouseClickedCol3(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("COL", 3));
			marketAble = false;
		}
	}

	public void onMouseClickedCol4(){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction("COL", 4));
			marketAble = false;
		}
	}

	public void setDeck(ReducedDevelopmentCardsDeck deck){
		this.deck=deck;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				DevelopmentCard card=deck.getCardStackStructure()[i][j].get(0);
				cardsImages[i][j].setImage(new Image("/assets/devCards/"+card.getCardNumber()+".png"));

			}
		}
	}

	public void setGreen(ArrayList<DevelopmentCard> cards){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(cards.contains(deck.getCardStackStructure()[i][j].get(0)))
					greenCardsImages[i][j].setVisible(true);
			}
		}
	}


}
