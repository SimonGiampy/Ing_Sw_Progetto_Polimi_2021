package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientSideController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppGui extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/connection.fxml"));
		Parent rootLayout;
		try {
			rootLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
		
		Scene scene = new Scene(rootLayout);
		
		SceneSwitcher switcher = new SceneSwitcher(scene);
		GUI gui = new GUI(switcher);
		ClientSideController clientController = new ClientSideController(gui);
		gui.attach(clientController);
		
		Connection controller = loader.getController();
		controller.attach(clientController);
		
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Masters of Renaissance");
		stage.show();
	}
	
	@Override
	public void stop() {
		Platform.exit();
		System.exit(0);
	}
}
