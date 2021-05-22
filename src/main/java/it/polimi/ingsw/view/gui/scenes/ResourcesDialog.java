package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.util.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class ResourcesDialog extends Dialog<ArrayList<Resources>> {
	
	/**
	 * Constructor of the dialog window
	 * @param number of resources that the user needs to choose
	 * @param prompt to be shown in a label on the top of the window
	 */
	public ResourcesDialog(int number, String prompt) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("it/polimi/ingsw/view/gui/choose_resources.fxml"));
			DialogPane dialogPane = loader.load();
			ResourcesDialogController controller = loader.getController();
			
			ButtonType t = new ButtonType("Confirm Choices", ButtonBar.ButtonData.OK_DONE);
			dialogPane.getButtonTypes().addAll(t);
			Button confirm = (Button) dialogPane.lookupButton(t);
			
			controller.setNumberOfResources(number, prompt);
			controller.setConfirmButton(confirm);
			
			setDialogPane(dialogPane); // sets the content of the dialog window
			setTitle("Choose the resources");
			
			setResultConverter(buttonType -> controller.getSelectedResources()); // converts the output in a list of resources
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
}
