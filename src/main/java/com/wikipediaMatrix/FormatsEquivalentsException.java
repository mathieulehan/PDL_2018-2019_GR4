package main.java.com.wikipediaMatrix;

/**
 * 
 * Classe creant une Exception lorsque la comparaison des 2 extraction (format HTML et Wikitext) :
 * - Nombre de lignes equivalentes
 * - Nombre de colonnes equivalentes
 * @author Charlotte
 *
 */
public class FormatsEquivalentsException extends Exception{

	String message;
	
	public FormatsEquivalentsException(String message) {
		super();
		this.message = message;
	}
}

