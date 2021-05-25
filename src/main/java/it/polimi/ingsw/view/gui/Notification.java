package it.polimi.ingsw.view.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class Notification {

	public Label lblText;
	public Button btnClose;
	public AnchorPane anchorPane;
	public ImageView img;
	private AnchorPane root;
	private final boolean[] notificationStack;
	private int index;

	public Notification(AnchorPane root, String text, boolean[] notificationStack){
		this.notificationStack = notificationStack;
		index = 0;
		while(!notificationStack[index])
			index++;
		notificationStack[index] = false;
		double posX = 1420;
		double posY = 980;
		initNotification(root, text, posX, posY - 108*index);
	}

	@FXML
	public void initialize(){
		btnClose.setOnMouseClicked(e -> dismiss());
	}


	public void initNotification(AnchorPane root, String text, double layoutX, double layoutY){
		Node node;
		this.root = root;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/notification.fxml"));

			fxmlLoader.setController(this);
			node = fxmlLoader.load();

			anchorPane.setLayoutX(layoutX);
			anchorPane.setLayoutY(layoutY);
			root.getChildren().addAll(node);

			setNotification(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNotification(String text){
		lblText.setText(text);
	}

	public void dismiss(){
		anchorPane.setVisible(false);
		notificationStack[index] = true;
		root.getChildren().remove(anchorPane);
	}

	public void showNotification(){
		anchorPane.setVisible(true);
		lblText.setVisible(true);
		btnClose.setVisible(true);
		img.setVisible(true);
	}

	//TODO: code for animations, i have to understand how to adapt it
	/*

	private Timeline setupDismissAnimation() {


		Timeline tl = new Timeline();

		KeyValue kv1 = new KeyValue(1420, );
		KeyFrame kf1 = new KeyFrame(Duration.millis(2000), kv1);

		KeyValue kv2 = new KeyValue(stage.opacityProperty(), 0.0);
		KeyFrame kf2 = new KeyFrame(Duration.millis(2000), kv2);

		tl.getKeyFrames().addAll(kf1, kf2);

		tl.setOnFinished(e -> {
			trayIsShowing = false;
			stage.close();
			stage.setLocation(stage.getBottomRight());
		});

		return tl;
	}

	private Timeline setupShowAnimation() {

		Timeline tl = new Timeline();

		KeyValue kv1 = new KeyValue(stage.yLocationProperty(), stage.getBottomRight().getY() + stage.getWidth());
		KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);

		KeyValue kv2 = new KeyValue(stage.yLocationProperty(), stage.getBottomRight().getY());
		KeyFrame kf2 = new KeyFrame(Duration.millis(1000), kv2);

		KeyValue kv3 = new KeyValue(stage.opacityProperty(), 0.0);
		KeyFrame kf3 = new KeyFrame(Duration.ZERO, kv3);

		KeyValue kv4 = new KeyValue(stage.opacityProperty(), 1.0);
		KeyFrame kf4 = new KeyFrame(Duration.millis(2000), kv4);

		tl.getKeyFrames().addAll(kf1, kf2, kf3, kf4);

		tl.setOnFinished(e -> trayIsShowing = true);

		return tl;
	}
		 */
}
