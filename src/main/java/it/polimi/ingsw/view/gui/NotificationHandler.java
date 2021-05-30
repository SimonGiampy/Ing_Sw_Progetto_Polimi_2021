package it.polimi.ingsw.view.gui;

import javafx.scene.layout.AnchorPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationHandler {

	private final boolean[] notificationStack = {true, true, true, true, true, true, true, true};
	private final Notification[] notifications = new Notification[8];
	private long lastTime;
	private int lastDelay;

	/**
	 * constructor of the notification handler
	 */
	public NotificationHandler(){ lastTime = System.currentTimeMillis();}


	public void addNotification(String genericMessage, AnchorPane root, boolean error){

		Notification notification = new Notification(root, genericMessage, this, error);
		notification.showNotification();

		int delay = 6;
		long now = System.currentTimeMillis();
		if(now - lastTime < 1000) delay = lastDelay + 1;

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
			}, delay, TimeUnit.SECONDS);
		}
		lastDelay = delay;
		lastTime = System.currentTimeMillis();
	}

	public void onDismiss(int index){
		synchronized (notificationStack) {
			for (int i = index; i < notificationStack.length; i++) {
				if (!notificationStack[i]) {
					notifications[i].slideNotification();
					notifications[i - 1] = notifications[i];
				}
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
