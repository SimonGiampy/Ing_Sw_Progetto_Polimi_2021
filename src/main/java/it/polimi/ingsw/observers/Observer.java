package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.messages.Message;

// implemented by ClientSideController,ServerSideController, VirtualView
public interface Observer {

	/**
	 * generic method of update
	 */
	void update(Message message);
}
