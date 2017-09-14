package se.per.rps.challenge;

public class GameServletException extends Exception {
	private static final long serialVersionUID = 1L;

	public final int error;

	public GameServletException(int error) {
		this.error = error;
	}
}
