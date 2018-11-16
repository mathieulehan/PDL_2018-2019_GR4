package classes;

import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ConvertionInvalideException;
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

	public Donnee_Html(String donneeHTML) {
		this.donneeHTML = donneeHTML;
	}

	/**
	 * Recupere le contenu et le modifie en CSV
	 * @param url
	 * @throws UrlInvalideException 
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 * @throws ConvertionInvalideException 
	 */
	@Override
	public void extraire(Url url) throws UrlInvalideException, ExtractionInvalideException, MalformedURLException, ConvertionInvalideException {
		if(url.estUrlValide()) {
			String langue = url.getLangue();
			String titre = url.getTitre();

			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			donneeHTML = "" + recupContenu(page);
			htmlVersCSV();
		}
	}

	/**
	 * Methode qui parcoure les tables du HTML et les convertit en CSV
	 * @throws ConvertionInvalideException 
	 */
	public void htmlVersCSV() throws ConvertionInvalideException {
		String outputPath = "src/ressources/html.csv";
		try {
			FileWriter writer = new FileWriter(outputPath);
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
			throw new ConvertionInvalideException("Convertion HTML vers CSV incorrecte");
		}

	}

	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	public boolean pageComporteTableau() throws ExtractionInvalideException {
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
