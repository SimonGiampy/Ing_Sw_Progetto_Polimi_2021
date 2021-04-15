package it.polimi.ingsw.message;

import it.polimi.ingsw.util.Resources;

import java.util.ArrayList;

public class ResourcesListMessage extends Message {
	private final ArrayList<Resources> resourcesList;
	private final ArrayList<Integer> resourcesNumber;

	public ResourcesListMessage(String nickname, ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber){
		super(nickname,MessageType.RESOURCES_LIST_MESSAGE);
		this.resourcesList=resourcesList;
		this.resourcesNumber=resourcesNumber;
	}
}
