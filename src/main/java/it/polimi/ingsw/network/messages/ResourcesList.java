package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

public class ResourcesList extends Message {
	private final ArrayList<Resources> resourcesList;
	private final ArrayList<Integer> resourcesNumber;

	public ResourcesList(String nickname, ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber){
		super(nickname,MessageType.RESOURCES_LIST);
		this.resourcesList=resourcesList;
		this.resourcesNumber=resourcesNumber;
	}
}
