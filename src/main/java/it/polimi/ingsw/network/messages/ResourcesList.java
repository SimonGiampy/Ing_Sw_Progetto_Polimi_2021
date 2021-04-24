package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

/**
 * the client sends selected resources
 */
public class ResourcesList extends Message {
	private final ArrayList<Resources> resourcesList;
	private final ArrayList<Integer> resourcesNumber;

	public ResourcesList(String nickname, ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber){
		super(nickname,MessageType.RESOURCES_LIST);
		this.resourcesList=resourcesList;
		this.resourcesNumber=resourcesNumber;
	}

	public ArrayList<Resources> getResourcesList() {
		return resourcesList;
	}

	public ArrayList<Integer> getResourcesNumber() {
		return resourcesNumber;
	}
}
