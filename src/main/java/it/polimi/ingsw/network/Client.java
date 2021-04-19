package it.polimi.ingsw.network;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Observable {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	public Client(String address, int port){

		try {
			this.socket = new Socket(address, port);
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMessage(){
		Message message;
		try {
			message = (Message) inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			//message = new ErrorMessage();
		}
		//notifyObserver(message);
	}

	public void sendMessage(Message message){
		try {
			outputStream.writeObject(message);
			outputStream.reset();
		} catch (IOException e) {
			e.printStackTrace();
			//notifyObserver(new ErrorMessage);
		}
	}

}
