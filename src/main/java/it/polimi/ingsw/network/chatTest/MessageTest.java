package it.polimi.ingsw.network.chatTest;

import java.io.Serializable;

/**
 * MessageTest format: 12;something here;3.14
 * Integer;String;Double
 */
public class MessageTest implements Serializable {
	
	private int id;
	private String string;
	private double weight;
	
	public MessageTest(int id, String string, double weight) {
		this.id = id;
		this.string = string;
		this.weight = weight;
	}
	
	// constructor overloading for ease of instantiation
	public MessageTest(String inputString) {
		String[] inputs = inputString.split(";");
		this.id = Integer.parseInt(inputs[0]);
		this.string = inputs[1];
		this.weight = Double.parseDouble(inputs[2]);
	}
	
	public int getId() {
		return id;
	}
	
	public String getString() {
		return string;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return this.id + ":" + this.getString() + ":" + this.getWeight();
	}
}
