package it.polimi.ingsw.network;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */

import java.net.*;
import java.io.*;

public class UserThread implements Runnable {
	
	private Socket socket;
	private ChatServer server;
	
	private ObjectOutputStream objectOutputStream;
	
	public UserThread(Socket socket, ChatServer server) {
		this.socket = socket;
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			
			printUsers();
			
			// initial message
			String userName = (String) objectInputStream.readObject();
			server.addUserName(userName);
			
			MessageTest serverMessage = new MessageTest(1,"New user connected: " + userName, 3.14);
			server.broadcast(serverMessage, this);
			
			String message;
			MessageTest clientMessage;
			do {
				// reads the new message from the client, then redirects it to the other clients connected
				clientMessage = (MessageTest) objectInputStream.readObject();
				message = "[" + userName + "] #" + clientMessage.getId() + ": " + clientMessage.getString() +
						" with weight = " + clientMessage.getWeight();
				serverMessage = new MessageTest(clientMessage.getId(), message, clientMessage.getWeight());
				server.broadcast(serverMessage, this);
				
			} while (!clientMessage.getString().equals("bye"));
			
			//closes socket connection when the user quits the chat
			server.removeUser(userName, this);
			socket.close();
			
			serverMessage = new MessageTest(1,userName + " has quit.", 3.14);
			server.broadcast(serverMessage, this);
			
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sends a list of online users to the newly connected user.
	 */
	void printUsers() {
		//this function sends strings as data via the output stream. The client must read the object sent as a String and not a MessageTest
		
		if (server.hasUsers()) {
			try {
				//MessageTest message = new MessageTest(7, "Connected users: " + server.getUserNames(), 4.5);
				String message = "Connected users: " + server.getUserNames();
				objectOutputStream.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				//MessageTest message = new MessageTest(7, "No other users connected", 4.5);
				String message = "No other users connected";
				objectOutputStream.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends a MessageTest object to the client via output stream
	 */
	void sendMessage(MessageTest message) {
		try {
			objectOutputStream.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}