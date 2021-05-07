package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMechanicsMultiPlayer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.reducedClasses.ReducedDevelopmentCardsDeck;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.singleplayer.Token;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.network.messages.game.server2client.WinMessage;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnController {

	private final ArrayList<String> nicknameList;
	private final HashMap<String, VirtualView> virtualViewMap;
	private final ServerSideController serverSideController;
	//private boolean endOfTurn;
	private boolean endOfGame;
	private boolean endgameStarted;
	private int remainingTurn;


	public boolean isMainActionDone() {
		return mainActionDone;
	}

	private boolean mainActionDone;


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

	/**
	 * set next active player
	 */
	public void nextTurn(){
		int currentPlayerIndex = nicknameList.indexOf(activePlayer);
		if(currentPlayerIndex + 1 < mechanics.getNumberOfPlayers())
			currentPlayerIndex = currentPlayerIndex + 1;
		else
			currentPlayerIndex = 0;
		activePlayer=nicknameList.get(currentPlayerIndex);
	}

	public void startTurn(){
		//endOfTurn=false;
		leaderAction=false;
		mainActionDone=false;
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

	private void tokenActivation(){
		VirtualView view = virtualViewMap.get(activePlayer);
		GameMechanicsSinglePlayer mechanicsSinglePlayer = (GameMechanicsSinglePlayer) mechanics;

		Token currentToken= mechanicsSinglePlayer.revealTop();
		boolean check=currentToken.applyEffect();
		view.showToken(currentToken.getTokenType(),currentToken.getColor());
		if(currentToken.getTokenType()==TokenType.DISCARD_TOKEN)
			view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
		else
			view.showFaithTrack( new ReducedFaithTrack(mechanicsSinglePlayer.getLorenzoFaithTrack()));

		if(currentToken.isEndGame())
			endOfGame=true;

		else if (check)
			mechanicsSinglePlayer.shuffleTokenDeck();
	}

	public void setTurnPhase(TurnPhase turnPhase) {
		if(turnPhase == TurnPhase.MAIN_ACTION){
			mainActionDone=true;
			int playerIndex= nicknameList.indexOf(activePlayer);
			Player player= mechanics.getPlayer(playerIndex);
			if(leaderAction || player.checkAvailableLeaderActions().size()==0){
				endTurn();
			}
			else {
				turnAskLeaderAction();
				//endOfTurn=true;
			}
		}
		else if (turnPhase == TurnPhase.LEADER_ACTION){
			leaderAction=true;
			if(mainActionDone){
				endTurn();
			}
			else turnAskAction();
		}

		else if (turnPhase == TurnPhase.END_TURN){
			endTurn();
		}

	}

	private void endTurn(){
		if(nicknameList.size()==1) {
			tokenActivation(); //activate token just in singleplayer
		}
		endgame(); //activated only if endgame started
		if (endOfGame) {
			sendWinner(); // activated only if the game is over. stop the game
		}
		nextTurn();
		startTurn();
	}

	private void sendWinner(){
		StringBuilder winner = new StringBuilder();
		int[] winnerInfo= mechanics.winningPlayers();
		if(winnerInfo[1]==-1)
			winner.append("Lorenzo");
		else
			for (int i = 1; i < winnerInfo.length; i++) {
				winner.append(nicknameList.get(winnerInfo[i]));
				if(i!=winnerInfo.length-1)
					winner.append(", ");
			}
		String stringWinner=winner.toString();
		serverSideController.getLobby().broadcastMessage(new WinMessage(stringWinner,winnerInfo[0]));
		serverSideController.getLobby().endGame();
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
		Player player= mechanics.getPlayer(playerIndex);
		ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
		ArrayList<PlayerActions> leaderActions=player.checkAvailableLeaderActions();
		boolean checkLeader1=leaderActions.contains(PlayerActions.PLAY_LEADER_1);
		boolean checkLeader2=leaderActions.contains(PlayerActions.PLAY_LEADER_2);
		leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[0],player.isActiveAbilityLeader1(),player.isDiscardedLeader1(),checkLeader1));
		leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[1], player.isActiveAbilityLeader2(),player.isDiscardedLeader2(),checkLeader2));
		view.askLeaderAction(leaderCards);
	}

}
