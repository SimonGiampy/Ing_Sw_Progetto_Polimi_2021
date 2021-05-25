package it.polimi.ingsw.view.gui;

import javafx.scene.layout.AnchorPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationHandler {

	private final boolean [] notificationStack = {true, true, true, true, true, true};
	private final Notification[] notifications = new Notification[6];
	private int delay;

	public NotificationHandler(){
		delay = 0;
	}

	public void addNotification(String genericMessage, AnchorPane root){

		Notification notification = new Notification(root, genericMessage, this);
		notification.showNotification();

		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		executorService.schedule(notification::dismissInit, 8 + delay, TimeUnit.SECONDS);

		delay++;
		if(delay == 3) delay = 0;
	}

	public void onDismiss(int index){
		for(int i = index; i < notificationStack.length; i++){
			if(!notificationStack[i]) {
				notifications[i].slideNotification();
				notifications[i-1] = notifications[i];
			}
		}
	}

	public boolean[] getNotificationStack() {
		return notificationStack;
	}

	public Notification[] getNotifications() {
		return notifications;
	}
}
