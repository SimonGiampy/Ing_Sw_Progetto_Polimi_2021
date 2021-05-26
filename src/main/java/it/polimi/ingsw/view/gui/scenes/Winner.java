package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.text.Text;

public class Winner extends ViewObservable implements SceneController {

	@FXML Text winner;
	@FXML Text nickname;
	@FXML Text points;


	  @FXML public void initialize(){
	  	winner.setEffect(new Glow(1));
	  }
	public void setWinner(String winner) {
		this.winner.setText(winner);
	}

	public void setNickname(String nickname){
		this.nickname.setText(nickname);
	}

	public void setPoints(String points){
		this.points.setText(points);
	}
}
