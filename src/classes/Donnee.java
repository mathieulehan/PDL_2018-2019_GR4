package classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import exceptions.ConversionInvalideException;
import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

/**
 * Classe abstraite pour recuperer les donnees
 * @author thomas
 *
 */

public abstract class Donnee{

	private long tempsOriginal;

	/**
	 * Extraction des donnees
	 * @param url
	 * @throws UrlInvalideException
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 * @throws ConversionInvalideException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	abstract void extraire(Url url) throws UrlInvalideException, ExtractionInvalideException, MalformedURLException, ConversionInvalideException, IOException, JSONException;

	/**
	 * A partir de l'url donnee, recupere le contenu de la page en json
	 * @param url
	 * @return String
	 * @throws ExtractionInvalideException 
	 */
	public String recupContenu(URL url) throws ExtractionInvalideException{
		try {
			StringBuilder result = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null)
				result.append(inputLine);

			in.close();
			return result.toString();
		} catch (Exception e) {
			throw new ExtractionInvalideException("Recuperation du contenu impossible");
		}
	}

	/**
	 * Verification de la presence de tableaux
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	abstract boolean pageComporteTableau(String donnee) throws ExtractionInvalideException;

	/**
	 * Demarre le chronometre en back
	 */
	public void start(){
		this.tempsOriginal = System.currentTimeMillis();
	}

	/**
	 * Donne le temps du chronometre a l'instant T
	 * @return long
	 */
	public long getTime(){
		return System.currentTimeMillis() - tempsOriginal;
	}
}