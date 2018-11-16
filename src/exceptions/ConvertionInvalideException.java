package exceptions;

/**
 * 
 * Classe creant une Exception dans differents cas d'erreur lors de la convertion des donnees vers le fichier CSV:
 * @author Charlotte
 *
 */
public class ConvertionInvalideException extends Exception{

	String message;
	
	public ConvertionInvalideException(String message) {
		super();
		this.message = message;
	}
}

