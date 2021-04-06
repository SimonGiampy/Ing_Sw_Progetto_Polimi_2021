package it.polimi.ingsw.InternetTesting;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 *
 * @author www.codejava.net
 */

public class ChatClient {
	private String hostname;
	private int port;
	private String userName;
	
	public ChatClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	public void execute() {
		try {
			Socket socket = new Socket(hostname, port);
			
			System.out.println("Connected to the chat server");
			
			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();
			
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
	
	
	public static void main(String[] args) {
		//if (args.length < 2) return;
		
		System.out.println("write hostname: ");
		Scanner scanner = new Scanner(System.in);
		String hostname = scanner.nextLine();
		System.out.println("write port number: ");
		int port = Integer.parseInt(scanner.nextLine());
		
		ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}
}