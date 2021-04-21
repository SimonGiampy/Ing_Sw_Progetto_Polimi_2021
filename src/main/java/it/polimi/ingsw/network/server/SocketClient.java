package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketClient extends Observable {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	private final ExecutorService readExecutionQueue;
	
	public static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());

	public SocketClient(String address, int port) {

		try {
			this.socket = new Socket(address, port);
			LOGGER.info("Connected to server");
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		readExecutionQueue = Executors.newSingleThreadExecutor();
	}

	public void readMessage() {
		readExecutionQueue.execute(() -> {
			while (!readExecutionQueue.isShutdown()) {
				Message message;
				try {
					message = (Message) inputStream.readObject();
					SocketClient.LOGGER.info("Received: " + message);
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					//message = new ErrorMessage();
					disconnect();
					readExecutionQueue.shutdownNow();
				}
				//notifyObserver(message);
			}
		});
		
		
	}

	public void sendMessage(Message message) {
		try {
			outputStream.writeObject(message);
			//outputStream.reset(); // is this necessary?
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			notifyObserver(new ErrorMessage("", "Error in sending message"));
		}
	}
	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			notifyObserver(new ErrorMessage("", "disconnection"));
		}
	}

}
