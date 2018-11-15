package classes;

import java.io.IOException;
import java.net.URL;

import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

/**
 * Classe abstraite pour recuperer les donnees
 * @author thomas
 *
 */

abstract class Donnee{
		
	private long tempsOriginal;
	
	/**
	 * Extraction des donnees
	 * @param url
	 * @throws IOException
	 */
	abstract void extraire(Url url) throws IOException, UrlInvalideException;
	
	/**
	 * Recuperation des donnees a partir de l'url
	 * @param url
	 * @return
	 * @throws IOException
	 */
	abstract String recupContenu(URL url) throws IOException;
	
	/**
	 * Verification de la presence de tableaux
	 * @param donnee
	 * @return
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
	 * @return
	 */
	public long getTime(){
		return System.currentTimeMillis() - tempsOriginal;
	}
}