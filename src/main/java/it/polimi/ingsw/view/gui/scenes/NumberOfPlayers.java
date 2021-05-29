package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class NumberOfPlayers extends ViewObservable implements SceneController {

	@FXML public ImageView onePlayer;
	@FXML public ImageView twoPlayers;
	@FXML public ImageView threePlayers;
	@FXML public ImageView fourPlayers;

	private ImageView[] images;

	@FXML public void initialize(){
		images= new ImageView[]{onePlayer, twoPlayers, threePlayers, fourPlayers};

		for (int i = 0; i < images.length; i++) {
			int finalI = i;
			images[i].setOnMouseEntered(event->mouseEntered(images[finalI]));
			images[i].setOnMouseExited(event->mouseExited(images[finalI]));
			images[i].setOnMouseClicked(event->mouseClicked(finalI+1));
		}
	}

	/**
	 * it handles mouse entered animation
	 * @param image is the selected imageView
	 */
	public void mouseEntered(ImageView image){
		setShadow(0.5,image);
	}

	/**
	 * it handles mouse exited animation
	 * @param image is the selected imageView
	 */
	public void mouseExited(ImageView image){
		image.setEffect(new Glow(0));
	}

	/**
	 * it handles mouse click on the selected image
	 * @param number is the number of players
	 */
	public void mouseClicked(int number){
		notifyObserver(obs->obs.onUpdatePlayersNumber(number));
	}

	/**
	 * it adds a shadow to the selected image
	 * @param value is the intensity of the shadow
	 * @param img is the selected image
	 */
	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.WHITE);
		img.setEffect(dropShadow);
	}

}
