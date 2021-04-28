package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMechanicsMultiPlayer;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnController {

	private final ArrayList<String> nicknameList;
	private final HashMap<String, VirtualView> virtualViewMap;
	private final GameController gameController;
	private boolean endOfTurn;



	private boolean MainActionDone;
	private PhaseType phaseType;



	private boolean leaderAction;
	private final GameMechanicsMultiPlayer mechanics;

	private String activePlayer;

	public TurnController(HashMap<String,VirtualView> virtualViewMap, GameController gameController,
						  ArrayList<String> nicknameList,GameMechanicsMultiPlayer mechanics){
		this.nicknameList=nicknameList;
		activePlayer=nicknameList.get(0);
		this.virtualViewMap=virtualViewMap;
		this.gameController=gameController;
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
		else currentPlayerIndex=0;
		activePlayer=nicknameList.get(0);
	}

	public void newTurn(){
		setEndOfTurn(false);
		setLeaderAction(false);
		setMainActionDone(false);
		VirtualView view= virtualViewMap.get(activePlayer);
		view.showGenericMessage("It's your turn!");
		turnAskAction();
		setPhaseType(PhaseType.SELECTING_ACTION);
	}

	public void turnAskAction(){
		VirtualView view = virtualViewMap.get(activePlayer);
		int playerIndex= nicknameList.indexOf(activePlayer);
		view.askAction(mechanics.getPlayer(playerIndex).checkAvailableActions());
	}

	public void setEndOfTurn(boolean endOfTurn) {
		this.endOfTurn = endOfTurn;
	}

	public void setLeaderAction(boolean leaderAction) {
		this.leaderAction = leaderAction;
	}

	public PhaseType getPhaseType() {
		return phaseType;
	}

	public void setPhaseType(PhaseType phaseType) {
		this.phaseType = phaseType;

		if(phaseType == PhaseType.MAIN_ACTION){
			setMainActionDone(true);
			if(leaderAction){
				next();
				newTurn();
			}
			else {
				turnAskLeaderAction();
				setEndOfTurn(true);
			}
		}
		else if (phaseType==PhaseType.LEADER_ACTION){
			setLeaderAction(true);
			if(MainActionDone){
				next();
				newTurn();
			}
			else turnAskAction();
		}

		else if (phaseType==PhaseType.END_TURN){
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
