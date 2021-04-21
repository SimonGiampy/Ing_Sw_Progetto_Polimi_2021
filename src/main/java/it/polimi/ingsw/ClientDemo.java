package it.polimi.ingsw;

import it.polimi.ingsw.network.chatTest.ReadThread;
import it.polimi.ingsw.network.chatTest.WriteThread;
import it.polimi.ingsw.network.messages.LoginRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.SocketClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientDemo {
	
	private final String hostname;
	private final int port;
	private String userName;
	
	public static void main(String[] args) {
		System.out.println("write hostname: ");
		Scanner scanner = new Scanner(System.in);
		String hostname = scanner.nextLine();
		System.out.println("write port number: ");
		int port = Integer.parseInt(scanner.nextLine());
		
		ClientDemo client = new ClientDemo(hostname, port);
		SocketClient socketClient = new SocketClient(hostname, port);
		
		
		// chooses the username and communicates it to the host server
		System.out.println("Enter your username first: ");
		String input = scanner.nextLine();
		client.setUserName(input);
		Message message = new LoginRequest(client.userName);
		socketClient.sendMessage(message);
		
		
	}
	
	public ClientDemo(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	void setUserName(String userName) {
		this.userName = userName;
	}
}
