package com.wikipediaMatrix;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.junit.*;

public class Donnee_WikitableTest {

	/**
	 * Renvoie un message dans le cas ou l URL est fausse
	 */
	@Test
	public void testPageNonExistante() {
		/*ExtractWikitable testExtractPageNonExistante = new ExtractWikitable();
		assertThrows(IOException.class,() -> {
			testExtractPageNonExistante.extractWikiTable("en", "render", "erreurPage");
	    });*/
	}

	/**
	 * Renvoie un message dans le cas ou la langue choisie est erronee
	 */
	@Test
	public void erreurLangue() {
		/*ExtractWikitable testErreurLangue = new ExtractWikitable();
		assertThrows(UnknownHostException.class,() -> {
			testErreurLangue.extractWikiTable("erreurLangue", "render", "Wikipedia:Unusual_articles/Places_and_infrastructure");
	    });*/
	}

	/**
	 * Test tout a la wanaghen, en gros c est un main dans les tests
	 * @throws ExtractionInvalideException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	@Test
	public void testCreationCSV() throws ExtractionInvalideException, IOException {
		URL monUrl;
		String contenu = "", wikitable = "";
		try {
			//https://fr.wikipedia.org/w/api.php?action=parse&page=Kevin_Bacon&prop=wikitext&format=json
			//
			monUrl = new URL("https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_between_Esperanto_and_Ido&prop=wikitext&format=json");
			Url newUrl = new Url(monUrl);
			Donnee_Wikitable test = new Donnee_Wikitable();
			try {
				contenu = test.recupContenu(newUrl.getURL());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				wikitable = test.jsonVersWikitable(contenu);			
				test.wikitableEnTeteVersCSV("titre",wikitable);

			}catch(ExtractionInvalideException e) {
				e.printStackTrace();
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Si la page ne comporte pas de tableau, 
	 * on genere une {@link ExtractionInvalideException}
	 * On assert l'exception avec JUnit 5 comme ci-dessous
	 * (on utilise une lambda expression de Java 8)
	 */
//	@Test
//	void pasDeTableau() {
//		Donnee_Wikitable donneeWikitableTest = Mockito.mock(Donnee_Wikitable.class);
//		Mockito.when(donneeWikitableTest.wikitable.contains())
//	    assertThrows(NullPointerException.class,
//	            ()->{
//	            // on met ici ce qui doit renvoyer une exception
//	            donneeWikitableTest.pageComporteTableau();
//	            });
//	}
}
