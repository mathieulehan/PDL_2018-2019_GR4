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
			FileWriter writer = new FileWriter(outputPath);
			//if(pageComporteTableau()){
				wikitable = wikitable.replaceAll("\n", "");
				String[] lignes = wikitable.split("(\\|-)");
				wikitable = wikitable.replaceAll(" align=\"center\" ", "");
				for (String ligne : lignes) {
					System.out.println(ligne);
					if (ligne.startsWith("!")) {
						
						ligne.substring(14, ligne.length());
						writer.write(ligne.concat(";"));
					}
					else{
						ligne = "";
						writer.write("bidule");
					}
					//ligne = ligne.replaceAll("\\{(.*?)\\}", "");
				}
			//}
			writer.close();
		}
		catch (Exception e) {
			throw new ExtractionInvalideException("En-tete vers CSV : extraction et convertion echouees");
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
