package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;

public class Observable {

	private final ArrayList<Observer> observerList = new ArrayList<>();

	public void attach(Observer observer){
		observerList.add(observer);
	}

	public void detach(Observer observer){
		observerList.remove(observer);
	}

	public void notifyObserver(Message message){
		for(Observer observer: observerList){
			observer.update(message);
		}
	}

}
