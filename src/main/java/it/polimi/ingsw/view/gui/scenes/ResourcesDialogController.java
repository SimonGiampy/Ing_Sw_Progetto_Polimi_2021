package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * controller class for asking a number of resources
 */
public class ResourcesDialogController implements SceneController {
	
	@FXML private MenuButton numServants;
	@FXML private MenuButton numStones;
	@FXML private MenuButton numShields;
	@FXML private MenuButton numCoins;
	@FXML private Label lbl;
	
	private Button confirm;
	
	private int number; // number of resources to reach in total
	private ArrayList<Resources> selected; // resources chosen by the user
	
	// update the list and the button state whenever a new choice is clicked in the menu items buttons
	
	/**
	 * setting parameters after dialog is created
	 * @param number of resources the user chooses
	 * @param prompt to be displayed in the label on the top
	 */
	protected void setNumberOfResources(int number, String prompt) {
		this.number = number;
		lbl.setText(prompt + "\nConfirm your choices when you're done.");
		
		// adds the option to the menus
		setMenus(numCoins, "Coins: ", Resources.COIN);
		setMenus(numStones, "Stones: ", Resources.STONE);
		setMenus(numServants, "Servants: ", Resources.SERVANT);
		setMenus(numShields, "Shields: ", Resources.SHIELD);
		
	}
	
	/**
	 * memorizes the button used for confirmation (button type created in the dialog)
	 * @param confirmButton button
	 */
	protected void setConfirmButton(Button confirmButton) {
		confirmButton.setDisable(true);
		this.confirm = confirmButton;
		confirm.getStyleClass().add("success");
	}
	
	@FXML
	public void initialize() {
		selected = new ArrayList<>();
	}
	
	/**
	 * sets the display text, listeners, and checkers for a menu button, for each item
	 * @param button menu from where to choose the resources
	 * @param text to be displayed on it when a selection occurs
	 * @param res associated resource
	 */
	private void setMenus(MenuButton button, String text, Resources res) {
		ArrayList<RadioMenuItem> items = new ArrayList<>();
		ToggleGroup group = new ToggleGroup();
		for (int i = 0; i <= number; i++) {
			RadioMenuItem item = new RadioMenuItem(String.valueOf(i));
			int finalI = i;
			item.setOnAction(actionEvent -> {
				button.setText(text + finalI);
				assignSelected(res, finalI);
				checkValidity();
			});
			item.setToggleGroup(group);
			items.add(item);
		}
		button.getItems().addAll(items);
	}
	
	/**
	 * updates the selected list of resources
	 * @param res single resource to be updated
	 * @param num new number associated
	 */
	private void assignSelected(Resources res, int num) {
		int count = ListSet.count(selected, res);
		if (count > num) {
			for (int i = 0; i < count - num; i++) {
				selected.remove(res);
			}
		} else if (count < num) {
			for (int i = 0; i < num - count; i++) {
				selected.add(res);
			}
		}
	}
	
	/**
	 * checks the validity of the user input and enables the confirmation button accordingly
	 */
	private void checkValidity() {
		RadioMenuItem menuItem = (RadioMenuItem) numCoins.getItems().get(1);
		menuItem.isSelected();
		
		int num = selectedNumber(numCoins) + selectedNumber(numServants) + selectedNumber(numStones) + selectedNumber(numShields);
		confirm.setDisable(num != number);
	}
	
	/**
	 * gets the number selected by the user
	 * @param m menu button pressed
	 * @return integer corresponding to the selection
	 */
	private int selectedNumber(MenuButton m) {
		return Integer.parseInt(m.getItems().stream().filter(x -> ((RadioMenuItem) x).isSelected())
				.map(MenuItem::getText).reduce("0", (s, s2) -> s + s2));
	}
	
	/**
	 * result of this dialog window
	 * @return list of resources
	 */
	public ArrayList<Resources> getSelectedResources() {
		return selected;
	}
}
