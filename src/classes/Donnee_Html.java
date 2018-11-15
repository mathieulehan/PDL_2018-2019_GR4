package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	private String html;
	private String outputPath = "src/ressources/html.csv";
	private int lignesEcrites = 0;
	private int colonnesEcrites = 0;

	public Donnee_Html(String html) {
		this.html = html;
	}

	/**
	 * Recupere le contenu et le modifie en CSV
	 * @param url
	 * @throws IOException
	 * @throws UrlInvalideException 
	 * @throws ExtractionInvalideException 
	 */
	@Override
	public void extraire(Url url) throws UrlInvalideException, IOException, ExtractionInvalideException {
		if(url.estUrlValide()) {
			String langue = url.getLangue();
			String titre = url.getTitre();

			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			html = "" + recupContenu(page);

			Donnee_Html donneeHTML = new Donnee_Html(html);
			donneeHTML.htmlVersCSV();
		}
	}

	/**
	 * Methode qui parcoure les tables du HTML et les convertit en CSV
	 * @throws IOException 
	 * @throws ExtractionInvalideException
	 */
	public void htmlVersCSV() throws IOException, ExtractionInvalideException {
		FileWriter writer = new FileWriter(outputPath);
		Document page = Jsoup.parseBodyFragment(html);
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

	}

	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	public boolean pageComporteTableau() throws ExtractionInvalideException {
		Document page = Jsoup.parseBodyFragment(html);
		if(page.getElementsByTag("table") == null){
			throw new ExtractionInvalideException("Aucun tableau présent dans la page");
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
