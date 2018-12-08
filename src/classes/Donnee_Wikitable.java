package classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

/**
 * Classe permettant de recuperer et convertir des tables HTML en CSV
 * @author mathi & thomas
 *
 */

public class Donnee_Wikitable extends Donnee{

	private String wikitable;
	private int lignesEcrites = 0;
	private int colonnesEcrites = 0;
	private String[][] tab;
	private int maxLigne = 0;
	private int maxColone = 0;

	public Donnee_Wikitable(){
		this.wikitable = "";
		
		this.tab = new String[500][500];

		initTab();
		
	}

	/**
	 * Recupere les donnees en JSON pour les mettre dans un CSV
	 * @param url
	 * @throws UrlInvalideException 
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 */
	@Override
	public void extraire(Url url) throws  UrlInvalideException, ExtractionInvalideException, MalformedURLException, IOException {
		if(url.estUrlValide()) {
			String langue = url.getLangue();
			String titre = url.getTitre();

			URL page = new URL("https://"+langue+".wikipedia.org/w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json");
			String json = recupContenu(page);
			wikitable = jsonVersWikitable(json);
			wikitableEnTeteVersCSV(titre,wikitable);
		}
	}

	/**
	 * Recupere le wikitext dans le JSON
	 * @param json
	 * @return String
	 * @throws ExtractionInvalideException 
	 */
	public String jsonVersWikitable(String json) throws ExtractionInvalideException {
		try {
			JSONObject objetJson1 = new JSONObject(json);
			JSONObject objetJson2 = (JSONObject) objetJson1.get("parse");
			JSONObject objetJson3 = (JSONObject) objetJson2.get("wikitext");
			
			return objetJson3.getString("*");
		} catch (Exception e) {
			throw new ExtractionInvalideException("Extraction JSON vers Wikitext echouee");
		}
	}
	
	/**
	 * 
	 * @param wikitable
	 * @throws IOException 
	 * @throws ExtractionInvalideException
	 */
	public void wikitableEnTeteVersCSV(String titre, String wikitable) throws IOException {
		int i = 0,j = 0, nbtab = 0;
		boolean first = false, tableau = false, dansCell = false, drap = false;
		
		wikitable = wikitableReplace(wikitable);
						
		String[] lignes = wikitable.split("\n");
		
		for (String ligne : lignes) {
			if(ligne.startsWith(" ") || ligne.startsWith("	")) {
				ligne = supprimerEspaceDebut(ligne);
			}
			//System.out.println(ligne);
			if(ligne.startsWith("{|")) {
				i=0;
				nbtab++;
				first = true;
				tableau = true;
			}
			
			if(ligne.startsWith("!") && tableau) {
				if(ligne.contains("!!")) {
					String[] mots = ligne.split("(!!)");
					for(String mot : mots) {
						while(tab[i][j] != "VIDE") {
							j++;
						}
						tab[i][j] = mot;
						j++;
					}
				}else {
					if(ligne.contains("colspan") || ligne.contains("rowspan")) {
						j = gererColspanEtRowspan(ligne, i, j);
					}else {
						while(tab[i][j] != "VIDE") {
							j++;
						}
						ligne = ligne.substring(1, ligne.length());
						tab[i][j] = ligne;
						j++;
					}
				}
				first = false;
			}else if(ligne.startsWith("|") && tableau) {
				if(ligne.startsWith("|-")) {
					if(!first) {
						i++;
						if(j>maxColone) {
							maxColone = j;
						}
					}
					j=0;
				}else if(ligne.contains("|}")) {
						if(drap) {
							j++;
						}
						if(j>maxColone) {
							maxColone = j;
						}
						maxLigne = i+1;
						ecrireCsv(titre, nbtab);
						initTab();
						maxColone = 0;
						maxLigne = 0;
						tableau = false;
						j=0;
						i=0;
				}else if(ligne.startsWith("|+")){
						ligne = "";
				}else {
					ligne = ligne.substring(1, ligne.length());
					if(ligne.contains("||")) {
						String[] mots = ligne.split("(\\|\\|)");
						for(String mot : mots) {
							tab[i][j] = mot;
							j++;
						}
					}else {
						if(ligne.contains("colspan") || ligne.contains("rowspan")) {
							j = gererColspanEtRowspan(ligne, i, j);
						}else {
							
							while(tab[i][j] != "VIDE") {
								j++;
							}
							tab[i][j] = ligne;
							j++;
						}
					}
				}
				first = false;
				dansCell = false;
				drap = false;
			}else if(tableau && !first) {
				if(!dansCell) {
					j--;
					dansCell = true;
				}
				tab[i][j] = tab[i][j]+ligne;
				drap = true;
			}
		}
	}
	
