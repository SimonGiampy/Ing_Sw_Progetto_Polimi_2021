package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observers.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Client extends Observable {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	private final ExecutorService readExecutionQueue;
	
	public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

	public Client(String address, int port) {

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
				Message message = null;
				try {
					message = (Message) inputStream.readObject();
					LOGGER.info("Received: " + message);
				} catch (IOException | ClassNotFoundException e) {
					LOGGER.severe("Error: disconnection caused by the server termination");
					disconnect();
					readExecutionQueue.shutdownNow();
				}
				notifyObserver(message);
			}
		});
	}

	public void sendMessage(Message message) {
		try {
			LOGGER.info("Sending: " + message);
			outputStream.writeObject(message);
		} catch (IOException e) {
			disconnect();
			LOGGER.severe("Error in sending message");
		}
	}
	
	/**
	 * Method called when an error occurs and the client disconnects from the server. The socket gets closed
	 */
	public void disconnect() {
		try {
			if (!socket.isClosed()) {
				readExecutionQueue.shutdownNow();
				socket.close();
			}
			LOGGER.info("Client disconnected from the game");
		} catch (IOException e) {
			LOGGER.severe("Client-side disconnection cannot be completed");
			e.printStackTrace();
		}
	}

}
