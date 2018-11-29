package classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ArticleInexistantException;
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
	 * @throws ConversionInvalideException 
	 * @throws ArticleInexistantException 
	 * @throws IOException 
	 */
	@Override
	public void extraire(Url url) throws UrlInvalideException, ExtractionInvalideException, ConversionInvalideException, ArticleInexistantException, IOException {
		if(url.estUrlValide()) {
			start();
			String langue = url.getLangue();
			String titre = url.getTitre();
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			this.donneeHTML = recupContenu(page);
			if(pageComporteTableau()){
				String titreSain = titre.replaceAll("[\\/\\?\\:\\<\\>]", "");
				htmlVersCSV(titreSain);
			}
		}
	}

	/**
	 * Methode qui parcoure les tables du HTML et les convertit en CSV
	 * @throws ConversionInvalideException
	 * @throws IOException 
	 */
	public void htmlVersCSV(String titre) throws ConversionInvalideException, IOException {
		String outputPath = "src/ressources/" + titre + ".csv";
		FileOutputStream outputStream = new FileOutputStream(outputPath);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		Document page = Jsoup.parseBodyFragment(this.donneeHTML);
		Elements wikitables = page.select(".wikitable");

		for (Element table : wikitables) {
			// entetes -> ecrireEnTete(page, writer);
			ecrireLignes(table, writer);
			writer.write("\n\n");
		}
		writer.close();
	}

	public void ecrireLignes(Element table, OutputStreamWriter writer) throws IOException {
		Elements lignes = table.getElementsByTag("tr");
		for (Element ligne : lignes) {
			Elements cellules = ligne.getElementsByTag("td");
			for (Element cellule : cellules) {
				writer.write(cellule.text().concat("; "));
				colonnesEcrites++;
			}
			writer.write("\n");
			lignesEcrites++;
		}
	}

	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	public boolean pageComporteTableau() throws ExtractionInvalideException {
		Document page = Jsoup.parseBodyFragment(this.donneeHTML);
		if(page.getElementsByTag("<table class=\"wikitable\">") == null){
			throw new ExtractionInvalideException("Aucun tableau present dans la page");
		}
		return true;
	}

	@Override
	public int getNbTableaux() {
		return StringUtils.countMatches("<table class=\"wikitable\">", donneeHTML);
	}

	public int getColonnesEcrites() {
		return colonnesEcrites;
	}

	public int getLignesEcrites() {
		return lignesEcrites;
	}
}
