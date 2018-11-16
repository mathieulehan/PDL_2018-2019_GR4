package exceptions;

/**
 * 
 * Classe creant une Exception dans differents cas d'erreur lors de l'extraction des donnees de la page Wikipedia :
<<<<<<< HEAD
 * - Pas de tableau dans la page
=======
 * - Pas de tableaux dans la page
>>>>>>> V1
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

