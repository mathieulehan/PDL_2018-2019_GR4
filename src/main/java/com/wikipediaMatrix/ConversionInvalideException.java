package main.java.com.wikipediaMatrix;

/**
 * 
 * Classe creant une Exception dans differents cas d'erreur lors de la convertion des donnees vers le fichier CSV:
 * @author Charlotte
 *
 */
public class ConversionInvalideException extends Exception{

	String message;
	
	public ConversionInvalideException(String message) {
		super();
		this.message = message;
	}
}

