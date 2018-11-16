package test;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import classes.Donnee_Html;
import classes.Url;
import exceptions.ConvertionInvalideException;
import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.*;

public class Donnee_HtmlTest {

	/**
	 * Renvoie un message dans le cas ou l URL est fausse
	 */
	@Test
	void testPageNonExistante() {
		/*ExtractWikitable testExtractPageNonExistante = new ExtractWikitable();
		assertThrows(IOException.class,() -> {
			testExtractPageNonExistante.extractWikiTable("en", "render", "erreurPage");
	    });*/
	}

	/**
	 * Renvoie un message dans le cas ou la langue choisie est erronee
	 */
	@Test
	void erreurLangue() {
		/*ExtractWikitable testErreurLangue = new ExtractWikitable();
		assertThrows(UnknownHostException.class,() -> {
			testErreurLangue.extractWikiTable("erreurLangue", "render", "Wikipedia:Unusual_articles/Places_and_infrastructure");
	    });*/
	}

	/**
	 * Renvoie un message si le temps d execution depasse un temps maximal
	 * @param nbATest
	 * @throws MalformedURLException 
	 * @throws ConvertionInvalideException 
	 * @throws ExtractionInvalideException 
	 * @throws UrlInvalideException 
	 */
	@Test
	void testTempsExec(long nbATest) throws MalformedURLException, UrlInvalideException, ExtractionInvalideException, ConvertionInvalideException {
		Url monUrl = new Url(new URL("https://fr.wikipedia.org/wiki/Kevin_Bacon"));
		Donnee_Html donnees = new Donnee_Html("");
		donnees.extraire(monUrl);

		assertTrue("Temps d'execution de " + nbATest/1000 + " secondes", nbATest < 24000);
	}


}
