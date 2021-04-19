package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.util.Colors;

import java.util.ArrayList;

//TODO: add CustomGame method

public interface ViewObserver {

	void onUpdateNickname(String nickname);

	void onUpdatePlayersNumber(int playerNumber);

	void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders);

	void onUpdateLeaderAction(ArrayList<Integer> selectedLeaders);

	void onUpdateAction(ArrayList<Integer> availableAction);

	void onUpdateMarketAction(String which, int where);

	void onUpdateDepotMove(); // TODO: incomplete

	void onUpdateBuyCardAction(Colors color, int level);

	void onUpdateProductionAction(ArrayList<Integer> selectedProduction);

	void onDisconnection();
}
