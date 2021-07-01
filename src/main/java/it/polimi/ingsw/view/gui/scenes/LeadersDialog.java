package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class LeadersDialog extends Dialog<ArrayList<Integer>> {
	
	/**
	 *
	 * @param leaderCards
	 */
	public LeadersDialog(ArrayList<ReducedLeaderCard> leaderCards) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("it/polimi/ingsw/view/gui/choose_leaders.fxml"));
			DialogPane dialogPane = loader.load();
			LeadersDialogController controller = loader.getController();
			
			ButtonType t = new ButtonType("Confirm Leaders", ButtonBar.ButtonData.OK_DONE);
			dialogPane.getButtonTypes().addAll(t);
			Button confirm = (Button) dialogPane.lookupButton(t);
			confirm.setCancelButton(true);
			
			controller.setLeaders(leaderCards);
			controller.setConfirmButton(confirm);
			
			setDialogPane(dialogPane); // sets the content of the dialog window
			setTitle("Choose 2 Leader Cards");
			
			setResultConverter(buttonType -> controller.getSelectedLeaders()); // converts the output in a list of resources
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
