import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Classe permettant de convertir des tables html en CSV
 * @author mathi
 *
 */
public class ParseHTML {

	/**
	 * Le HTML de la page wikipédia
	 */
	private String html;

	public ParseHTML(String html) {
		this.html = html;
	}
	/**
	 * Méthode qui parcoure les tables du HTMl et les convertit en CSV
	 * @param html
	 */
	public void htmlToCSV(String html) {

		try {
			FileWriter writer = new FileWriter("C:\\Users\\mathi\\Documents\\html.csv");
			
			Document page = Jsoup.parseBodyFragment(html);
			Elements lignes = page.getElementsByTag("tr");
			
			for (Element ligne : lignes) {
				Elements cellules = ligne.getElementsByTag("td");
				for (Element cellule : cellules) {
					writer.write(cellule.text().concat("; "));
				}
				writer.write("\n");
			}
			writer.close();
		}
		catch (IOException e) {
			e.getStackTrace();
		}
		
	}
}
