package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.model.util.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class WhiteMarblesDialog extends Dialog<Integer[]> {

	/**
	 * creates a new dialog window for asking how many and which leader actions to apply when receiving multiple white marbles
	 * and when two white marble leaders are activated
	 * @param fromWhiteMarble1 list of resources of the ability the first leader
	 * @param fromWhiteMarble2 list of resources of the ability the second leader
	 * @param howMany how many marbles needs to be transformed
	 */
	public WhiteMarblesDialog(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int howMany) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("it/polimi/ingsw/view/gui/white_marbles_dialog.fxml"));
			DialogPane dialogPane = loader.load();
			WhiteMarblesDialogController controller = loader.getController();
			
			ButtonType t = new ButtonType("Confirm the quantities", ButtonBar.ButtonData.OK_DONE);
			dialogPane.getButtonTypes().addAll(t);
			Button confirm = (Button) dialogPane.lookupButton(t);
			confirm.setCancelButton(true);
			
			controller.setData(fromWhiteMarble1, fromWhiteMarble2, howMany);
			
			setDialogPane(dialogPane); // sets the content of the dialog window
			setTitle("Choose the White Marbles abilities parameters");
			
			setResultConverter(buttonType -> controller.getQuantities()); // converts the output in a list of resources
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
