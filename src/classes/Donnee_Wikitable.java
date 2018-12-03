package classes;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

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
	private String outputPath = "src/ressources/wikitext.csv";

	public Donnee_Wikitable(){
		this.wikitable = "";
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
			wikitableEnTeteVersCSV(wikitable);
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
	 * Parcours le JSON, extrait les tableaux et les convertis en CSV
	 * @param wikitable
	 * @throws ExtractionInvalideException 
	 */
	public void wikitableVersCSV() throws ExtractionInvalideException {
		try {
			FileWriter writer = new FileWriter(outputPath);
			if(pageComporteTableau()){
				wikitable = wikitable.replaceAll("\n", "");
				String[] lignes = wikitable.split("\\|-");
				for (String ligne : lignes) {
					if (ligne.startsWith("| ")) {
						int finHeader = ligne.indexOf("]]'''|", 0);
						ligne = ligne.substring(0, finHeader) + "; " + ligne.substring(finHeader, ligne.length());
						colonnesEcrites++;
						ligne = ligne.replaceAll("(\\{\\{convert\\|)|(\\||adj=\\w+}})|(\\[\\[)|(\\w+]])", "");
						ligne = ligne.replaceAll("\\{(.*?)\\}", "");
						// ligne = ligne.replaceAll( "([^;&\\W&])+" , "");
						ligne = ligne.replaceAll(" (]]''')|( ''')", "");
						writer.write(ligne.concat("\n"));
						lignesEcrites++;
					}
					else{
						ligne = "";
					}
					ligne = ligne.replaceAll("\\{(.*?)\\}", "");
				}
			}	
			writer.close();
		}
		catch (Exception e) {
			throw new ExtractionInvalideException("Wikitext vers CSV : extraction et convertion echouees");
		}
	}

	/**
	 * 
	 * @param wikitable
	 * @throws ExtractionInvalideException
	 */
	public void wikitableEnTeteVersCSV(String wikitable) throws ExtractionInvalideException {
		try {
			String[][] res = null;
			int i = 0,j = 0;
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF8"));
			if(pageComporteTableau()){
				wikitable = wikitable.replaceAll("\n", "");
				wikitable = wikitable.replaceAll(" align=\"center\"", "");
				String[] lignes = wikitable.split("(\\|-)");
				for (String ligne : lignes) {
					recupPremiereLigneTableau(ligne);
					if(ligne.startsWith("| ") || ligne.startsWith(" |") || ligne.startsWith(" | ") || ligne.startsWith("!")) {
						ligne = ligne.replaceAll("(\\|\\|)", ";");
						ligne = ligne.replaceAll("(\\|)", ";");
						ligne = ligne.replaceAll("!", ";");
						if(ligne.contains("[")) {
							ligne = supprimePointVirguleLien(ligne);
						}
						ligne = ligne.substring(1, ligne.length());
						System.out.println(ligne);
						writer.write(ligne.concat(";"));
						writer.write("\n");
					}
					i++;
				}
			}
			writer.close();
		}
		catch (Exception e) {
			throw new ExtractionInvalideException("En-tete vers CSV : extraction et convertion echouees");
		}
	}
	
	/**
	 * Supprimer les point virgule des lien 
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
	 * Recupere la premiere ligne du tableau
	 * @param ligne
	 * @return
	 */
	public String recupPremiereLigneTableau(String ligne) {
		
		for (int i=0; i < ligne.length(); i++){
			if (ligne.charAt(i) == '{' && ligne.charAt(i+1) == '|') {
				ligne= ligne.substring(i+1,ligne.length());	
			}
			
		}
		
		System.out.println(ligne);
		
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
	
	@Override
	public int getNbTableaux() {
		return StringUtils.countMatches("{|", wikitable);
	}

	public int getColonnesEcrites() {
		return colonnesEcrites;
	}

	public int getLignesEcrites() {
		return lignesEcrites;
	}
}
