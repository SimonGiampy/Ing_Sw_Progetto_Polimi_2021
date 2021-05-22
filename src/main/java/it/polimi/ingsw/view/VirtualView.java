package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.server2client.*;
import it.polimi.ingsw.network.messages.generic.DisconnectionMessage;
import it.polimi.ingsw.network.messages.generic.ErrorMessage;
import it.polimi.ingsw.network.messages.generic.GenericMessage;
import it.polimi.ingsw.network.messages.login.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observers.Observer;

import java.util.ArrayList;

public class VirtualView implements View, Observer {

	private final ClientHandler clientHandler;

	public VirtualView(ClientHandler clientHandler){
		this.clientHandler = clientHandler;
	}

	@Override
	public void update(Message message) {
		clientHandler.sendMessage(message);
	}
	
	@Override
	public void askNumberOfPlayer() {
		clientHandler.sendMessage(new PlayerNumberRequest());
	}
	
	@Override
	public void askNickname() {
		clientHandler.sendMessage(new NicknameRequest());
	}

	/*
	@Override
	public void askCustomGame() {
		clientHandler.sendMessage(new GameConfigRequest());
	}
	 */

	@Override
	public void askInitResources(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 0));
	}

	@Override
	public void askInitLeaders(String nickname, ArrayList<ReducedLeaderCard> leaderCards) {
		clientHandler.sendMessage(new LeaderInteractions(nickname, leaderCards, 0));
	}

	@Override
	public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		clientHandler.sendMessage(new LeaderInteractions(nickname, availableLeaders, 2));
	}

	@Override
	public void askAction(String nickname,ArrayList<PlayerActions> availableAction) {
		clientHandler.sendMessage(new ActionRequest(nickname,availableAction));
	}

	@Override
	public void askMarketAction(String nickname,ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow(nickname,market, true));
	}

	@Override
	public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2,
									 int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
		clientHandler.sendMessage(new WhiteMarbleRequest( fromWhiteMarble1,  fromWhiteMarble2, whiteMarblesInput1,
				whiteMarblesInput2, howMany));
	}

	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		clientHandler.sendMessage(new DepotReply(depot, initialMove, confirmationAvailable, inputValid));
	}

	@Override
	public void askBuyCardAction(String nickname, ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
		clientHandler.sendMessage(new BuyableDevCards(nickname,cardsAvailable, wrongSlot));
	}

	@Override
	public void askProductionAction(String nickname,ArrayList<Productions> productionAvailable) {
		clientHandler.sendMessage(new ProductionsAvailable(nickname,productionAvailable));
	}

	@Override
	public void askFreeInput(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 1));
	}

	@Override
	public void askFreeOutput(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 2));
	}

	@Override
	public void showNicknameConfirmation(boolean nicknameConfirmation) {
		clientHandler.sendMessage(new NicknameConfirmation(nicknameConfirmation));
	}

	@Override
	public void showLobbyConfirmation(boolean lobbyAccessed) {
		clientHandler.sendMessage(new LobbyConfirmation(lobbyAccessed));
	}

	@Override
	public void showGenericMessage(String genericMessage) {
		clientHandler.sendMessage(new GenericMessage(genericMessage));
	}

	@Override
	public void disconnection(String text, boolean termination) {
		clientHandler.sendMessage(new DisconnectionMessage(text, termination));
	}

	@Override
	public void showError(String error) {
		clientHandler.sendMessage(new ErrorMessage(error));
	}

	@Override
	public void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {
		clientHandler.sendMessage(new PlayerCardsAndProductionShow(nickname, cardProductionsManagement));
	}

	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		clientHandler.sendMessage(new CardsDeckShow(deck));
	}

	@Override
	public void showStrongBox(String nickname, ReducedStrongbox strongbox) {
		clientHandler.sendMessage(new StrongboxShow(nickname, strongbox));
	}

	@Override
	public void showFaithTrack(String nickname, ReducedFaithTrack faithTrack) {
		clientHandler.sendMessage(new FaithTrackShow(nickname, faithTrack));
	}

	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
		clientHandler.sendMessage(new DepotShow(nickname, depot));
	}

	@Override
	public void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		clientHandler.sendMessage(new LeaderInteractions(nickname, availableLeaders, 1));
	}

	@Override
	public void showMarket(ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow("",market, false));
	}

	@Override
	public void showMatchInfo(ArrayList<String> players) {
		clientHandler.sendMessage(new MatchInfo(players));
	}

	public void showLobbyList(ArrayList<String> lobbyList, int idVersion){
		clientHandler.sendMessage(new LobbyList(lobbyList, idVersion));
	}

	@Override
	public void showWinMessage(String winner, int points) {
		clientHandler.sendMessage(new WinMessage(winner,points));
	}

	@Override
	public void showToken(TokenType token, Colors color){
		clientHandler.sendMessage(new TokenShow(token,color));
	}

	@Override
	public void connectionError() {
	}
	
}
