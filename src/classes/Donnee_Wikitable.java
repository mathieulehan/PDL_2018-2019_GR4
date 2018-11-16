package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

	public Donnee_Wikitable(String wikitable){
		this.wikitable = wikitable;
	}

	/**
	 * Recupere les donnees en JSON pour les mettre dans un CSV
	 * @param url
	 * @throws UrlInvalideException 
	 * @throws ExtractionInvalideException 
	 * @throws MalformedURLException 
	 */
	@Override
	public void extraire(Url url) throws  UrlInvalideException, ExtractionInvalideException, MalformedURLException {
		if(url.estUrlValide()) {
			String langue = url.getLangue();
			String titre = url.getTitre();

			URL page = new URL("https://"+langue+".wikipedia.org/w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json");
			String json = recupContenu(page);
			wikitable = jsonVersWikitable(json);
			wikitableVersCSV();
		}
	}

	/**
	 * Recupere le wikitext dans le JSON
	 * @param json
	 * @return String
	 * @throws ExtractionInvalideException 
	 */
	public String jsonVersWikitable(String json) throws ExtractionInvalideException {
		String content = "";
		try {
			JSONObject objetJson = new JSONObject(json);
			String docs = objetJson.getString("parse");
			JSONObject objetJson2 = new JSONObject(docs);
			String wikitext = objetJson2.getString("wikitext");
			JSONObject objetJson3 = new JSONObject(wikitext);
			content = objetJson3.getString("*");
		} catch (Exception e) {
			throw new ExtractionInvalideException("JSON vide");
		}
		return content;
	}

	/**
	 * Parcoure le JSON, extrait les tableaux et les convertis en CSV
	 * @param wikitable
	 * @throws ExtractionInvalideException 
	 */
	public void wikitableVersCSV() throws ExtractionInvalideException {
		String outputPath = "src/ressources/wikitext.csv";
		try {
			FileWriter writer = new FileWriter(outputPath);
			if(wikitable.contains("|-")){
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
		catch (IOException e) {
			throw new ExtractionInvalideException("Contenu : extraction et convertion invalide");
		}
	}

	/**
	 * 
	 * @param wikitable
	 * @throws ExtractionInvalideException
	 */
	public void wikitableEnTeteVersCSV(String wikitable) throws ExtractionInvalideException {
		String outputPath = "src/ressources/wikitext.csv";
		try {
			FileWriter writer = new FileWriter(outputPath);
			if(wikitable.contains("{|")){
				wikitable = wikitable.replaceAll("\n", "");
				String[] lignes = wikitable.split("(\\|-)");
				for (String ligne : lignes) {
					if (ligne.startsWith("! ")) {
						ligne.substring(14, ligne.length());
						writer.write(ligne.concat("\n"));
					}
					else{
						ligne = "";
					}
					ligne = ligne.replaceAll("\\{(.*?)\\}", "");
				}
			}	
			writer.close();
		}
		catch (IOException e) {
			throw new ExtractionInvalideException("En-tete : extraction et convertion invalide");
		}
	}

	/**
	 * Verification de la presence de tableaux dans les donnees
	 * @return boolean
	 * @throws ExtractionInvalideException 
	 */
	@Override
	boolean pageComporteTableau() throws ExtractionInvalideException {
		if(!wikitable.contains("|-")){
			throw new ExtractionInvalideException("Aucun tableau pr�sent dans la page");
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
