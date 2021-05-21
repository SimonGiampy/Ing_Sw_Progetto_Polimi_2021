package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;


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

	@FXML ImageView normalRow1;
	@FXML ImageView normalRow2;
	@FXML ImageView normalRow3;
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

	@FXML ImageView text; //useless

	@FXML Button marketActivation;
	@FXML Button cardsActivation;

	@FXML Button slot1;
	@FXML Button slot2;
	@FXML Button slot3;

	@FXML Text slotText;

	private boolean marketAble;
	private boolean cardsAble;
	private ReducedDevelopmentCardsDeck deck;

	private int level;
	private Colors color;
	private ArrayList<DevelopmentCard> buyableCards;




	@FXML
	public void initialize(){
		marketAble=false;
		cardsAble=false;
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

	public void onMouseClickedActivateMarket(){
		marketAble=true;
		cardsActivation.setVisible(false);
		marketActivation.setVisible(false);
		notifyObserver(obs -> obs.onUpdateAction(PlayerActions.MARKET));
	}

	public void onMouseClickedActivateCards(){
		cardsAble=true;
		cardsActivation.setVisible(false);
		marketActivation.setVisible(false);

		notifyObserver(obs -> obs.onUpdateAction(PlayerActions.BUY_CARD));
	}

	public void setButtonVisible(ArrayList<PlayerActions> actions){
		if (actions.contains(PlayerActions.BUY_CARD))
			cardsActivation.setVisible(true);
		marketActivation.setVisible(true);
	}

	public void setButtonVisible(boolean value){
		if(value){
			cardsActivation.setVisible(true);
			marketActivation.setVisible(true);
		}
		else {
			cardsActivation.setVisible(false);
			marketActivation.setVisible(false);
		}
	}

	public void setSlotsButtonVisible( boolean visible){
		if (visible) {
			slot1.setVisible(true);
			slot2.setVisible(true);
			slot3.setVisible(true);
			slotText.setVisible(true);
		}
		else{
			slot1.setVisible(false);
			slot2.setVisible(false);
			slot3.setVisible(false);
			slotText.setVisible(false);
		}
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
			normalRow1.setEffect(new Glow(1));
	}

	public void onMouseEnteredRow2(){
		if(marketAble)
			normalRow2.setEffect(new Glow(1));
	}

	public void onMouseEnteredRow3(){
		if(marketAble)
			normalRow3.setEffect(new Glow(1));
	}


	public void onMouseEnteredCol1(){
		if(marketAble)
			normalCol1.setEffect(new Glow(1));
	}

	public void onMouseEnteredCol2(){
		if(marketAble)
			normalCol2.setEffect(new Glow(1));
	}

	public void onMouseEnteredCol3(){
		if(marketAble)
			normalCol3.setEffect(new Glow(1));
	}

	public void onMouseEnteredCol4(){
		if(marketAble)
			normalCol4.setEffect(new Glow(1));
	}

	public void onMouseExitedRow1(){
		normalRow1.setEffect(new Glow(0.0));
	}

	public void onMouseExitedRow2(){
		normalRow2.setEffect(new Glow(0.0));

	}

	public void onMouseExitedRow3(){
		normalRow3.setEffect(new Glow(0.0));
	}

	public void onMouseExitedCol1(){
		normalCol1.setEffect(new Glow(0.0));
	}

	public void onMouseExitedCol2(){
		normalCol2.setEffect(new Glow(0.0));
	}

	public void onMouseExitedCol3(){
		normalCol3.setEffect(new Glow(0.0));
	}

	public void onMouseExitedCol4(){
		normalCol4.setEffect(new Glow(0.0));
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

	public void onMouseClickedCards00(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[0][0].get(0))){
			level=1;
			color=Colors.GREEN;
			setGlow();
			cardsImages[0][0].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}
	}

	public void onMouseClickedCards01(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[0][1].get(0))){
			level=1;
			color=Colors.BLUE;
			setGlow();
			cardsImages[0][1].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards02(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[0][2].get(0))){
			level=1;
			color=Colors.YELLOW;
			setGlow();
			cardsImages[0][2].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards03(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[0][3].get(0))){
			level=1;
			color=Colors.PURPLE;
			setGlow();
			cardsImages[0][3].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}
	}

	public void onMouseClickedCards10(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[1][0].get(0))){
			level=2;
			color=Colors.GREEN;
			setGlow();
			cardsImages[1][0].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards11(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[1][1].get(0))){
			level=2;
			color=Colors.BLUE;
			setGlow();
			cardsImages[1][1].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards12(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[1][2].get(0))){
			level=2;
			color=Colors.YELLOW;
			setGlow();
			cardsImages[1][2].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}
	}

	public void onMouseClickedCards13(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[1][3].get(0))){
			level=2;
			color=Colors.PURPLE;
			setGlow();
			cardsImages[1][3].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards20(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[2][0].get(0))){
			level=3;
			color=Colors.GREEN;
			setGlow();
			cardsImages[2][0].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards21(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[2][1].get(0))){
			level=3;
			color=Colors.BLUE;
			setGlow();
			cardsImages[2][1].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards22(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[2][2].get(0))){
			level=3;
			color=Colors.YELLOW;
			setGlow();
			cardsImages[2][2].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}

	}

	public void onMouseClickedCards23(){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[2][3].get(0))){
			level=3;
			color=Colors.PURPLE;
			setGlow();
			cardsImages[2][3].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
		}
	}

	public void onMouseClickedSlot1(){
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color,level,1));
		cardsAble=false;
		setSlotsButtonVisible(false);
	}

	public void onMouseClickedSlot2(){
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color,level,2));
		cardsAble=false;
		setSlotsButtonVisible(false);
	}

	public void onMouseClickedSlot3(){
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color,level,3));
		cardsAble=false;
		setSlotsButtonVisible(false);
	}

	public void setDeck(ReducedDevelopmentCardsDeck deck){
		this.deck=deck;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				DevelopmentCard card=deck.getCardStackStructure()[i][j].get(0);
				cardsImages[i][j].setImage(new Image("/assets/devCards/"+card.getCardNumber()+".png"));
				cardsImages[i][j].setEffect(new Glow(0));
			}
		}
	}

	public void setGreen(ArrayList<DevelopmentCard> cards){
		buyableCards=cards;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(cards.contains(deck.getCardStackStructure()[i][j].get(0))) {
					setShadow(0.7, cardsImages[i][j]);
				}
			}
		}
	}

	public void setGlow(){
		for (int k = 0; k < 3; k++) {
			for (int l = 0; l < 4; l++) {
					if(buyableCards.contains(deck.getCardStackStructure()[k][l].get(0)))
							setShadow(0.7, cardsImages[k][l]);
					else cardsImages[k][l].setEffect(new Glow(0));
			}
		}
	}


	public void setTextVisible(Boolean visible){
		text.setVisible(visible);
		text.setEffect(new Glow(0.7));
	}

	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.GREEN);
		img.setEffect(dropShadow);
	}


}
