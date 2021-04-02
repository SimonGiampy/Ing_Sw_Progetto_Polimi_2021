package it.polimi.ingsw.exceptions;

public class ProductionOutOfRangeException extends InvalidInputException {
	public ProductionOutOfRangeException(String errorMessage) {
		super(errorMessage);
	}
}
