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
	private int lignesEcrites;
	private int colonnesEcrites;
	private int ligneActuelle;
	private int colonneActuelle;
	private String[][] tableau;

	public Donnee_Html() {
		this.donneeHTML = "";
	}

	/**
	 * Recupere le contenu de la page
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
			supprimerPointsVirgule(this.donneeHTML);
			if(pageComporteTableau()){
				String titreSain = titre.replaceAll("[\\/\\?\\:\\<\\>]", "");
				htmlVersCSV(titreSain);
			}
		}
	}

	/**
	 * Methode qui parcoure les tables du HTML et les convertit en CSV
	 * On cree un fichier csv par tableau trouvé
	 * @throws ConversionInvalideException
	 * @throws IOException 
	 */
	public void htmlVersCSV(String titre) throws IOException {

		Document page = Jsoup.parseBodyFragment(this.donneeHTML);
		Elements wikitables = page.getElementsByClass("wikitable");
		int nbTableaux = getNbTableaux(page);

		for (int i = 0 ; i < nbTableaux ; i++) {
			
			String outputPath = "src/ressources/" + titre + i + ".csv";
			FileOutputStream outputStream = new FileOutputStream(outputPath);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
			this.tableau = new String[100][100];

			// Fill each row with 1.0
			for (String[] ligne: tableau) {
				java.util.Arrays.fill(ligne,"VIDE");
			}

			stockerLignes(wikitables.get(i));
			ecrireTableau(writer);
			writer.close();
		}	
	}

	/**
	 * Methode qui parcoure les lignes puis les cellules d'une wikitable
	 * et qui appelle les methodes permettant de gerer les colspans et rowspans
	 * @param table
	 */
	public void stockerLignes(Element table) {
		Elements lignes = table.getElementsByTag("tr");

		for (Element ligne : lignes) {
			this.colonneActuelle = 0;
			Elements cellules = ligne.select("td, th");
			/* Parcours des cellules de la ligne, et appel de methodes
			gerant les colspans et rowspans si besoin */
			for (Element cellule : cellules) {
				if (cellule.hasAttr("colspan")) {
					int nbColspans = Integer.parseInt(cellule.attr("colspan"));
					gererColspans(nbColspans, cellule);
				}
				else if (cellule.hasAttr("rowspan")) {
					int nbRowspans = Integer.parseInt(cellule.attr("rowspan"));
					gererRowspans(nbRowspans, cellule, this.ligneActuelle);
				}
				else if ((cellule.hasAttr("colspan")) & (cellule.hasAttr("rowspan"))){
					int nbColspans = Integer.parseInt(cellule.attr("rowspan"));
					int nbRowspans = Integer.parseInt(cellule.attr("rowspan"));
					gererColspans(nbColspans, ligne);
					gererRowspans(nbRowspans, cellule, this.ligneActuelle);
				}
				// La cellule est 'normale'
				else {
					stockerCellule(cellule);
				}
				this.colonnesEcrites++;
				this.colonneActuelle++;
			}
			this.ligneActuelle++;
			this.lignesEcrites++;
		}
	}

	/**
	 * Stocke la cellule dans une matrice a deux dimensions representant la wikitable
	 * @param cellule la cellule a ajouter
	 */
	private void stockerCellule(Element cellule) {
		while (!this.tableau[this.ligneActuelle][this.colonneActuelle].equals("VIDE")) {
			this.colonneActuelle++;
		}
		this.tableau[this.ligneActuelle][this.colonneActuelle] = cellule.text().concat("; ");
	}

	/**
	 * Stocke la cellule dans une matrice a deux dimensions representant la wikitable
	 * La ligne n'est pas l'attribut ligneActuelle, mais une ligne donnee en parametre
	 * Methode utilise uniquement lorsqu'une cellule a un attribut rowspan.
	 * @param cellule la cellule a ajouter
	 * @param ligneRowspan la ligne du tableau ou doit etre ajoutee la cellule
	 */
	private void stockerCellule(Element cellule, int ligneRowspan) {
		if (!this.tableau[ligneRowspan][this.colonneActuelle].equals("VIDE")) {
		}
		else {
			this.tableau[ligneRowspan][this.colonneActuelle] = cellule.text().concat("; ");
		}
	}
	
	/**
	 * Cree un fichier csv a partir de la matrice a double dimension tableau.
	 * @param writer
	 * @throws IOException
	 */
	private void ecrireTableau(OutputStreamWriter writer) throws IOException {
		for (int i = 0; i < this.tableau.length; i++) {
			boolean contientInfos = ligneContientInfos(i);
			for (int j = 0; j < this.tableau.length; j++) {
				if (!this.tableau[i][j].equals("VIDE")) {
					writer.write(this.tableau[i][j]);
				}
			}
			// Si la ligne n'est pas vide, alors on fait un saut de ligne
			if (contientInfos) {
				writer.write("\n");				
			}
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

	/**
	 * Suppression des points virgules, interprete dans le csv comme un changement de colonne
	 * @param html
	 */
	private void supprimerPointsVirgule(String html){
		String newHtml = html.replaceAll(";", " ");
		this.donneeHTML = newHtml;
	}

	/**
	 * Methode creeant le nombre de cellules necessaires, en fonction
	 * de la valeur de l'attribut colspan de la cellule
	 * @param nbColspans la valeur de l'attribut colspan
	 * @param cellule la cellule a ajouter
	 */
	private void gererColspans(int nbColspans, Element cellule) {
		for (int i = 0 ; i < nbColspans; i++) {
			this.colonneActuelle = this.colonneActuelle + i;
			stockerCellule(cellule);
		}
	}

	/**
	 * Methode creeant le nombre de cellules necessaires, en fonction
	 * de la valeur de l'attribut colspan de la cellule
	 * @param nbRowspans la valeur de l'attribut rowspan
	 * @param cellule la cellule a ajouter
	 * @param ligneActuelle la ligne actuelle
	 */
	private void gererRowspans(int nbRowspans, Element cellule, int ligneActuelle) {
		for (int i = 0 ; i < nbRowspans; i++) {
			stockerCellule(cellule, ligneActuelle);
			ligneActuelle++;
		}
	}

	/**
	 * Renvoie true si la ligne du tableau a des valeurs provenant
	 * de l'extraction d'une wikitable, false sinon
	 * @param nbLigne le numero de la ligne a tester
	 * @return true ou false
	 */
	private boolean ligneContientInfos(int nbLigne) {
		for (int nbColonne = 0; nbColonne < tableau.length; nbColonne++) {
			if (!tableau[nbLigne][nbColonne].equals("VIDE")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Renvoie le nombre de tableaux du fichier
	 */
	@Override
	public int getNbTableaux(Document page) {
		return page.getElementsByClass("wikitable").size();
	}

	/**
	 * Renvoie le nombre de colonnes ecrites dans le csv
	 * a partir du parsing d'une page
	 * @return
	 */
	public int getColonnesEcrites() {
		return colonnesEcrites;
	}

	/**
	 * Renvoie le nombre de lignes ecrites dans le csv 
	 * a partir du parsing d'une page
	 * @return
	 */
	public int getLignesEcrites() {
		return lignesEcrites;
	}
}
