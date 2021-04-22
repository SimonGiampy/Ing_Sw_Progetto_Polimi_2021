package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.function.Consumer;


//extended by CLI and GUI
public abstract class ViewObservable {
	protected final ArrayList<ViewObserver> observers= new ArrayList<>();

	public void addObserver(ViewObserver observer){
		observers.add(observer);
	}

	public void addAllObservers(ArrayList<ViewObserver> observerList) {
		observers.addAll(observerList);
	}

	public void removeObserver(ViewObserver observer) {
		observers.remove(observer);
	}

	public void removeAllObservers(ArrayList<ViewObserver> observerList) {
		observers.removeAll(observerList);
	}

	//TODO: è palese che sta cosa sia copiata ahahhaha
	protected void notifyObserver(Consumer<ViewObserver> lambda) {
		for (ViewObserver observer : observers) {
			lambda.accept(observer);
		}
	}
}