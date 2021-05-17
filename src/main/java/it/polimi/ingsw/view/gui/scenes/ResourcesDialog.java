package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.model.util.Resources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ResourcesDialog extends Dialog<ArrayList<Resources>> {
	
	private int number; // number of resources to reach in total
	private ArrayList<Resources> selected;
	
	public ResourcesDialog(int number) {
		this.number = number;
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("it/polimi/ingsw/view/gui/resources_choices.fxml"));
			DialogPane dialogPane = loader.load();
			ResourcesDialogController controller = loader.getController();
			
			ButtonType t = new ButtonType("Confirm Choices", ButtonBar.ButtonData.OK_DONE);
			dialogPane.getButtonTypes().addAll(t);
			Button confirm = (Button) dialogPane.lookupButton(t);
			//confirm.setDisable(true);
			
			controller.setNumberOfResources(number);
			controller.setConfirmButton(confirm);
			//controller.initialize();
			
			setDialogPane(dialogPane);
			setTitle("Choose the resources");
			
			
			
			//selected = new ArrayList<>();
			//selected.add(Resources.COIN);
			
			setResultConverter(buttonType -> controller.getSelectedResources());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
}
