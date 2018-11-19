package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.mockito.Mockito;

import classes.Donnee;
import classes.Donnee_Html;
import classes.Url;
import exceptions.UrlInvalideException;

public class Donnee_HtmlTest {

	// On "mock" les classes qui vont être utilisées dans le test
	Donnee donneeTest = Mockito.mock(Donnee.class);
	Donnee_Html donnee_HtmlTest = Mockito.mock(Donnee_Html.class);
	Url urlTest = Mockito.mock(Url.class);
	
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
	 * Test ou l'url est valide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void extraireTest1() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estUrlValide()).thenReturn(true);
		// TODO assert ne throw pas d'exception
	}
	
	/**
	 * Test ou l'url est invalide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test(expected = UrlInvalideException.class)
	public void extraireTest2() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estUrlValide()).thenReturn(false);
	}
	
	/**
	 * Renvoie un message si le temps d execution depasse un temps maximal
	 * @param nbATest
	 */
//	@Test
//	public void testTempsExec(long nbATest) {
//		assertTrue("Temps d'execution de " + nbATest/1000 + " secondes", nbATest < 24000);
//	}
	
}