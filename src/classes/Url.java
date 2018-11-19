package classes;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import exceptions.UrlInvalideException;
/**
 * Classe permettant de recuperer les informations d'une page via son url
 * Validation de l'url requise
 * @author Charlotte
 *
 */
public class Url {

	private URL url;
	private String titre;
	private String langue;

	public Url(URL url) {
		this.url = url;
		this.titre = "";
		this.langue = "";
	}

	/**
	 * Verification de la langue de la page
	 * @return true si la langue est en francais ou en anglais
	 * @throws UrlInvalideException 
	 */
	private boolean estLangueValide() throws UrlInvalideException {
		langue = url.toString().substring(8, url.toString().indexOf('.'));
		if (!langue.matches("fr|en")) {
			throw new UrlInvalideException("Langue invalide");
		}  
		return true;
	}

	/**
	 * Verification du titre de la page
	 * @return true si le titre comporte au moins un caractere
	 * @throws MalformedURLException 
	 */
	private boolean estTitreValide() throws MalformedURLException {
		titre = url.toString().substring(url.toString().lastIndexOf('/')+1);
		if (!titre.matches("^[\\w]+$")) {
			throw new MalformedURLException("Titre de la page invalide");
		}
		return true;
	}

	/**
	 * Tester une connexion avec le serveur HTTP afin de savoir si l'url renvoie bien a une page existante
	 * ATTENTION methode lourde (en temps et en memoire)
	 * @return true si la connexion HTTP est reussie 
	 * @throws UrlInvalideException 
	 */
	public boolean testerConnexionHTTP() throws UrlInvalideException {
		try {
			HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
			if (!(connexion.getResponseCode() == HttpURLConnection.HTTP_OK)) {
				throw new UrlInvalideException("Page inexistante");
			}
			connexion.disconnect();
			return true;
		} catch (Exception e) {
			throw new UrlInvalideException("Connexion echouee");
		}
	}

	/**
	 * Methode implementant les precedentes methodes
	 * @return true si titre valide, langue valide et connexion reussie
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	public boolean estUrlValide() throws UrlInvalideException, MalformedURLException {
		return estTitreValide() && estLangueValide() /*&& testerConnexionHTTP()*/;
	}

	/**
	 * Recuperer l'URL 
	 * @return String url
	 */
	public URL getURL() {
		return this.url;
	}
	
	/**
	 * Recuperer le titre de la page url
	 * @return String titre
	 */
	public String getTitre(){
		return this.titre.toString();
	}

	/**
	 * Recuperer la langue de la page url
	 * @return String langue
	 */
	public String getLangue() {
		return this.langue;
	}
}
