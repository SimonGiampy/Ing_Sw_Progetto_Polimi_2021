package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

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
		initNotification(root, text, posX, posY - 100*index);
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
		anchorPane.setStyle("-fx-background-color: #31b0d5");
		anchorPane.setVisible(true);
		lblText.setVisible(true);
		btnClose.setVisible(true);
		img.setVisible(true);
	}
}
