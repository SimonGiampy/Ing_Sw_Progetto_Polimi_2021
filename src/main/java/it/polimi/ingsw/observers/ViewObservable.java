package it.polimi.ingsw.observers;

import java.util.ArrayList;
import java.util.function.Consumer;


//extended by CLI and GUI
public abstract class ViewObservable {
	protected final ArrayList<ViewObserver> observers= new ArrayList<>();

	public void attach(ViewObserver observer){
		observers.add(observer);
	}

	public void addAllObservers(ArrayList<ViewObserver> observerList) {
		observers.addAll(observerList);
	}

	public void detach(ViewObserver observer) {
		observers.remove(observer);
	}

	public void removeAllObservers(ArrayList<ViewObserver> observerList) {
		observers.removeAll(observerList);
	}
	
	/**
	 * this is crazy furious
	 * @param lambda expression that executes the desired function
	 */
	protected void notifyObserver(Consumer<ViewObserver> lambda) {
		for (ViewObserver observer : observers) {
			lambda.accept(observer);
		}
	}
	
}
