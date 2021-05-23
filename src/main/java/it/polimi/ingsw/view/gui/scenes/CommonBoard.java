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

	@FXML ImageView text; //useless
	@FXML Button marketActivation;
	@FXML Button cardsActivation;
	@FXML Button slot1;
	@FXML Button slot2;
	@FXML Button slot3;
	@FXML Text slotText;

	private ImageView[][] marketImages;
	private ImageView[][] cardsImages;
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

		//set cards imageview matrix
		cardsImages= new ImageView[][]{new ImageView[]{cards00,cards01,cards02,cards03},
				new ImageView[]{cards10,cards11,cards12,cards13},
				new ImageView[]{cards20,cards21,cards22,cards23}};
		/*
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

		 */

		//set market imageview matrix
		marketImages= new ImageView[][]{new ImageView[]{market00,market01,market02,market03},
				new ImageView[]{market10,market11,market12,market13},
				new ImageView[]{market20,market21,market22,market23}};
		/*
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

		 */

		//set rows images array
		ImageView[] rows= new ImageView[]{normalRow1,normalRow2,normalRow3};

		//set cols images array
		ImageView[] cols= new ImageView[]{normalCol1,normalCol2,normalCol3,normalCol4};

		//set slots
		Button[] slots= new Button[]{slot1,slot2,slot3};


		//set action buttons click event
		marketActivation.setOnMouseClicked(event->actionSelectionHandler(PlayerActions.MARKET));
		cardsActivation.setOnMouseClicked(event->actionSelectionHandler(PlayerActions.BUY_CARD));

		//set cards click event
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				int finalI = i;
				int finalJ = j;
				cardsImages[i][j].setOnMouseClicked(event -> cardsSelectionHandler(finalI+1,Colors.values()[finalJ],finalI,finalJ));
			}
		}

		//set rows events
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			rows[i].setOnMouseEntered(event->marketEnteredHandler(rows[finalI]));
			rows[i].setOnMouseExited(event->marketExitedHandler(rows[finalI]));
			rows[i].setOnMouseClicked(event->marketCLickHandler("ROW", finalI+1));
		}

		//set cols events
		for (int i = 0; i < 4; i++) {
			int finalI = i;
			cols[i].setOnMouseEntered(event->marketEnteredHandler(cols[finalI]));
			cols[i].setOnMouseExited(event->marketExitedHandler(cols[finalI]));
			cols[i].setOnMouseClicked(event->marketCLickHandler("COL", finalI+1));
		}

		//set slots event
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			slots[i].setOnMouseClicked(event->slotClickHandler(finalI +1));
		}

	}

	public void cardsSelectionHandler(int level, Colors color, int i, int j){
		if(cardsAble && buyableCards.contains(deck.getCardStackStructure()[i][j].get(0))){
			this.level=level;
			this.color=color;
			setGlow();
			cardsImages[i][j].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
			slotText.setText("Select a Slot!");
		}
	}

	public void actionSelectionHandler(PlayerActions action){
		if(action.equals(PlayerActions.MARKET))
			marketAble=true;
		cardsActivation.setVisible(false);
		marketActivation.setVisible(false);
		notifyObserver(obs -> obs.onUpdateAction(action));
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

	public void marketEnteredHandler(ImageView img){
		if(marketAble)
			img.setEffect(new Glow(1));
	}

	public void marketExitedHandler(ImageView img){
		img.setEffect(new Glow(0.0));
	}

	public void marketCLickHandler(String which, int where){
		if(marketAble) {
			notifyObserver(obs -> obs.onUpdateMarketAction(which, where));
			marketAble=false;
		}
	}

	public void slotClickHandler(int slot){
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color,level,slot));
		cardsAble=false;
		setSlotsButtonVisible(false);
	}

	public void setDeck(ReducedDevelopmentCardsDeck deck){
		this.deck=deck;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(deck.getCardStackStructure()[i][j].size()>0) {
					DevelopmentCard card = deck.getCardStackStructure()[i][j].get(0);
					cardsImages[i][j].setImage(new Image("/assets/devCards/" + card.getCardNumber() + ".png"));
					cardsImages[i][j].setEffect(new Glow(0));
				}
				else
					cardsImages[i][j].setVisible(false);
			}
		}
	}

	public void setGreen(ArrayList<DevelopmentCard> cards, boolean wrong){
		if(wrong) {
			slotText.setVisible(true);
			slotText.setText("Wrong Slot!");

		}
		cardsAble=true;
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

	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.GREEN);
		img.setEffect(dropShadow);
	}

}
