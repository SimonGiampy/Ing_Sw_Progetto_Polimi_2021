package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class OfflineController extends ClientSideController implements ViewObserver, Observer {
	
	private final ServerSideController controller;
	private final String nickname;
	
	public OfflineController(View view, ServerSideController controller, String nickname) {
		super(view);
		this.controller=controller;
		this.nickname=nickname;
	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {
		controller.onMessageReceived(new LeaderSelection(nickname,selectedLeaders));
	}

	@Override
	public void onUpdateLeaderAction(int selectedLeader, int action) {
		controller.onMessageReceived(new LeaderAction(nickname,selectedLeader,action));
	}

	@Override
	public void onUpdateAction(PlayerActions selectedAction) {
		controller.onMessageReceived(new ActionReply(nickname,selectedAction));
	}

	@Override
	public void onUpdateMarketAction(String which, int where) {
		controller.onMessageReceived(new MarketInteraction(nickname,which,where));
	}

	@Override
	public void onUpdateWhiteMarbleChoice(int quantity1, int quantity2) {
		controller.onMessageReceived(new WhiteMarbleReply(nickname,quantity1,quantity2));
	}

	@Override
	public void onUpdateDepotMove(String fromWhere, String toWhere, int origin, int destination, boolean confirmation) {
		controller.onMessageReceived(new DepotInteraction(nickname,fromWhere,toWhere,origin,destination,confirmation));
	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level, int slot) {
		controller.onMessageReceived(new BuyCard(nickname,level,color,slot));
	}

	@Override
	public void onUpdateProductionAction(ArrayList<Productions> selectedProduction) {
		controller.onMessageReceived(new ProductionSelection(nickname,selectedProduction));
	}

	@Override
	public void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber, int flag) {
		controller.onMessageReceived(new ResourcesList(nickname,resourcesList,resourcesNumber,flag));
	}

	@Override
	public void onDisconnection() {
		super.onDisconnection();
	}
}
