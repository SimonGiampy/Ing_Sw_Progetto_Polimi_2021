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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;


public class CommonBoard extends ViewObservable implements SceneController{

	@FXML
	GridPane marketGUI;

	//marbles imageView
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

	//arrows imageView
	@FXML ImageView normalRow1;
	@FXML ImageView normalRow2;
	@FXML ImageView normalRow3;
	@FXML ImageView normalCol1;
	@FXML ImageView normalCol2;
	@FXML ImageView normalCol3;
	@FXML ImageView normalCol4;

	//cards imageView
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
	//buttons
	@FXML Button marketActivation;
	@FXML Button cardsActivation;
	@FXML Button slot1;
	@FXML Button slot2;
	@FXML Button slot3;
	@FXML Button endTurn;

	//text for wrong slot
	@FXML Text slotText;

	//matrices of images
	private ImageView[][] marketImages;
	private ImageView[][] cardsImages;

	//flags needed for indicating if an action is doable
	private boolean marketDoable;
	private boolean cardsDoable;

	//attributes for cards
	private ReducedDevelopmentCardsDeck deck;
	private int level;
	private Colors color;
	private ArrayList<DevelopmentCard> buyableCards;
	
	private Board playerBoard;

	@FXML
	public void initialize(){
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 10);
		
		marketDoable = false;
		cardsDoable = false;

		//set cards imageview matrix
		cardsImages= new ImageView[][]{new ImageView[]{cards00,cards01,cards02,cards03},
				new ImageView[]{cards10,cards11,cards12,cards13},
				new ImageView[]{cards20,cards21,cards22,cards23}};
		
		//set market imageview matrix
		marketImages= new ImageView[][]{new ImageView[]{market00,market01,market02,market03},
				new ImageView[]{market10,market11,market12,market13},
				new ImageView[]{market20,market21,market22,market23}};

		//set rows images array
		ImageView[] rows= new ImageView[]{normalRow1,normalRow2,normalRow3};

		//set cols images array
		ImageView[] cols= new ImageView[]{normalCol1,normalCol2,normalCol3,normalCol4};

		//set slots
		Button[] slots= new Button[]{slot1,slot2,slot3};


		//set action buttons click event
		marketActivation.setOnMouseClicked(event-> actionClicked(PlayerActions.MARKET));
		cardsActivation.setOnMouseClicked(event-> actionClicked(PlayerActions.BUY_CARD));

