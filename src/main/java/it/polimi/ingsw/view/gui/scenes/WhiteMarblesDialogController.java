package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.util.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class WhiteMarblesDialogController implements SceneController {

	@FXML Label whiteMarblesLbl;
	@FXML private MenuButton lead1;
	@FXML private MenuButton lead2;
	@FXML private ImageView res1;
	@FXML private ImageView res2;
	@FXML Label lbl;
	
	private Integer[] quantities;
	
	@FXML
	public void initialize() {
		quantities = new Integer[] {0, 0};
	}
	
	/**
	 * sets the contents of this dialog window
	 * @param fromWhiteMarble1 resources transformed by the first leader
	 * @param fromWhiteMarble2 resources transformed by the second leader
	 * @param howMany number of white marbles incoming from the market
	 */
	public void setData(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int howMany) {
		res1.setImage(new Image(fromWhiteMarble1.get(0).path));
		res2.setImage(new Image(fromWhiteMarble2.get(0).path));
		whiteMarblesLbl.setText("White Marbles to convert: " + howMany);
		setMenus(lead1, howMany, "Leader 1 Activations: ");
		setMenus(lead2, howMany, "Leader 2 Activations: ");
	}
	
	/**
	 * sets the menu buttons items lists
	 * @param button menu button corresponding to a leader
	 * @param num number of white marbles incoming from the market
	 */
	private void setMenus(MenuButton button, int num, String text) {
		ArrayList<RadioMenuItem> items = new ArrayList<>();
		ToggleGroup group = new ToggleGroup();
		for (int i = 0; i <= num; i++) {
			RadioMenuItem item = new RadioMenuItem(String.valueOf(i));
			int finalI = i;
			item.setOnAction(actionEvent -> button.setText(text + finalI));
			item.setToggleGroup(group);
			items.add(item);
		}
		button.getItems().addAll(items);
	}
	
	/**
	 * quantities that the dialog returns
	 * @return integer array
	 */
	public Integer[] getQuantities() {
		quantities[0] = selectedNumber(lead1);
		quantities[1] = selectedNumber(lead2);
		return quantities;
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
}
