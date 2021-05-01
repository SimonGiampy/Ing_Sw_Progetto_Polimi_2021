package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMechanicsMultiPlayer;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.singleplayer.Token;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnController {

	private final ArrayList<String> nicknameList;
	private final HashMap<String, VirtualView> virtualViewMap;
	private final ServerSideController serverSideController;
	private boolean endOfTurn;



	private boolean MainActionDone;
	private TurnPhase turnPhase;



	private boolean leaderAction;
	private final GameMechanicsMultiPlayer mechanics;

	private String activePlayer;

	public TurnController(HashMap<String,VirtualView> virtualViewMap, ServerSideController serverSideController,
						  ArrayList<String> nicknameList,GameMechanicsMultiPlayer mechanics){
		this.nicknameList=nicknameList;
		activePlayer=nicknameList.get(0);
		this.virtualViewMap=virtualViewMap;
		this.serverSideController = serverSideController;
		this.mechanics=mechanics;
	}

	public String getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(String activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * set next active player
	 */
	public void next(){
		int currentPlayerIndex=nicknameList.indexOf(activePlayer);
		if(currentPlayerIndex+1< mechanics.getNumberOfPlayers())
			currentPlayerIndex=currentPlayerIndex+1;
		else
			currentPlayerIndex=0;
		activePlayer=nicknameList.get(currentPlayerIndex);
	}

	public void newTurn(){
		setEndOfTurn(false);
		setLeaderAction(false);
		setMainActionDone(false);
		VirtualView view= virtualViewMap.get(activePlayer);
		view.showGenericMessage("It's your turn!");
		turnAskAction();
		setTurnPhase(TurnPhase.SELECTING_ACTION);
	}

	public void turnAskAction(){
		VirtualView view = virtualViewMap.get(activePlayer);
		int playerIndex= nicknameList.indexOf(activePlayer);
		if(leaderAction)
			view.askAction(mechanics.getPlayer(playerIndex).checkAvailableNormalActions());
		else
			view.askAction(mechanics.getPlayer(playerIndex).checkAvailableActions());
	}

	public void setEndOfTurn(boolean endOfTurn) {
		this.endOfTurn = endOfTurn;
	}

	public void setLeaderAction(boolean leaderAction) {
		this.leaderAction = leaderAction;
	}

	public TurnPhase getPhaseType() {
		return turnPhase;
	}

	private void tokenActivation(){
		VirtualView view = virtualViewMap.get(activePlayer);
		if(nicknameList.size()==1){
			Token currentToken= mechanics.revealTop();
			currentToken.applyEffect();
			view.showToken(currentToken.getTokenType(),currentToken.getColor());
			if(currentToken.getTokenType()==TokenType.DISCARD_TOKEN)
				view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			else
				view.showFaithTrack( new ReducedFaithTrack(mechanics.getLorenzoFaithTrack()));
			if(currentToken.applyEffect())
				mechanics.shuffleTokenDeck();
		}
	}

	public void setTurnPhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
		if(turnPhase == TurnPhase.MAIN_ACTION){
			setMainActionDone(true);
			if(leaderAction){
				tokenActivation();
				next();
				newTurn();
			}
			else {
				turnAskLeaderAction();
				setEndOfTurn(true);
			}
		}
		else if (turnPhase == TurnPhase.LEADER_ACTION){
			setLeaderAction(true);
			if(MainActionDone){
				tokenActivation();
				next();
				newTurn();
			}
			else turnAskAction();
		}

		else if (turnPhase == TurnPhase.END_TURN){
			tokenActivation();
			next();
			newTurn();
		}

	}

	public void turnAskLeaderAction(){
		VirtualView view= virtualViewMap.get(activePlayer);
		int playerIndex= nicknameList.indexOf(activePlayer);
		ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[0]));
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[1]));
		view.askLeaderAction(leaderCards);
	}

	public void setMainActionDone(boolean mainActionDone) {
		MainActionDone = mainActionDone;
	}

	public boolean isMainActionDone() {
		return MainActionDone;
	}
}
