package net.gizzmo.battlethrone.api.exception;

public class UnsupportedMinecraftVersionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedMinecraftVersionException() {
        super("Cette version de Minecraft n'est pas prise en charge par le plugin. Essayez de rétrograder Minecraft ou de mettre à niveau le plugin.");
    }
}
