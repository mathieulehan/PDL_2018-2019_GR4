package classes;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ConversionInvalideException;
import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

/**
 * Classe permettant de recuperer et convertir des tables html en CSV
 * @author mathi & thomas
 *
 */

public class Donnee_Html extends Donnee{
	/**
	 * Le HTML de la page wikipedia
	 */
	private String donneeHTML;
	private int lignesEcrites = 0;
	private int colonnesEcrites = 0;

	public Donnee_Html() {
		this.donneeHTML = "";
	}

	/**
	 * Recupere le contenu et l'insere dans un CSV
	 * @param url
	 * @throws UrlInvalideException 
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 * @throws ConversionInvalideException 
	 */
	@Override
	public void extraire(Url url) throws UrlInvalideException, ExtractionInvalideException, MalformedURLException, ConversionInvalideException {
		if(url.estUrlValide()) {
			start();
			String langue = url.getLangue();
			String titre = url.getTitre();
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			String titreSain = titre.replaceAll("/|\\|?|:|*|<|>|\\||\"", "");
			donneeHTML = "" + recupContenu(page);
			htmlVersCSV(titreSain);
		}
	}
	
	/**
	 * Meme fonction que extraire, mais avec une liste en entree, prevision du "concours"
	 * @param listUrl
	 * @throws UrlInvalideException
	 * @throws ExtractionInvalideException
	 * @throws MalformedURLException
	 * @throws ConversionInvalideException
	 */
	public void extraireList(Url[] listUrl) throws UrlInvalideException, ExtractionInvalideException, MalformedURLException, ConversionInvalideException {
		for (int i = 0; i < listUrl.length; i++) {
			start();
			String langue = listUrl[i].getLangue();
			String titre = listUrl[i].getTitre();
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			String titreSain = titre.replaceAll("/|\\|?|:|*|<|>|\\||\"", "");
			donneeHTML = "" + recupContenu(page);
			htmlVersCSV(titreSain);
		}
	}

	/**
	 * Methode qui parcoure les tables du HTML et les convertit en CSV
	 * @throws ConversionInvalideException
	 */
	public void htmlVersCSV(String titre) throws ConversionInvalideException {
		String outputPath = "src/ressources/" + titre + ".csv";
		try {
			FileOutputStream outputStream = new FileOutputStream(outputPath);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
			Document page = Jsoup.parseBodyFragment(donneeHTML);
			Elements lignes = page.getElementsByTag("tr");

			for (Element ligne : lignes) {
				Elements cellules = ligne.getElementsByTag("td");
				for (Element cellule : cellules) {
					writer.write(cellule.text().concat("; "));
					colonnesEcrites++;
				}
				writer.write("\n");
				lignesEcrites++;
			}
			writer.close();
		} catch (Exception e) {
			throw new ConversionInvalideException("Convertion HTML vers CSV incorrecte");
		}
	}

	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	public boolean pageComporteTableau(String html) throws ExtractionInvalideException {
		Document page = Jsoup.parseBodyFragment(donneeHTML);
		if(page.getElementsByTag("table") == null){
			throw new ExtractionInvalideException("Aucun tableau present dans la page");
		}
		return true;
	}

	public int getColonnesEcrites() {
		return colonnesEcrites;
	}

	public int getLignesEcrites() {
		return lignesEcrites;
	}
}
