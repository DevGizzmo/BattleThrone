package net.gizzmo.battlethrone.api.exception.location;

public class InvalidLocationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	InvalidLocationException(String str) {
        super(str);
    }

    public InvalidLocationException() {
        super("Location spécifié invalide.");
    }
}
