package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

// implemented by ClientController,GameController, VirtualView
public interface Observer {

	/**
	 * generic method of update
	 */
	void update(Message message);
}
