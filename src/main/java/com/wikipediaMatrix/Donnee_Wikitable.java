package com.wikipediaMatrix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

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
		
		this.tab = new String[100][100];

		initTab();
		
	}

	public String getContenu() {
		return this.wikitable;
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
	// ATTENTION !! Si tableau avec ! ça ne marche pas
	public void wikitableEnTeteVersCSV(String titre, String wikitable) throws IOException {
		int i = 0,j = 0, nbtab = 0;
        
		//if(pageComporteTableau()){
        wikitable = wikitable.replaceAll("\n", "");
        wikitable = wikitable.replaceAll("style=\"text-align:center\"", "");
        wikitable = wikitable.replaceAll("\n", "");
        wikitable = wikitable.replaceAll("(\\|\\|-)", "\\|\\.");
        wikitable = wikitable.replaceAll(";", "");
		wikitable = wikitable.replaceAll("align=\"center\"", "");
		wikitable = wikitable.replaceAll("<br />", "");
		wikitable = wikitable.replaceAll("</center>", "");
		wikitable = wikitable.replaceAll("<center>", "");
        String[] tableaux = wikitable.split("(\\{\\|)|(\\|\\})");
        
        for (String tableau : tableaux) {
			String[] lignes = tableau.split("(\\|-)");
			for (String ligne : lignes) {

				if(ligne.startsWith(" ") || ligne.startsWith("	")) {
					ligne = supprimerEspaceDebut(ligne);
				}
				//System.out.println(ligne);
				if(ligne.startsWith("|") || ligne.startsWith(" |") || ligne.startsWith(" | ") || ligne.startsWith("!") || ligne.contains("! ") || ligne.contains(" !") || ligne.contains("wikitable")) {
					if(ligne.contains("|+")) {
						ligne = ajouteRetourAlaLigneApresTitre(ligne);
					}
					ligne = ligne.replaceAll("(\\|\\|)", ";");
					ligne = ligne.replaceAll("(\\|)", ";");
					ligne = ligne.replaceAll("(!!)", ";");
					ligne = ligne.replaceAll("!", ";");
					if(ligne.contains("class=\"wikitable\"")) {
						ligne = supprimeClassWikitable(ligne);
					}
					if(ligne.contains("[")) {
						ligne = supprimePointVirguleLien(ligne);
					}
					if(ligne.contains("{")) {
						ligne = supprimePointVirguleLienMoustache(ligne);
					}
					ligne = ligne.substring(1, ligne.length());
					//System.out.println(ligne);
					j= remplirTableau(j,i, ligne);
					maxColone = j;
					j=0;
				}
				i++;
			}
			
			if(tableau.contains("wikitable")) {
				nbtab++;
				maxLigne = i;
				//System.out.println(nbtab+" : "+tableau);
				ecrireCsv(titre,nbtab);
				initTab();
			}
			i=0;
        }
		//}
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
		String outputPath = "src/output/" + "titre" + nbtab + ".csv";
		FileOutputStream outputStream = new FileOutputStream(outputPath);
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		
		for(int k = 0; k < maxLigne; k++) {
			for(int l = 0; l < maxColone; l++) {
				//System.out.println(tab[k][l].concat(";"));
				writer.write(tab[k][l].concat(";"));
			}
			writer.write("\n");
		}	
		writer.close();
	}
	
	/**
	 * Permet de gerer les colspan et les rowspan
	 * @param j
	 * @param i
	 * @param ligne
	 * @return
	 */
	public int remplirTableau(int j ,int i, String ligne) {
		String[] mots = ligne.split(";");
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
							//System.out.println(" "+tab[i][j]);
							j++;
						}
					}
					
					if(row){
						for(int x = 0; x < nbrow; x++) {
							tab[i+x][j]=mot;
							//System.out.println(" "+tab[i+x][j]);
						}
						nbrow=0;
						j++;
					}

					if(!col && !row){
						tab[i][j] = mot;
						//System.out.println(" "+tab[i][j]);
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
	 * Supprimer les point virgule des lien [[]]
	 * @param ligne
	 * @return
	 */
	public String supprimePointVirguleLien(String ligne) {
		boolean inter = false;
		for (int i=0; i < ligne.length(); i++){
			if (ligne.charAt(i) == '[') {
				inter = true;
			}
			if(inter && ligne.charAt(i) == ';') {
				ligne= ligne.substring(0, i-1)+"/"+ligne.substring(i+1,ligne.length());
			}
			if(inter && ligne.charAt(i) == ']') {
				inter=false;
			}
				
		}
		ligne = ligne.replaceAll("\\[", "");
		ligne = ligne.replaceAll("\\]", "");
		return ligne;
	}
	
	/**
	 * Supprimer les point virgule des lien {{}}
	 * @param ligne
	 * @return
	 */
	public String supprimePointVirguleLienMoustache(String ligne) {
		boolean inter = false;
		for (int i=0; i < ligne.length(); i++){
			if (ligne.charAt(i) == '{') {
				inter = true;
			}
			if(inter && ligne.charAt(i) == ';') {
				ligne= ligne.substring(0, i-1)+"/"+ligne.substring(i+1,ligne.length());
			}
			if(inter && ligne.charAt(i) == '}') {
				inter=false;
			}
				
		}
		ligne = ligne.replaceAll("\\{", "");
		ligne = ligne.replaceAll("\\}", "");
		return ligne;
	}
	
	/**
	 * supprime class="wikitable"
	 * @param ligne
	 * @return
	 */
	public String supprimeClassWikitable(String ligne) {
		boolean okay = true;
		boolean drap = false;
		for (int i=0; i < ligne.length(); i++){
			if (ligne.charAt(i) == ';' && okay==true) {
				ligne= ligne.substring(i+1,ligne.length());
				okay=false;
				drap = true;
			}			
		}
		
		if(!drap) {
			ligne=" ";
		}
		return ligne;
	}
	
	/**
	 * Ajoute un retour a la ligne apres le titre d un tableau
	 * @param ligne
	 * @return
	 */
	public String ajouteRetourAlaLigneApresTitre(String ligne) {
		
		boolean drap = false;
		
		for (int i=0; i < ligne.length(); i++){
			if (ligne.charAt(i) == '+') {
				drap = true;
			}
			if (ligne.charAt(i) == '!' && drap == true) {
				ligne= ligne.substring(1,i-1)+"\n"+ligne.substring(i+1,ligne.length());
				drap = false;
			}			
		}
		return ligne;
	}
	
	/**
	 * Supprime les espaces en debut de ligne
	 * @param ligne
	 * @return
	 */
	private String supprimerEspaceDebut(String ligne) {
		int i = 0;
		while(ligne.startsWith(" ") || ligne.startsWith("	")) {
			ligne = ligne.substring(i+1,ligne.length());
			i++;
		}
		return ligne;
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
	public int getNbTableaux(String contenuPage) {
		return StringUtils.countMatches(contenuPage, "{|");
	}
}
