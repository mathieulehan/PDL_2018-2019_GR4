package classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
	 * @throws IOException
	 * @throws ExtractionInvalideException 
	 */
	abstract void extraire(Url url) throws IOException, UrlInvalideException, ExtractionInvalideException;
	
	/**
	 * A partir de l'url donnee, recupere le contenu de la page en json
	 * @param url
	 * @return String
	 * @throws IOException
	 */
	public String recupContenu(URL url) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			result.append(inputLine);

		in.close();
		return result.toString();
	}
	
	/**
	 * Verification de la presence de tableaux
	 * @param donnee
	 * @return
	 * @throws ExtractionInvalideException 
	 */
	abstract boolean pageComporteTableau() throws ExtractionInvalideException;

	/**
	 * Demarre le chronometre en back
	 */
	public void start(){
		this.tempsOriginal = System.currentTimeMillis();
	}

	/**
	 * Donne le temps du chronometre a l'instant T
	 * @return
	 */
	public long getTime(){
		return System.currentTimeMillis() - tempsOriginal;
	}
}