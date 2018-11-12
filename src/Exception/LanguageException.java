package Exception;

/**
 * 
 * Classe cr�ant une Exception dans le cas o� la page n'est pas dans les langues suivantes :*
 * - Anglais 
 * - Fran�ais
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
