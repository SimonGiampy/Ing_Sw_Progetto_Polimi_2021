package it.polimi.ingsw.exceptions;

public class InvalidUserRequestException extends InvalidInputException {
	public InvalidUserRequestException(String errorMessage) {
		super(errorMessage);
	}
}