		//set cards click event
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				int finalI = i;
				int finalJ = j;
				cardsImages[i][j].setOnMouseClicked(event -> cardsSelectionHandler(Colors.values()[finalJ],finalI,finalJ));
			}
		}

		//set rows events
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			rows[i].setOnMouseEntered(event->marketEnteredHandler(rows[finalI]));
			rows[i].setOnMouseExited(event->marketExitedHandler(rows[finalI]));
			rows[i].setOnMouseClicked(event-> marketClickHandler("ROW", finalI+1));
		}

		//set cols events
		for (int i = 0; i < 4; i++) {
			int finalI = i;
			cols[i].setOnMouseEntered(event->marketEnteredHandler(cols[finalI]));
			cols[i].setOnMouseExited(event->marketExitedHandler(cols[finalI]));
			cols[i].setOnMouseClicked(event-> marketClickHandler("COL", finalI+1));
		}

		//set slots event
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			slots[i].setOnMouseClicked(event->slotClickHandler(finalI +1));
		}

		//set endTurn button event
		endTurn.setOnMouseClicked(event->endTurnHandler());

	}
	
	public void setPlayerBoard(Board playerBoard) {
		this.playerBoard = playerBoard;
	}

	/*----------------------------------------------------------- ACTION ----------------------------------------------------*/

	/**
	 * when the buttons for the interactions are clicked, they become invisible
	 * @param action is the action selected by the player
	 */
	public void actionClicked(PlayerActions action){
		if(action.equals(PlayerActions.MARKET))
			marketDoable =true;
		cardsActivation.setVisible(false);
		marketActivation.setVisible(false);
		notifyObserver(obs -> obs.onUpdateAction(action));
	}

	/**
	 * the buttons for the market and card interactions are set visible
	 * @param actions is a list of available actions
	 */
	public void setButtonVisible(ArrayList<PlayerActions> actions){
		if (actions.contains(PlayerActions.BUY_CARD))
			cardsActivation.setVisible(true);
		marketActivation.setVisible(true);
	}

	/**
	 * this sets all the action buttons not visible
	 */
	public void disableButtons() {
		cardsActivation.setVisible(false);
		marketActivation.setVisible(false);
	}


	/*----------------------------------------------------------- CARDS DECK ----------------------------------------------------*/

	/**
	 * updates the dev cards deck with all the corresponding images
	 * @param deck is the deck of cards from server
	 */
	public void setDeck(ReducedDevelopmentCardsDeck deck){
		this.deck=deck;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(deck.getCardStackStructure()[i][j].size()>0) {
					DevelopmentCard card = deck.getCardStackStructure()[i][j].get(0);
					cardsImages[i][j].setImage(new Image("/assets/devCards/" + card.getCardNumber() + ".png"));
					cardsImages[i][j].setEffect(new Glow(0));
				} else {
					cardsImages[i][j].setVisible(false);
				}
			}
		}
	}

	/**
	 * mouse click handler on a development card in the deck
	 * @param color is the color of the selected card
	 * @param i is the index of the row of the matrix
	 * @param j is the index of the column of the matrix
	 */
	public void cardsSelectionHandler(Colors color, int i, int j){
		if(cardsDoable && buyableCards.contains(deck.getCardStackStructure()[i][j].get(0))){
			this.level=i+1;
			this.color=color;
			setEffectOnCards();
			cardsImages[i][j].setEffect(new Glow(0.5));
			setSlotsButtonVisible(true);
			slotText.setText("Select a Slot!");
		}
	}

	/**
	 * sets the visibility of the slots buttons
	 * @param visible booloan flag
	 */
	public void setSlotsButtonVisible(boolean visible){
		slot1.setVisible(visible);
		slot2.setVisible(visible);
		slot3.setVisible(visible);
		slotText.setVisible(visible);
	}

	/**
	 * handles the click on one of the 3 buttons for the slots
	 * @param slot is the slot selected by the player
	 */
	public void slotClickHandler(int slot){
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color,level,slot));
		cardsDoable =false;
		setSlotsButtonVisible(false);
	}

	/**
	 * adds a shadow to the buyable cards. This function sets also the label text for when the selected slot is not valid
	 * @param cards is a list of cards to be set
	 * @param wrong is true if the player has selected a not available slot
	 */
	public void setBuyableCardsEffect(ArrayList<DevelopmentCard> cards, boolean wrong){
		if(wrong) {
			slotText.setVisible(true);
			slotText.setText("Wrong Slot!");
		}
		cardsDoable =true;
		buyableCards=cards;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if(!deck.getCardStackStructure()[i][j].isEmpty() && cards.contains(deck.getCardStackStructure()[i][j].get(0))) {
					setShadow(0.7, cardsImages[i][j]);
				}
			}
		}
	}

	/**
	 * adds a golden shadow to the buyable cards or resets the glow effect
	 */
	public void setEffectOnCards() {
		for (int k = 0; k < 3; k++) {
			for (int l = 0; l < 4; l++) {
				if(deck.getCardStackStructure()[k][l].size()>0 && buyableCards.contains(deck.getCardStackStructure()[k][l].get(0)))
					setShadow(0.7, cardsImages[k][l]);
				else cardsImages[k][l].setEffect(new Glow(0));
			}
		}
	}

	/**
	 * adds a shadow with a certain value to an image
	 * @param value is the value of the shadow
	 * @param img is the selected image
	 */
	public void setShadow(double value, ImageView img){
		DropShadow dropShadow = new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.GOLD);
		img.setEffect(dropShadow);
	}

	/*----------------------------------------------------------- MARKET ----------------------------------------------------*/

	/**
	 * updates marble images in the market
	 * @param market is the market from the server
	 */
	public void setMarket(ReducedMarket market) {
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

	/**
	 * applies a glow effect to the arrows where the mouse hovers
	 * @param img is the selected image
	 */
	public void marketEnteredHandler(ImageView img) {
		if(marketDoable)
			img.setEffect(new Glow(1));
	}

	/**
	 * removes the glow effect to the arrows where the mouse stops hovering
	 * @param img is the selected image
	 */
	public void marketExitedHandler(ImageView img){
		img.setEffect(new Glow(0.0));
	}

	/**
	 * click handler on the arrows, then notifies the server with the chosen row or col
	 * @param which is row or col
	 * @param where is the selected row/col
	 */
	public void marketClickHandler(String which, int where) {
		if(marketDoable) {
			notifyObserver(obs -> obs.onUpdateMarketAction(which, where));
			marketDoable =false;
		}
	}

	/*----------------------------------------------------------- END_TURN ----------------------------------------------------*/

	/**
	 * sets the visibility of the end turn button
	 * @param value is true if the button has to be set visible
	 */
	public void setEndTurnVisible(boolean value){
		endTurn.setVisible(value);
	}

	/**
	 * click on end turn button
	 */
	public void endTurnHandler() {
		notifyObserver(obs->obs.onUpdateLeaderAction(0,0));
		endTurn.setVisible(false);
		playerBoard.setEndTurn();
	}

}
