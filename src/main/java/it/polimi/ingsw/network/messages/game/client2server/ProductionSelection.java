package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * the client sends selected production
 */
public class ProductionSelection extends Message {

	private final ArrayList<Integer> selectedProductions;
	private final ArrayList<Integer> resourcesInputNumber;
	private final ArrayList<Resources> resourcesInputList;
	private final ArrayList<Integer> resourcesOutputNumber;
	private final ArrayList<Resources> resourcesOutputList;

	public ProductionSelection(String nickname, ArrayList<Integer> selectedProductions,
							   ArrayList<Resources> resourcesInputList, ArrayList<Integer> resourcesInputNumber,
							   ArrayList<Resources> resourcesOutputList, ArrayList<Integer> resourcesOutputNumber) {
		super(nickname, MessageType.PRODUCTION_SELECTION);
		this.selectedProductions = selectedProductions;
		this.resourcesInputNumber= resourcesInputNumber;
		this.resourcesInputList= resourcesInputList;
		this.resourcesOutputList= resourcesOutputList;
		this.resourcesOutputNumber= resourcesOutputNumber;

	}

	public ArrayList<Integer> getSelectedProductions() {
		return selectedProductions;
	}

	public ArrayList<Integer> getResourcesInputNumber() {
		return resourcesInputNumber;
	}

	public ArrayList<Resources> getResourcesInputList() {
		return resourcesInputList;
	}

	public ArrayList<Integer> getResourcesOutputNumber() {
		return resourcesOutputNumber;
	}

	public ArrayList<Resources> getResourcesOutputList() {
		return resourcesOutputList;
	}
}
