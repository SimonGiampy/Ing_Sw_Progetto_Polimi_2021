package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class LeadersDialogController implements SceneController {
	
	@FXML private ImageView img1;
	@FXML private ImageView img2;
	@FXML private ImageView img3;
	@FXML private ImageView img4;
	
	private Button confirm;
	
	private ArrayList<ImageView> selection;
	
	@FXML
	public void initialize() {
		selection = new ArrayList<>();
	}
	
	public void setLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
		img1.setImage(new Image("/assets/leaderCards/" + leaderCards.get(0).getIdNumber() + ".png"));
		img2.setImage(new Image("/assets/leaderCards/" + leaderCards.get(1).getIdNumber() + ".png"));
		img3.setImage(new Image("/assets/leaderCards/" + leaderCards.get(2).getIdNumber() + ".png"));
		img4.setImage(new Image("/assets/leaderCards/" + leaderCards.get(3).getIdNumber() + ".png"));
		
		img1.setOnMouseClicked(event -> applyGlow(img1));
		img2.setOnMouseClicked(event -> applyGlow(img2));
		img3.setOnMouseClicked(event -> applyGlow(img3));
		img4.setOnMouseClicked(event -> applyGlow(img4));
		
	}
	
	private void applyGlow(ImageView img) {
		if (selection.contains(img)) {
			img.setEffect(new Glow(0));
			selection.remove(img);
		} else {
			img.setEffect(new Glow(0.5));
			selection.add(img);
		}
		
		confirm.setDisable(selection.size() != 2);
	}
	
	
	public void setConfirmButton(Button button) {
		this.confirm = button;
		confirm.setDisable(true);
		confirm.getStyleClass().add("success");
	}
	
	public ArrayList<Integer> getSelectedLeaders() {
		ArrayList<Integer> integers = new ArrayList<>();
		if (selection.contains(img1)) integers.add(1);
		if (selection.contains(img2)) integers.add(2);
		if (selection.contains(img3)) integers.add(3);
		if (selection.contains(img4)) integers.add(4);
		return integers;
	}
}
