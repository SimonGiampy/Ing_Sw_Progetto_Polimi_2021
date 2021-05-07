package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TestFX extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/test1.fxml"));
		Parent rootLayout = null;
		try {
			rootLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TestFX controller = loader.getController();
		//controller.addObserver(clientController);
		
		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		stage.setScene(scene);
		stage.setWidth(1280d);
		stage.setHeight(720d);
		stage.setTitle("Masters Game");
		stage.show();
	}
	
	@FXML
	public void suca(MouseEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete  ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.YES) {
			//do stuff
		}
	}
}
