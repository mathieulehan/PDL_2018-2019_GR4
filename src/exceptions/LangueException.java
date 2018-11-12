package exceptions;

/**
 * 
 * Classe créant une Exception dans le cas où la page n'est pas dans les langues suivantes :
 * - Anglais 
 * - Français
 * 
 * @author Charlotte
 *
 */
public class LangueException extends Exception {

	String message;
	
	public LangueException(String message) {
		super();
		this.message = message;
	}
}
