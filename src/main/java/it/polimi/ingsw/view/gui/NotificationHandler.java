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

	public void addNotification(String genericMessage, AnchorPane root, boolean error){

		Notification notification = new Notification(root, genericMessage, this, error);
		notification.showNotification();

		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		if(error){
			executorService.schedule(() -> {
				if(notification.isShowing)
					notification.dismissInit();
			}, 5, TimeUnit.SECONDS);
		}
		else {
			executorService.schedule(() -> {
				if (notification.isShowing)
					notification.dismissInit();
			}, 7 + delay, TimeUnit.SECONDS);

			delay++;
			if (delay == 3) delay = 0;
		}
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
