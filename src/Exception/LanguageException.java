package Exception;

/**
 * 
 * Classe créant une Exception dans le cas où la page n'est pas dans les langues suivantes :*
 * - Anglais 
 * - Français
 * 
 * @author Charlotte
 *
 */
public class LanguageException extends Exception {

	String object = "Invalid language";
	
	public LanguageException() {
		super();
	}
	
	@Override
	public String toString() {
		return "Exception " + object;
	}
}
