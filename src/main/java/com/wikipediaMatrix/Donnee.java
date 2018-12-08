package com.wikipediaMatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Classe abstraite pour recuperer les donnees
 * @author thomas
 *
 */

public abstract class Donnee {

	private long tempsOriginal;
	private Map<Integer, Integer> nbLignesTableaux = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> nbColonnesTableaux = new HashMap<Integer, Integer>();;

	public Map<Integer, Integer> getNbLignesTableaux() {
		return nbLignesTableaux;
	}

	public Map<Integer, Integer> getNbColonnesTableaux() {
		return nbColonnesTableaux;
	}

	/**
	 * A partir d'une Url, determine de combien de lignes et de colonnes
	 * sera compose le csv en sortie, en prenant en compte les rowspans et colspans
	 * @param url l'url de la page wikipedia
	 * @throws MalformedURLException
	 * @throws UrlInvalideException
	 * @throws ExtractionInvalideException
	 */
	public void nbLignesColonnes(Url url) throws MalformedURLException, UrlInvalideException, ExtractionInvalideException {
		if (url.estUrlValide()) {
			String langue = url.getLangue();
			String titre = url.getTitre();
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			String donneeHTML = recupContenu(page);
			Document htmlParse = Jsoup.parseBodyFragment(donneeHTML);
			// On recupere les wikitables
			Elements wikitables = htmlParse.getElementsByClass("wikitable");
			// Ainsi que les tables classiques, sans attributs
			Elements tablesNonWiki = htmlParse.select("table:not([^])");
			for (Element element : tablesNonWiki) {
					wikitables.add(element);
			}
			int nbTableaux = getNbTableaux(donneeHTML);
			// On parcoure l'ensemble des tableaux de la page
			for (int i = 0 ; i < nbTableaux ; i++) {
				int[] nbLignesColonnes = getNbLignesColonnes(wikitables.get(i));
				nbLignesTableaux.put(i, nbLignesColonnes[0]);
				nbColonnesTableaux.put(i, nbLignesColonnes[1]);
			}
		}
	}

	/**
	 * Determine le nombre de lignes et de colonnes qu'une wikitable possedera une fois parsee en CSV
	 * @param wikitable
	 * @return
	 */
	int[] getNbLignesColonnes(Element wikitable) {
		int[] nbLignesColonnes = new int[2];
		// Calcul du nombre de lignes
		Elements lignes = wikitable.getElementsByTag("tr");
		int nbLignes = lignes.size();
		// Calcul du nombre de colonnes
		int nbColonnesMax = 0;
		int nbColonnes = 0;
		for (int i = 0; i < lignes.size(); i++) {
			// On va chercher la ligne avec le plus grand nombre de colonnes
			nbColonnes = lignes.get(i).select("td, th").size() + getNbColonnesAjouteesColspans(lignes.get(i));
			if(nbColonnes > nbColonnesMax) {
				nbColonnesMax = nbColonnes;
			}
		}

		Elements rowspans = wikitable.getElementsByAttribute("rowspan");

		// nombre de lignes total ajoutees par les rowspans	
		int totalRowspans = getNbLignesAjouteesRowspans(rowspans);

		nbLignesColonnes[0] = nbLignes + totalRowspans;
		nbLignesColonnes[1] = nbColonnesMax+1;

		return nbLignesColonnes;
	}

	/**
	 * Renvoie le nombre de lignes rajoutees par les rowspans pour un tableau
	 * @param rowspans
	 * @return
	 */
	private int getNbLignesAjouteesRowspans(Elements rowspans) {
		int totalRowspans = 0;
		for (Element rowspan : rowspans) {
			int valueRowspan = Integer.parseInt(rowspan.attr("rowspan"));
			totalRowspans += valueRowspan -1;
		}
		return totalRowspans;
	}

	/**
	 * Renvoie le nombre de colonnes rajoutees par les colspans dans un tableau
	 * @param colspans
	 * @return
	 */
	private int getNbColonnesAjouteesColspans(Element ligne) {
		int totalColspans = 0;
		for (Element colspan : ligne.getElementsByAttribute("colspan")) {
			String colspanValue = colspan.attr("colspan").replaceAll("[^0-9.]", "");
			int valueColspan = Integer.parseInt(colspanValue);
			totalColspans += valueColspan-1;
		}
		return totalColspans;
	}

	/**
	 * Extraction des donnees
	 * @param url
	 * @throws UrlInvalideException
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 * @throws ConversionInvalideException 
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws ArticleInexistantException 
	 */
	abstract void extraire(Url url) throws UrlInvalideException, ExtractionInvalideException, MalformedURLException, ConversionInvalideException, IOException, JSONException, ArticleInexistantException;

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
	 * On verifie que la page demandee contient bien un article
	 * @return
	 * @throws ArticleInexistantException
	 * @throws ExtractionInvalideException
	 */
	public boolean contientUnArticle(URL url) throws ArticleInexistantException, ExtractionInvalideException{
		String contenu = recupContenu(url);
		if (contenu.contains("<table id=\"noarticletext\"")) {
			return true;
		}
		else {
			throw new ArticleInexistantException("Il n'y a pas d'articles pour cette page.");
		}
	}

	/**
	 * Verification de la presence de tableaux
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	abstract boolean pageComporteTableau() throws ExtractionInvalideException;

	public abstract int getNbTableaux(String contenuPage);

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