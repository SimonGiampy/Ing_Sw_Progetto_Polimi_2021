package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scenes.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class AppGui extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/it/polimi/ingsw/view/gui/connection.fxml"));
		Parent rootLayout;
		try {
			rootLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Scene scene = new Scene(rootLayout);
		GUI gui = new GUI(scene, stage);

		Connection controller = loader.getController();
		controller.setGui(gui);

		stage.getIcons().add(new Image("/assets/board/calamaio.png")); // game icon in the taskbar
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setMaximized(true);
		stage.setFullScreen(true);
		stage.setTitle("Masters of Renaissance");
		stage.setOnCloseRequest((WindowEvent t) -> {
			Platform.exit();
			System.exit(0);
		});
		stage.show();
	}
	
	@Override
	public void stop() {
		Platform.exit();
		System.exit(0);
	}
}
