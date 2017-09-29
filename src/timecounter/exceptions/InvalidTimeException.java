package timecounter.exceptions;


public class InvalidTimeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidTimeException(String message) {
		super(message);
	}
}
