package classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
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
			/* On recupere le nombre calcule de lignes et de colonnes de tous
			les tableaux de l'url*/
			nbLignesColonnes(url);
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
			System.out.println(titre);
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

			String outputPath = "src/output/" + titre + i + ".csv";
			System.out.println("Tableau n° " + i);
			FileOutputStream outputStream = new FileOutputStream(outputPath);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
			// On recupere le nmobre de lignes et de colonnes du tableau en cours
			int nbLignes = getNbLignesTableaux().get(i);
			int nbColonnes = getNbColonnesTableaux().get(i);
			// On initialise la matrice de donnees a la bonne taille
			this.tableau = new String[nbLignes][nbColonnes];
			this.ligneActuelle = 0;
			// On remplit toutes les lignes et colonnes de la matrice
			for (String[] ligne: tableau) {
				java.util.Arrays.fill(ligne,"VIDE");
			}
			// On stocke les donnees en provenance de la wikitable dans une matrice
			stockerLignes(wikitables.get(i));
			// On cree un fichier CSV en parcourant la matrice
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
		// On parcoure les lignes de la wikitable
		for (Element ligne : lignes) {
			this.colonneActuelle = 0;
			Elements cellules = ligne.select("td, th");
			/* Parcours des cellules de la ligne, et appel de methodes
			gerant les colspans et rowspans si besoin */
			for (Element cellule : cellules) {
				// Si on un colspan et un rowspan sur la meme cellule
				if ((cellule.hasAttr("colspan")) & (cellule.hasAttr("rowspan"))){
					int nbColspans = Integer.parseInt(cellule.attr("colspan"));
					int nbRowspans = Integer.parseInt(cellule.attr("rowspan"));
					gererColspansEtRowspans(nbRowspans, nbColspans, cellule);
				}
				// Si on a un rowspan uniquement
				else if (cellule.hasAttr("rowspan")) {
					int nbRowspans = Integer.parseInt(cellule.attr("rowspan"));
					gererRowspans(nbRowspans, cellule, this.ligneActuelle);
				}
				// Si on a un colspan uniquement
				else if (cellule.hasAttr("colspan")) {
					int nbColspans = Integer.parseInt(cellule.attr("colspan"));
					gererColspans(nbColspans, cellule, this.colonneActuelle);
				}
				// La cellule est 'normale'
				else {
					stockerCellule(cellule);
				}
				/* TODO : ici on incremente colonnesEcrites de 1 seulement, alors qu'une cellule peut creer plusieurs colonnes
					dans le cas d'un colspan par exemple*/
				this.colonnesEcrites++;
				this.colonneActuelle++;
			}
			/* TODO : ici on incremente lignesEcrites de 1 seulement, alors qu'une cellule peut creer plusieurs lignes
			dans le cas d'un rowspan par exemple*/
			this.ligneActuelle++;
			this.lignesEcrites++;
		}
	}

	/**
	 * Stocke la cellule dans une matrice a deux dimensions representant la wikitable
	 * @param cellule la cellule a ajouter
	 */
	private void stockerCellule(Element cellule) {
		/* Si les coordonnees donnees en parametre sont deja reservees, on avance 
		 	d'autant de colonnes qu'il faudra jusqu'a pouvoir stocker notre cellule*/
		while(!this.tableau[this.ligneActuelle][this.colonneActuelle].equals("VIDE")) {
			this.colonneActuelle++;
		}
		// On ajoute le texte de la cellule extraite a la matrice
		if (this.tableau[this.ligneActuelle][this.colonneActuelle].equals("VIDE")) {
			this.tableau[this.ligneActuelle][this.colonneActuelle] = cellule.text().concat("; ");
		}
	}

	/**
	 * Stocke la cellule dans une matrice a deux dimensions representant la wikitable
	 * La ligne n'est pas l'attribut ligneActuelle, mais une ligne donnee en parametre
	 * Methode utilise uniquement lorsqu'une cellule a un attribut rowspan.
	 * @param cellule la cellule a ajouter
	 * @param ligneRowspan la ligne du tableau ou doit etre ajoutee la cellule
	 */
	private void stockerCelluleColspan(Element cellule, int colonneActuelle) {
		if (!this.tableau[this.ligneActuelle][colonneActuelle].equals("VIDE")) {
		}
		else {
			this.tableau[this.ligneActuelle][colonneActuelle] = cellule.text().concat("; ");
		}
	}

	private void stockerCelluleRowspan(Element cellule, int ligneActuelle) {
		if (!this.tableau[ligneActuelle][this.colonneActuelle].equals("VIDE")) {
		}
		this.tableau[ligneActuelle][this.colonneActuelle] = cellule.text().concat("; ");
	}

	private void stockerRowspanSuivantColspan(Element cellule, int ligneActuelle, int colonneActuelle) {
		if (!this.tableau[ligneActuelle][colonneActuelle].equals("VIDE")) {
		}
		this.tableau[ligneActuelle][colonneActuelle] = cellule.text().concat("; ");
	}

	/**
	 * Cree un fichier csv a partir de la matrice a double dimension tableau.
	 * @param writer
	 * @throws IOException
	 */
	private void ecrireTableau(OutputStreamWriter writer) throws IOException {
		for (int i = 0; i < this.tableau.length; i++) {
			boolean contientInfos = ligneContientInfos(i);
			for (int j = 0; j < this.tableau[i].length; j++) {
				int longueur = this.tableau.length;
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
	private void gererColspans(int nbColspans, Element cellule, int colonneActuelle) {
		for (int i = 0 ; i < nbColspans; i++) {
			stockerCelluleColspan(cellule, colonneActuelle);
			if(!(i+1 == nbColspans)) {
				colonneActuelle++;
			}
		}
		this.colonneActuelle++;
		this.colonneActuelle = colonneActuelle;
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
			stockerCelluleRowspan(cellule, ligneActuelle);
			ligneActuelle++;
		}
	}

	/**
	 * Methode gerant le stockage d'une cellule contenant un attribut colspan et rowspan
	 * Traite d'abord le colspan, puis le rowspan en prenant en compte le traitement du colspan
	 * @param nbRowspans la valeur du rowspan
	 * @param nbColspans la valeur du colspan
	 * @param cellule la cellule courante
	 */
	private void gererColspansEtRowspans(int nbRowspans, int nbColspans, Element cellule) {
		int colonneColspan = this.colonneActuelle;
		for (int i = nbColspans; i > 0; i--) {
			int ligneRowspan = this.ligneActuelle+1;
			stockerCelluleColspan(cellule, colonneColspan);
			for (int j = nbRowspans-1; j > 0; j--) {
				stockerRowspanSuivantColspan(cellule, ligneRowspan, colonneColspan);
				ligneRowspan++;
			}
			colonneColspan++;
		}
		this.colonneActuelle = colonneColspan-1;
	}

	/**
	 * Renvoie true si la ligne du tableau a des valeurs provenant
	 * de l'extraction d'une wikitable, false sinon
	 * @param nbLigne le numero de la ligne a tester
	 * @return true ou false
	 */
	private boolean ligneContientInfos(int nbLigne) {
		for (int nbColonne = 0; nbColonne < tableau[nbLigne].length; nbColonne++) {
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
