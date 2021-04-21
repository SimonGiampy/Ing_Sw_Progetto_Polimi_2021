package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.network.server.SocketClient;
import it.polimi.ingsw.network.messages.LoginRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PlayerNumberReply;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class ClientController implements ViewObserver, Observer {

	private SocketClient client;
	private View view;
	private String nickname;

	public ClientController(String address, int port, View view){
		client = new SocketClient(address, port);
		this.view = view;
	}

	@Override
	public void update(Message message) {

	}

	@Override
	public void onUpdateNickname(String nickname) {
		this.nickname = nickname;
		client.sendMessage(new LoginRequest(nickname));
	}

	@Override
	public void onUpdatePlayersNumber(int playerNumber) {
		client.sendMessage(new PlayerNumberReply(nickname, playerNumber));
	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateLeaderAction(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateAction(int selectedAction) {

	}

	@Override
	public void onUpdateMarketAction(String which, int where) {

	}

	@Override
	public void onUpdateDepotMove() {

	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level) {

	}

	@Override
	public void onUpdateProductionAction(ArrayList<Integer> selectedProduction) {

	}

	@Override
	public void onDisconnection() {

	}
}
