package test;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Donnee_HtmlTest {
	
	@Test
	void testTempsExec(long nbATest) {
		assertTrue("Temps d'exécution de " + nbATest/1000 + " secondes", nbATest < 24000);
	}
	
	@Test
	void testRecupTables() {
		/*ExtractWikitable testExtractTable = new ExtractWikitable();
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader("test.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		String html = contentBuilder.toString();
		ParseHTML testParseHTML = new ParseHTML(html);
		testParseHTML.htmlToCSV(html, "C:\\Users\\mathi\\Documents\\wikitext.csv");*/
	}
	

}
