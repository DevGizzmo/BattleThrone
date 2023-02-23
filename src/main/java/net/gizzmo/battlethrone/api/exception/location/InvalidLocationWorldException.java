package net.gizzmo.battlethrone.api.exception.location;

public class InvalidLocationWorldException extends InvalidLocationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidLocationWorldException() {
        super("Monde spécifié invalide.");
    }
}
