package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.observers.ViewObserver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SceneSwitcher extends ViewObservable {
	
	private Scene activeScene;
	
	public SceneSwitcher(Scene scene) {
		this.activeScene = scene;
	}
	
	public void changeRootPane(ArrayList<ViewObserver> observerList, Scene scene, String fxml) {
		try {
			FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/fxml/" + fxml));
			Parent root = loader.load();
			ViewObservable controller = loader.getController();
			controller.addAllObservers(observerList);
			
			//activeController = (GenericSceneController) controller;
			activeScene = scene;
			activeScene.setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeRootPane(ArrayList<ViewObserver> observerList, String fxml) {
		changeRootPane(observerList, activeScene, fxml);
	}
	
}
