package it.polimi.ingsw.network;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible for reading server's input and printing it to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 */
public class ReadThread implements Runnable {
	private BufferedReader reader;
	private Socket socket;
	private ChatClient client;
	
	ObjectInputStream objectInputStream;
	
	public ReadThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
		
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				//String response = objectInputStream.readObject().toString();
				Object object = objectInputStream.readObject();
				
				// this instanceof behavior is temporary, an alternative must be found
				// every incoming data must be read as a specific object, rather than seeing which object it is
				if (object instanceof String) {
					System.out.println("\nstring = " + object.toString());
				} else if (object instanceof Message) {
					Message response = (Message) object;
					System.out.println("\nmessage = " + response.toString());
				}
				
				// prints the username after displaying the server's message
				if (client.getUserName() != null) {
					System.out.print("[" + client.getUserName() + "]: ");
				}
			} catch (IOException ex) {
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				System.out.println("Error: class is not found: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}