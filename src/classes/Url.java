package classes;

import exceptions.LangueException;
import exceptions.UrlInvalideException;

public class Url {

	public String url;
	public String titre;
	public String langue;

	public Url(String url) {
		this.url = url;
	}

	public void validationLangue() throws LangueException, UrlInvalideException {
		langue = url.substring(8, url.indexOf('.'));
		titre = url.substring(url.lastIndexOf('/')+1);
		
		if (!langue.matches("fr|en")) {
			throw new LangueException("Langue invalide");
		} else if (!titre.matches("^[\\w]+$")) {
			throw new UrlInvalideException("Titre de la page invalide");
		}
	}

	public static void main (String[] args) throws LangueException, UrlInvalideException {
		Url url = new Url("https://fr.wikipedia.org/wiki/RTL");
		url.validationLangue();
	}
}
