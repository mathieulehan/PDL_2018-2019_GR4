package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import org.json.JSONObject;

import exceptions.LangueException;

public class Donnee_Wikitable extends Donnee{

	private String wikitext;
	private String outputPath = "src/ressources/wikitext.csv";
	
	public Donnee_Wikitable(String wikitext){
		this.wikitext = wikitext;
	}
	
	public void extraire(String langue, String titre) throws IOException, LangueException {
		if (langue != "fr" || langue != "en" ) {
			throw new LangueException("Exception : Langue invalide");
		}
		
		URL page = new URL("https://"+langue+".wikipedia.org/w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json");
		String json = recupContenu(page);
		Donnee_Wikitable parserWikitext = new Donnee_Wikitable(json);
		String wikitable = parserWikitext.jsonVersWikitable(json);
		parserWikitext.wikitableVersCSV(wikitable);
	}

	public String recupContenu(URL url) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			result.append(inputLine);

		in.close();
		return result.toString();
	}
	
	/**
	 * Recupere le wikitext dans le json
	 * @param json
	 * @return
	 */
	public String jsonVersWikitable(String json) {
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
	public void wikitableVersCSV(String wikitable) {
		try {
			FileWriter writer = new FileWriter(outputPath);
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

	@Override
	boolean pageComporteTableau(String wikitable) {
		// TODO Auto-generated method stub
		if(wikitable.contains("|-")){
			return true;
		}
		return false;
	}
}
