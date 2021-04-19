package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;

import java.util.ArrayList;

public class ClientController implements ViewObserver, Observer {
	@Override
	public void update(Message message) {

	}

	@Override
	public void onUpdateNickname(String nickname) {

	}

	@Override
	public void onUpdatePlayersNumber(int playerNumber) {

	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateLeaderAction(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateAction(ArrayList<Integer> availableAction) {

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
