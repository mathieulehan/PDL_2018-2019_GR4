/**
 * 
 */
package Exception;

/**
 * 
 * Classe cr�ant une Exception dans diff�rents cas d'erreur d'URL :
 * - URL d'une page inexistante
 * - URL ne provenant pas de Wikip�dia
 * - URL inexistante
 * 
 * @author Charlotte
 *
 */
public class UrlInvalidException extends Exception{

	String object = "Invalid URL";
	
	public UrlInvalidException() {
		super();
	}
	
	@Override
	public String toString() {
		return "Exception " + object;
	}
}
