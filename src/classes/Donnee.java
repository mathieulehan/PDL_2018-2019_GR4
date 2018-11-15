package classes;

import java.io.IOException;
import java.net.URL;

/**
 * Classe abstraite pour recuperer les donnees
 * @author thomas
 *
 */

abstract class Donnee{
		
	private long tempsOriginal;
	
	/**
	 * Extraction des donnees
	 * @param langue
	 * @param titre
	 * @throws IOException
	 */
	abstract void extraire(String langue, String titre)  throws IOException;
	
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
	 */
	abstract boolean pageComporteTableau(String donnee);

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