package classes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ExtractWikitable {

	public static void extractWikiTable(String langue, String action, String titre) throws IOException {
		String html ="";
		if(action.equals("render")) {
			URL page = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
				html = getContent(page);
			
			ParseHTML parserHTML = new ParseHTML(html);
			parserHTML.htmlToCSV(html,"C:\\Users\\mathi\\Documents\\html.csv");
		}
		else if(action.equals("parse")) {
			URL page = new URL("https://"+langue+".wikipedia.org/w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json");
			String json = getContent(page);
			ParseWikitext parserWikitext = new ParseWikitext(json);
			String wikitable = parserWikitext.jsonToWikitable(json);
			parserWikitext.wikitableToCSV(wikitable);
		}
	}

	public static String getContent(URL url) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			result.append(inputLine);

		in.close();
		return result.toString();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// TEST EXTRACT HTML / WIKITABLE
		extractWikiTable("en", "render", "Wikipedia:Unusual_articles/Places_and_infrastructure");
		extractWikiTable("en", "parse", "Wikipedia:Unusual_articles/Places_and_infrastructure");
	}
}
