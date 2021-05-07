package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class TestFX extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hello, World!");
		primaryStage.show();
	}
}
