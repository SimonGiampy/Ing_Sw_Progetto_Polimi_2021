package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMechanicsMultiPlayer;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.singleplayer.Token;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.network.messages.game.server2client.WinMessage;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnController {

	private final ArrayList<String> nicknameList;
	private final HashMap<String, VirtualView> virtualViewMap;
	private final ServerSideController serverSideController;
	private boolean endOfTurn;
	private boolean endOfGame;
	private boolean endgameStarted;
	private int remainingTurn;



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
		endgameStarted=false;
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
	public void nextTurn(){
		int currentPlayerIndex=nicknameList.indexOf(activePlayer);
		if(currentPlayerIndex+1< mechanics.getNumberOfPlayers())
			currentPlayerIndex=currentPlayerIndex+1;
		else
			currentPlayerIndex=0;
		activePlayer=nicknameList.get(currentPlayerIndex);
	}

	public void startTurn(){
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
		GameMechanicsSinglePlayer mechanicsSinglePlayer = (GameMechanicsSinglePlayer) mechanics;
		if(nicknameList.size()==1){
			Token currentToken= mechanicsSinglePlayer.revealTop();
			currentToken.applyEffect();
			view.showToken(currentToken.getTokenType(),currentToken.getColor());
			if(currentToken.getTokenType()==TokenType.DISCARD_TOKEN)
				view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			else
				view.showFaithTrack( new ReducedFaithTrack(mechanicsSinglePlayer.getLorenzoFaithTrack()));

			if(currentToken.isEndGame())
				endOfGame=true;

			else if (currentToken.applyEffect())
				mechanicsSinglePlayer.shuffleTokenDeck();
		}
	}

	public void setTurnPhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
		if(turnPhase == TurnPhase.MAIN_ACTION){
			setMainActionDone(true);
			if(leaderAction){
				endTurn();
			}
			else {
				turnAskLeaderAction();
				setEndOfTurn(true);
			}
		}
		else if (turnPhase == TurnPhase.LEADER_ACTION){
			setLeaderAction(true);
			if(MainActionDone){
				endTurn();
			}
			else turnAskAction();
		}

		else if (turnPhase == TurnPhase.END_TURN){
			endTurn();
		}

	}

	private void endTurn(){
		tokenActivation();
		endgame(); //activated only if endgame started
		sendWinner(); // activated only if the game is over. stop the game
		nextTurn();
		startTurn();
	}

	private void sendWinner(){
		if (endOfGame){
			StringBuilder winner = new StringBuilder();
			int[] winnerInfo= mechanics.winner();
			if(winnerInfo[1]==-1)
				winner.append("Lorenzo");
			else
				for (int i = 1; i < winnerInfo.length; i++) {
					winner.append(nicknameList.get(winnerInfo[i]));
					if(i!=winnerInfo.length-1)
						winner.append(", ");
				}
			String stringWinner=winner.toString();
			serverSideController.getLobby().broadcastMessage(new WinMessage(stringWinner));
			//TODO: add end of game, closing socket
		}

	}

	public void endgame(){
		boolean check=false;
		for (int i = 0; i < nicknameList.size(); i++) {
			if(mechanics.getPlayer(i).isEndgameStarted()) {
				check = true;
				break;
			}
		}
		if(check && !endgameStarted){
			endgameStarted=true;
			remainingTurn=nicknameList.size()-nicknameList.indexOf(activePlayer)-1;
			if(remainingTurn==0)
				endOfGame=true;
		}
		else if(endgameStarted && remainingTurn>0){
			remainingTurn--;
		}

		else if(endgameStarted && remainingTurn==0)
			endOfGame=true;
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

	public boolean isEndOfGame() {
		return endOfGame;
	}

	public void setEndOfGame(boolean endOfGame) {
		this.endOfGame = endOfGame;
	}

	public void setEndgameStarted(boolean endgameStarted) {
		this.endgameStarted = endgameStarted;
	}

	public void setRemainingTurn(int remainingTurn) {
		this.remainingTurn = remainingTurn;
	}
}
