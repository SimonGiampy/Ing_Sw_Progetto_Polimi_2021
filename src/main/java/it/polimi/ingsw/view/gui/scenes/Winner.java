package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Winner extends ViewObservable implements SceneController {

	@FXML Text winner;
	@FXML Text nickname;
	@FXML Text points;


	@FXML
	public void initialize() {
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 10);
	  	winner.setEffect(new Glow(1));
	}

	/**
	 * it sets winner text
	 * @param winner is the selected String
	 */
	public void setWinner(String winner) {
		this.winner.setText(winner);
	}

	/**
	 * it sets nickname text
	 * @param nickname is the nickname of the winner
	 */
	public void setNickname(String nickname){
		this.nickname.setText(nickname);
	}

	/**
	 * it sets points text
	 * @param points is the number of points of the winner
	 */
	public void setPoints(String points){
		this.points.setText(points);
	}
}
