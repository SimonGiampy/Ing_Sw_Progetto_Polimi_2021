package it.polimi.ingsw.network.chatTest;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 */
public class WriteThread implements Runnable {

	private Socket socket;
	private ChatClient client;
	
	private String clientUserName;
	private ObjectOutputStream objectOutputStream;
	
	public WriteThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
		
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		Scanner scanner = new Scanner(System.in);
		String userInput;
		MessageTest message; //MessageTest format: 12;something here;3.14
		
		do {
			System.out.print("[" + clientUserName + "]: ");
			userInput = scanner.nextLine();
			
			if (!userInput.equals("bye")) {
				message = new MessageTest(userInput);
				
				try {
					objectOutputStream.writeObject(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} while (!userInput.equals("bye"));
		
		// stops the socket connection with the server as the client disconnects
		try {
			socket.close();
		} catch (IOException ex) {
			System.out.println("Error writing to server: " + ex.getMessage());
		}
	}
	
	// sends the client username to the server
	protected void logInChat(String username) {
		try {
			objectOutputStream.writeObject(username);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientUserName = username;
	}
	
}