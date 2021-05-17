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

public class ResourcesDialogController implements SceneController {
	
	@FXML private MenuButton numServants;
	@FXML private MenuButton numStones;
	@FXML private MenuButton numShields;
	@FXML private MenuButton numCoins;
	
	private Button confirm;
	
	private int number; // number of resources to reach in total
	private ArrayList<Resources> selected;
	
	// update the list and the button state whenever a new choice is clicked in the menu items buttons
	
	protected void setNumberOfResources(int number) {
		this.number = number;
		// adds the option to the menus
		setMenus(numCoins, "Coins: ", Resources.COIN);
		setMenus(numStones, "Stones: ", Resources.STONE);
		setMenus(numServants, "Servants: ", Resources.SERVANT);
		setMenus(numShields, "Shields: ", Resources.SHIELD);
		
	}
	
	protected void setConfirmButton(Button confirmButton) {
		confirmButton.setDisable(true);
		this.confirm = confirmButton;
	}
	
	@FXML
	public void initialize() {
		selected = new ArrayList<>();
	}
	
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
	
	private void checkValidity() {
		RadioMenuItem menuItem = (RadioMenuItem) numCoins.getItems().get(1);
		menuItem.isSelected();
		
		int num = selectedNumber(numCoins) + selectedNumber(numServants) + selectedNumber(numStones) + selectedNumber(numShields);
		confirm.setDisable(num != number);
	}
	
	private int selectedNumber(MenuButton m) {
		return Integer.parseInt(m.getItems().stream().filter(x -> ((RadioMenuItem) x).isSelected())
				.map(MenuItem::getText).reduce("0", (s, s2) -> s + s2));
	}
	
	public ArrayList<Resources> getSelectedResources() {
		return selected;
	}
}
