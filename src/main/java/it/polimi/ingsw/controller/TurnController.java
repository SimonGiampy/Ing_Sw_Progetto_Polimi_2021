package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMechanicsMultiPlayer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.singleplayer.Token;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnController {

	private final ArrayList<String> nicknameList;
	private final HashMap<String, VirtualView> virtualViewMap;
	private final ServerSideController serverSideController;
	
	private boolean endOfGame; // if the endgame phase started
	private boolean endgameStarted; // if the last turn started
	private int remainingTurn;
	private boolean mainActionDone; //indicated if the main action in a turn has been completed (market, production or dev card buying)
	private final GameMechanicsMultiPlayer mechanics;

	private String activePlayer;
	
	/**
	 * Constructor for the turn system during gameplay. Handles different phases of a single turn. It also handles
	 * different parts of the game (initial phase, core gameplay, last turn and endgame).
	 * @param virtualViewMap map between nicknames and their virtual views
	 * @param serverSideController server controller
	 * @param nicknameList list of client nicknames
	 * @param mechanics game mechanics (single player or multiplayer)
	 */
	public TurnController(HashMap<String,VirtualView> virtualViewMap, ServerSideController serverSideController,
						  ArrayList<String> nicknameList,GameMechanicsMultiPlayer mechanics){
		this.nicknameList = nicknameList;
		activePlayer = nicknameList.get(0);
		this.virtualViewMap = virtualViewMap;
		this.serverSideController = serverSideController;
		this.mechanics = mechanics;
		endgameStarted = false;
	}

	/**
	 * sets next active player
	 */
	public void nextTurn(){
		int currentPlayerIndex = nicknameList.indexOf(activePlayer);
		if(nicknameList.size()>1)
			virtualViewMap.get(nicknameList.get(currentPlayerIndex)).showGenericMessage("Your turn has ended!");
		if(currentPlayerIndex + 1 < mechanics.getNumberOfPlayers())
			currentPlayerIndex = currentPlayerIndex + 1;
		else
			currentPlayerIndex = 0;
		activePlayer = nicknameList.get(currentPlayerIndex);
	}

	/**
	 * it starts the turn
	 */
	public void startTurn(){
		mainActionDone = false;
		VirtualView view = virtualViewMap.get(activePlayer);
		view.showGenericMessage("It's your turn!");
		serverSideController.sendBoxes(nicknameList.indexOf(activePlayer), true);
		turnAskAction();
		setTurnPhase(TurnPhase.SELECTING_ACTION);
	}

	/**
	 * it sends to the active player which action he can do
	 */
	public void turnAskAction(){
		VirtualView view = virtualViewMap.get(activePlayer);
		int playerIndex = nicknameList.indexOf(activePlayer);
		if(mainActionDone)
			view.askAction(nicknameList.get(playerIndex), mechanics.getPlayer(playerIndex).checkAvailableLeaderActions());
		else
			view.askAction(nicknameList.get(playerIndex), mechanics.getPlayer(playerIndex).checkAvailableActions());
	}

	/**
	 * it activates single player token
	 */
	private void tokenActivation(){
		VirtualView view = virtualViewMap.get(activePlayer);
		GameMechanicsSinglePlayer mechanicsSinglePlayer = (GameMechanicsSinglePlayer) mechanics;

		Token currentToken = mechanicsSinglePlayer.revealTop();
		boolean check = currentToken.applyEffect();
		view.showToken(currentToken.getTokenType(),currentToken.getColor());
		if(currentToken.getTokenType() == TokenType.DISCARD_TOKEN) {
			view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			view.showGenericMessage("Token revealed! 2 " + currentToken.getColor().toString().toLowerCase() +
					" cards discarded from the deck");
		}
		else {
			if(check) view.showGenericMessage("Token revealed! Lorenzo gets 1 faith point and the token deck has been shuffled");
			else view.showGenericMessage("Token revealed! Lorenzo gets 2 faith points");
			if(!serverSideController.checkVaticanReport())
				serverSideController.sendFaithTracks();
		}

		if(currentToken.isEndGame()){ // true if Lorenzo triggers the end of the game
			endOfGame=true;
			sendWinner();
		} else if (check)
			mechanicsSinglePlayer.shuffleTokenDeck();
	}

	/**
	 * it sets turn phase and handles turn main actions
	 * @param turnPhase is the phase to be set
	 */
	public void setTurnPhase(TurnPhase turnPhase) {
		int playerIndex = nicknameList.indexOf(activePlayer);
		Player player = mechanics.getPlayer(playerIndex);
		if(turnPhase == TurnPhase.MAIN_ACTION){
			mainActionDone = true;

			if(player.checkAvailableLeaderActions().size()==0){
				endTurn();
			} else {
				turnAskLeaderAction();
			}
		}
		else if (turnPhase == TurnPhase.LEADER_ACTION){
			if(!mainActionDone)
				turnAskAction();
			else if(player.checkAvailableLeaderActions().size() == 0){
				endTurn();
			}
			else
				turnAskLeaderAction();
		} else if (turnPhase == TurnPhase.END_TURN){
			endTurn();
		}

	}

	/**
	 * it handles end of the turn phase
	 */
	private void endTurn(){
		endgame(); //activated only if endgame started
		if (endOfGame) {
			sendWinner(); // activated only if the game is over. stop the game
		}
		else {
			if(nicknameList.size() == 1) {
				tokenActivation(); //activate token just in singlePlayer
			}
			if(!endOfGame) {
				nextTurn();
				startTurn();
			}
		}
	}

	/**
	 * it sends winner message to all the players and ends the game
	 */
	protected void sendWinner(){
		StringBuilder winner = new StringBuilder();
		int[] winnerInfo;
		if(mechanics.getNumberOfPlayers() == 1) {
			GameMechanicsSinglePlayer mec = (GameMechanicsSinglePlayer) mechanics;
			winnerInfo = mec.winningPlayers();
			if(winnerInfo[1] == -1)
				winner.append("LORENZO IL MAGNIFICO");
			else
				winner.append(nicknameList.get(0));
		} else {
			winnerInfo = mechanics.winningPlayers();
			for (int i = 1; i < winnerInfo.length; i++) {
				winner.append(nicknameList.get(winnerInfo[i]));
				if (i != winnerInfo.length - 1)
					winner.append(", ");
			}
		}
		String stringWinner = winner.toString();

		for (String s : nicknameList) {
			virtualViewMap.get(s).showWinMessage(stringWinner, winnerInfo[0]);
		}

		if(serverSideController.getLobby() != null)
			serverSideController.getLobby().endGame();
	}

	/**
	 * it handles remaining turn after a player triggers end game
	 */
	public void endgame(){
		boolean check=false;
		for (int i = 0; i < nicknameList.size(); i++) {
			if(mechanics.getPlayer(i).isEndgameStarted()) {
				check = true;
				break;
			}
		}
		if(check && !endgameStarted){ //sets endgame and notify players
			if(nicknameList.size()>1) {
				for (String s : nicknameList) {
					virtualViewMap.get(s).showGenericMessage("EndGame started!");
				}
			}
			endgameStarted = true;
			remainingTurn = nicknameList.size() - nicknameList.indexOf(activePlayer) - 1;
			if(remainingTurn == 0)
				endOfGame = true;
		}
		else if(endgameStarted && remainingTurn>0){
			remainingTurn--;
		}

		else if(endgameStarted && remainingTurn==0)
			endOfGame = true;
	}

	/**
	 * it asks the active player if he wants to do a leader action or end the turn
	 */
	public void turnAskLeaderAction(){
		VirtualView view = virtualViewMap.get(activePlayer);
		int playerIndex = nicknameList.indexOf(activePlayer);
		Player player = mechanics.getPlayer(playerIndex);
		ArrayList<ReducedLeaderCard> leaderCards = serverSideController.obtainLeadersFromPlayer(player);
		view.askLeaderAction(nicknameList.get(playerIndex), leaderCards);
	}

	/**
	 * getter for main action boolean
	 * @return true if the main action is done
	 */
	public boolean isMainActionDone() {
		return mainActionDone;
	}

}
