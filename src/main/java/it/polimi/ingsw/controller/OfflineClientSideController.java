package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.game.client2server.ActionReply;
import it.polimi.ingsw.network.messages.game.client2server.LeaderSelection;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class OfflineClientSideController extends ClientSideController implements ViewObserver, Observer {
	private final ServerSideController controller;
	private final String nickname;
	public OfflineClientSideController(View view,ServerSideController controller, String nickname) {
		super(view);
		this.controller=controller;
		this.nickname=nickname;

	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {
		System.out.println("dfsdfs");
		controller.onMessageReceived(new LeaderSelection(nickname,selectedLeaders));

	}

	@Override
	public void onUpdateLeaderAction(int selectedLeader, int action) {
	}

	@Override
	public void onUpdateAction(PlayerActions selectedAction) {
		controller.onMessageReceived(new ActionReply(nickname,selectedAction));
	}

	@Override
	public void onUpdateMarketAction(String which, int where) {
		super.onUpdateMarketAction(which, where);
	}

	@Override
	public void onUpdateWhiteMarbleChoice(int quantity1, int quantity2) {
		super.onUpdateWhiteMarbleChoice(quantity1, quantity2);
	}

	@Override
	public void onUpdateDepotMove(String fromWhere, String toWhere, int origin, int destination, boolean confirmation) {
		super.onUpdateDepotMove(fromWhere, toWhere, origin, destination, confirmation);
	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level, int slot) {
		super.onUpdateBuyCardAction(color, level, slot);
	}

	@Override
	public void onUpdateProductionAction(ArrayList<Productions> selectedProduction) {
		super.onUpdateProductionAction(selectedProduction);
	}

	@Override
	public void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber, int flag) {
		super.onUpdateResourceChoice(resourcesList, resourcesNumber, flag);
	}

	@Override
	public void onDisconnection() {
		super.onDisconnection();
	}
}
