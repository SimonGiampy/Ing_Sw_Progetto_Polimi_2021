package it.polimi.ingsw.view.gui;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
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
	private final double layoutY;
	private Timeline showAnimation;

	public Notification(AnchorPane root, String text, boolean[] notificationStack){
		this.notificationStack = notificationStack;
		index = 0;
		while(!notificationStack[index])
			index++;
		notificationStack[index] = false;
		double posX = 1420;
		double posY = 980;
		layoutY = posY - 108*index;
		initNotification(root, text, posX);
	}

	@FXML
	public void initialize(){
		btnClose.setOnMouseClicked(e -> dismiss());
	}


	public void initNotification(AnchorPane root, String text, double layoutX){
		Node node;
		this.root = root;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/notification.fxml"));

			fxmlLoader.setController(this);
			node = fxmlLoader.load();

			root.getChildren().addAll(node);
			setNotification(text);

			anchorPane.setLayoutX(layoutX);

			showAnimation = setupShowAnimation();

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
		showAnimation.play();
	}

	//TODO: code for animations, i have to understand how to adapt it


	private Timeline setupShowAnimation() {

		Timeline tl = new Timeline();

		/*

		Animazione rimbalzina
		//Sets the x location of the tray off the screen
		KeyValue kvX = new KeyValue(anchorPane.layoutYProperty(), 1080);
		KeyFrame kf1 = new KeyFrame(Duration.ZERO, kvX);

		//Animates the Tray onto the screen and interpolates at a tangent for 300 millis
		Interpolator interpolator = Interpolator.TANGENT(Duration.millis(300), 50);
		KeyValue kvInter = new KeyValue(anchorPane.layoutYProperty(), layoutY, interpolator);
		KeyFrame kf2 = new KeyFrame(Duration.millis(1300), kvInter);

		 */

		KeyValue kv1 = new KeyValue(anchorPane.layoutYProperty(), 1080);
		KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);

		KeyValue kv2 = new KeyValue(anchorPane.layoutYProperty(), layoutY);
		KeyFrame kf2 = new KeyFrame(Duration.millis(800), kv2);


		KeyValue kv3 = new KeyValue(anchorPane.opacityProperty(), 0.0);
		KeyFrame kf3 = new KeyFrame(Duration.ZERO, kv3);

		KeyValue kv4 = new KeyValue(anchorPane.opacityProperty(), 1.0);
		KeyFrame kf4 = new KeyFrame(Duration.millis(800), kv4);

		tl.getKeyFrames().addAll(kf1, kf2, kf3, kf4);

		return tl;
	}

	/*
	private Timeline setupDismissAnimation() {


		Timeline tl = new Timeline();

		KeyValue kv1 = new KeyValue();
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

	 */

	private SimpleDoubleProperty getPaneYProperty(){
		SimpleDoubleProperty y = new SimpleDoubleProperty();
		return y;
	}

	private SimpleDoubleProperty getPaneXProperty(){
		SimpleDoubleProperty x = new SimpleDoubleProperty();
		x.set(1420);
		return x;
	}



}
