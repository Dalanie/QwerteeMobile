package melb.msafe.jdispatcher.jdispatch.exceptions;

@SuppressWarnings("serial")
public class UnexpectedException extends RuntimeException {

	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnexpectedException(Throwable cause) {
		super(cause);
	}
}
