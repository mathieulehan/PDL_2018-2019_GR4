package exceptions;

/**
 * 
 * Classe créant une Exception dans différents cas d'erreur lors de l'extraction des données de la page Wikipédia :
 * - Pas de tableaux dans la page
 * 
 * @author Charlotte
 *
 */
public class ExtractionInvalideException extends Exception{

	String message;
	
	public ExtractionInvalideException(String message) {
		super();
		this.message = message;
	}
}

