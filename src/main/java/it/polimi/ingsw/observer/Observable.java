package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

//extended by GameMechanics, and others classes
public class Observable {

	private final ArrayList<Observer> observerList = new ArrayList<>();

	/**
	 * it adds an observer to the list
	 * @param observer is the observer to be added
	 */
	public void attach(Observer observer){
		observerList.add(observer);
	}

	/**
	 * it removes an observer from the list
	 * @param observer is the observer to be removed
	 */
	public void detach(Observer observer){
		observerList.remove(observer);
	}

	/**
	 * it notifies all the current observers
	 * @param message is the message to be passed to the observers
	 */
	public void notifyObserver(Message message){
		for(Observer observer: observerList){
			observer.update(message);
		}
	}

}
