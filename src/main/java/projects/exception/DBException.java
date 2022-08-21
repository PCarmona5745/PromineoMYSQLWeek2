package projects.exception;

public class DBException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6348757421212262850L;

	public DBException(String message) {
		super(message);
	}

	public DBException(Throwable cause) {
		super(cause);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

}
