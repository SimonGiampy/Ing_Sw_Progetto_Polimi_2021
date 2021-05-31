package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.generic.PingMessage;
import it.polimi.ingsw.observers.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client extends Observable {

	private final Socket socket;
	private final ObjectOutputStream outputStream;
	private final ObjectInputStream inputStream;
	
	private final ExecutorService readExecutionQueue;
	private final ScheduledExecutorService pinger;
	
	public static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	
	/**
	 * constructor created by the client main app
	 * @param address address of the server to connect
	 * @param port port of the server to connect
	 */
	public Client(String address, int port) throws IOException {

		socket = new Socket(address, port);
		socket.setSoTimeout(20000);
		socket.setSoTimeout(60000);
		LOGGER.info("Connected to server");
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		this.inputStream = new ObjectInputStream(socket.getInputStream());

		readExecutionQueue = Executors.newSingleThreadExecutor();
		pinger = Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * runnable that reads incoming messages from the server. Notifies the observer for handling the message
	 */
	public void readMessage() {
		if(socket != null) {
			readExecutionQueue.execute(() -> {
				while (!readExecutionQueue.isShutdown()) {
					Message message;
					try {
						message = (Message) inputStream.readObject();
						if (message.getMessageType() != MessageType.PING) {
							LOGGER.info("Received: " + message);
							notifyObserver(message);
						}
						
					} catch (ClassNotFoundException e) {
						LOGGER.error("Error: wrong class read from stream");
					} catch (IOException ex) {
						LOGGER.error("Error: disconnection caused by the server termination: " + ex.getMessage());
						disconnect();
					}
				}
			});
		}
	}
	
	/**
	 * sends a message from the client to the server
	 * @param message to be sent
	 */
	public void sendMessage(Message message) {
		try {
			if (message.getMessageType() != MessageType.PING) {
				LOGGER.info("Sending: " + message);
			}
			outputStream.writeObject(message);
			outputStream.reset();
		} catch (IOException e) {
			LOGGER.error("Error in sending message: " + e.getMessage());
			e.printStackTrace();
			disconnect();
		}
	}
	
	/**
	 * Enable a heartbeat (ping messages) between client and server sockets to keep the connection alive
	 * @param enabled set this argument to {@code true} to enable the heartbeat.
	 *                set to {@code false} to kill the heartbeat.
	 */
	public void enablePinger(boolean enabled) {
		if (enabled) {
			pinger.scheduleAtFixedRate(() -> sendMessage(new PingMessage()), 0, 2000, TimeUnit.MILLISECONDS);
		} else {
			pinger.shutdownNow();
		}
	}
	
	/**
	 * Method called when an error occurs and the client disconnects from the server. The socket gets closed.
	 */
	public void disconnect() {
		try {
			if (!socket.isClosed()) {
				readExecutionQueue.shutdownNow();
				enablePinger(false);
				socket.close();
				LOGGER.info("Client disconnected from the game");
				final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
				executorService.schedule(() -> System.exit(1), 7, TimeUnit.SECONDS); // abrupt termination of program
			}
		} catch (IOException e) {
			LOGGER.error("Client-side disconnection cannot be completed: " + e.getMessage());
		}
	}

}
