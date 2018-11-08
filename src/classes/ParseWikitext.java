package classes;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

/**
 * Classe permettant de convertir des tables wikitext en CSV
 * @author mathi
 */
public class ParseWikitext {
	/**
	 * Le wikitext de la page wikipédia contenu dans un json
	 */
	private String wikitext;

	public ParseWikitext(String wikitext) {
		this.wikitext = wikitext;
	}

	/**
	 * Recupere le wikitext dans le json
	 * @param json
	 * @return
	 */
	public String jsonToWikitable(String json) {
		String content = "";
		try {
			JSONObject objetJson = new JSONObject(json);
			String docs = objetJson.getString("parse");
			JSONObject objetJson2 = new JSONObject(docs);
			String wikitext = objetJson2.getString("wikitext");
			JSONObject objetJson3 = new JSONObject(wikitext);
			content = objetJson3.getString("*");
		} catch (Exception e) {
			e.printStackTrace();
		}
	return content;
	}
	
	/**
	 * Parcoure le json, en extrait les tables et les convertit en CSV
	 * @param wikitable
	 */
	public void wikitableToCSV(String wikitable) {
		try {
			FileWriter writer = new FileWriter("C:\\Users\\mathi\\Documents\\wikitext.csv");
			if(wikitable.contains("|-")){
				wikitable = wikitable.replaceAll("\n", "");
				String[] lignes = wikitable.split("\\|-");
				for (String ligne : lignes) {
					if (ligne.startsWith("| ")) {
						int finHeader = ligne.indexOf("]]'''|", 0);
						ligne = ligne.substring(0, finHeader) + "; " + ligne.substring(finHeader, ligne.length());
						ligne = ligne.replaceAll("(\\{\\{convert\\|)|(\\||adj=\\w+}})|(\\[\\[)|(\\w+]])", "");
						ligne = ligne.replaceAll("\\{(.*?)\\}", "");
//						ligne = ligne.replaceAll( "([^;&\\W&])+" , "");
						ligne = ligne.replaceAll(" (]]''')|( ''')", "");
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
			e.getStackTrace();
		}
	}
}