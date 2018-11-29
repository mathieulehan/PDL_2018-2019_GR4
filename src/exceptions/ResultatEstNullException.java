package exceptions;

public class ResultatEstNullException extends Exception{

	String message;
	
	public ResultatEstNullException(String message) {
		super();
		this.message = message;
	}

}
