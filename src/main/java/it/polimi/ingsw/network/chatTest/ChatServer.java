package it.polimi.ingsw.network.chatTest;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the chat server program
 */
public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();
	
	public ChatServer(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hosting server: write port number: ");
		int port = Integer.parseInt(scanner.nextLine());
		
		// creates a server hosted on localhost with the defined port
		ChatServer server = new ChatServer(port);
		server.execute();
	}
	
	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			
			System.out.println("Chat Lobby is listening on port " + port);
			
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected from " + socket.getRemoteSocketAddress().toString());
				
				// each logged client is a thread that runs and interacts with the server
				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				new Thread(newUser).start();
				
			}
			
		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Delivers a message from one user to the others (broadcasting)
	 */
	void broadcast(MessageTest message, UserThread excludeUser) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}
	
	/**
	 * Stores username of the newly connected client.
	 */
	void addUserName(String userName) {
		userNames.add(userName);
	}
	
	/**
	 * When a client is disconnected, removes the associated username and UserThread
	 */
	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + userName + " quit");
		}
	}
	
	Set<String> getUserNames() {
		return this.userNames;
	}
	
	/**
	 * Returns true if there are other users connected (not count the currently connected user)
	 */
	boolean hasUsers() {
		return !this.userNames.isEmpty();
	}
}