	public int gererColspanEtRowspan(String ligne, int i, int j) {
		if(ligne.endsWith("|")) {
			ligne = ligne+" ";
		}
		String[] mots = ligne.split("\\|");
		int nbrow = 0;
		int nbcol = 0;
		boolean drap = false, col = false, row = false;
		for(String mot : mots) {
			while(tab[i][j] != "VIDE") {
				j++;
			}
					
			if(mot.contains("colspan")) {
				mot = mot.replaceAll("colspan", ",");
				for (int a=0; a < mot.length(); a++){
					if (mot.charAt(a) == ',' && mot.charAt(a+1) == '=') {
						if (mot.charAt(a+2) == '"') {
							nbcol = Integer.parseInt(Character.toString(mot.charAt(a+3)));
							mot = mot.substring(a+5, mot.length());
						}else {
							nbcol = Integer.parseInt(Character.toString(mot.charAt(a+2)));
							mot = mot.substring(a+3, mot.length());
						}
					}
				}
				col = true;
				drap = true;
			}
			if(mot.contains("rowspan")) {
				mot = mot.replaceAll("rowspan", ",");
				for (int a=0; a < mot.length(); a++){
					if (mot.charAt(a) == ',' && mot.charAt(a+1) == '=') {
						if (mot.charAt(a+2) == '"') {
							nbrow = Integer.parseInt(Character.toString(mot.charAt(a+3)));
						}else {
							nbrow = Integer.parseInt(Character.toString(mot.charAt(a+2)));
						}
					}
				}
				row = true;
				drap = true;
			}
			if(!drap) {
				
				if(row && col) {
					for(int x = 0; x < nbrow; x++) {
						for(int y = 0; y < nbcol; y++) {
							tab[i+x][j+y]=mot;
						}
					}
					j+=nbcol;
				}else {
					if(col){
						for(int x = 0; x < nbcol; x++) {
							tab[i][j]=mot;
							j++;
						}
					}
					
					if(row){
						for(int x = 0; x < nbrow; x++) {
							tab[i+x][j]=mot;
						}
						nbrow=0;
						j++;
					}

					if(!col && !row){
						tab[i][j] = mot;
						j++;
					}
				}
				col = false;
				row = false;
			}
			drap=false;
		}
		return j;
	}
	
	/**
	 * Initialise le tableau
	 */
	public void initTab() {
		for (String[] ligne: tab) {
			java.util.Arrays.fill(ligne,"VIDE");
		}
	}
	
	/**
	 * ecrit les fichier csv
	 * @param titre
	 * @param nbtab
	 * @throws IOException
	 */
	public void ecrireCsv(String titre, int nbtab) throws IOException{
		boolean drap = false;
		String outputPath = "src/ressources/" + titre + nbtab + ".csv";
		FileOutputStream outputStream = new FileOutputStream(outputPath);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		
		for(int k = 0; k < maxLigne; k++) {
			for(int l = 0; l < maxColone; l++) {
				//System.out.println(tab[k][l].concat(";"));
				if(tab[k][l].startsWith("!")) {
					tab[k][l]=tab[k][l].substring(1, tab[k][l].length());
				}
				if(tab[k][l] != "VIDE") {
					writer.write(tab[k][l].concat(";"));
				}else {
					drap = true;
				}
			}
			if(!drap) {
				writer.write("\n");
			}else {
				maxLigne--;
			}
		}	
		writer.close();
	}

	/**
	 * Supprime les espaces en debut de ligne
	 * @param ligne
	 * @return
	 */
	private String supprimerEspaceDebut(String ligne) {
		while(ligne.startsWith(" ") || ligne.startsWith("	")) {
			ligne = ligne.substring(1,ligne.length());
		}
		return ligne;
	}
	
	public String wikitableReplace(String wikitable) {
        wikitable = wikitable.replaceAll("scope=col", "");
        wikitable = wikitable.replaceAll("style=\"text-align:center\"", "");
        wikitable = wikitable.replaceAll("align=\"center\"\\|", "");
        wikitable = wikitable.replaceAll("align=\"center\"", "");
        wikitable = wikitable.replaceAll("align=center", "");
        wikitable = wikitable.replaceAll("align=\"right\"", "");
        wikitable = wikitable.replaceAll("align=right", "");
        wikitable = wikitable.replaceAll("align=\"left\"", "");
        wikitable = wikitable.replaceAll("align=left", "");
        wikitable = wikitable.replaceAll("align=\"top\"", "");
        wikitable = wikitable.replaceAll("align=top", "");
        wikitable = wikitable.replaceAll("&nbsp;", " ");
		wikitable = wikitable.replaceAll(";", ",");
		wikitable = wikitable.replaceAll("<br />", "");
		wikitable = wikitable.replaceAll("</center>", "");
		wikitable = wikitable.replaceAll("<center>", "");
		wikitable = wikitable.replaceAll("\\|-/", "\\| -/");
		
		return wikitable;
	}

	
	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	boolean pageComporteTableau() throws ExtractionInvalideException {
		if(!wikitable.contains("{|")){
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

	@Override
	public int getNbTableaux(Document page) {
		return StringUtils.countMatches("{|", wikitable);
	}
}
