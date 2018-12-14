package main.java.com.wikipediaMatrix;

/**
 * 
 * Classe creant une Exception dans differents cas d'erreur d'URL :
 * - URL d'une page inexistante
 * - Connexion via l'URL echouee
 * - Langue non geree
 * 
 * @author Charlotte
 *
 */
public class UrlInvalideException extends Exception {

	String message;
	
	public UrlInvalideException(String message) {
		super();
		this.message = message;
	}
}

