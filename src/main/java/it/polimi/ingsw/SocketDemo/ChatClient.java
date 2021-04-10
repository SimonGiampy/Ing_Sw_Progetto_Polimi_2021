package it.polimi.ingsw.SocketDemo;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the chat server program, client side
 */

public class ChatClient {
	
	private String hostname;
	private int port;
	private String userName;
	
	public ChatClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	public static void main(String[] args) {
		//initial connection setup
		System.out.println("write hostname: ");
		Scanner scanner = new Scanner(System.in);
		String hostname = scanner.nextLine();
		System.out.println("write port number: ");
		int port = Integer.parseInt(scanner.nextLine());
		
		ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}
	
	public void execute() {
		try {
			Socket socket = new Socket(hostname, port);
			System.out.println("Connected to the chat server");
			
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter your username first: ");
			String input = scanner.nextLine();
			
			// chooses the username and communicates it to the host server
			setUserName(input);
			WriteThread writeThread = new WriteThread(socket, this);
			writeThread.logInChat(this.userName);
			
			// creates new threads and starts them.
			new Thread(new ReadThread(socket, this)).start();
			new Thread(writeThread).start();
			
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}
		
	}
	
	void setUserName(String userName) {
		this.userName = userName;
	}
	
	String getUserName() {
		return this.userName;
	}
	
}