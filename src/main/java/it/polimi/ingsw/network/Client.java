package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observers.Observable;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client extends Observable {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	private final ExecutorService readExecutionQueue;
	
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	
	/**
	 * constructor created by the client main app
	 * @param address address of the server to connect
	 * @param port port of the server to connect
	 */
	public Client(String address, int port) {
		try {
			this.socket = new Socket(address, port);
			LOGGER.info("Connected to server");
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			LOGGER.error("Error in connecting to the server: " + e.getMessage());
		}
		readExecutionQueue = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * runnable that reads incoming messages from the server. Notifies the observer for handling the message
	 */
	public void readMessage() {
		readExecutionQueue.execute(() -> {
			while (!readExecutionQueue.isShutdown()) {
				Message message = null;
				try {
					message = (Message) inputStream.readObject();
					LOGGER.info("Received: " + message);
				} catch (ClassNotFoundException e) {
					LOGGER.error("Error: wrong class read from stream");
				} catch (IOException ex) {
					LOGGER.error("Error: disconnection caused by the server termination: " + ex.getMessage());
					disconnect();
				}
				notifyObserver(message);
			}
		});
	}
	
	/**
	 * sends a message from the client to the server
	 * @param message to be sent
	 */
	public void sendMessage(Message message) {
		try {
			LOGGER.info("Sending: " + message);
			outputStream.writeObject(message);
		} catch (IOException e) {
			LOGGER.error("Error in sending message: " + e.getMessage());
			disconnect();
		}
	}
	
	/**
	 * Method called when an error occurs and the client disconnects from the server. The socket gets closed.
	 */
	public void disconnect() {
		try {
			if (!socket.isClosed()) {
				readExecutionQueue.shutdownNow();
				socket.close();
			}
			LOGGER.info("Client disconnected from the game");
		} catch (IOException e) {
			LOGGER.error("Client-side disconnection cannot be completed: " + e.getMessage());
		}
	}

}
