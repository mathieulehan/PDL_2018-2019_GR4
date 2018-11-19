package test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.Mockito;

import classes.Donnee;
import classes.Donnee_Html;
import classes.Url;
import exceptions.ExtractionInvalideException;
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
	
	@Test
	public void page_comporte_tableau() throws ExtractionInvalideException {
		Document docTest = Mockito.mock(Document.class);
		Elements eleTest = Mockito.mock(Elements.class); // objet mock donc null !!!
		Mockito.when(docTest.getElementsByTag("table")).thenReturn(eleTest);
		
		//Mockito.when(donnee_HtmlTest.pageComporteTableau()).thenCallRealMethod();
		assertTrue(donnee_HtmlTest.pageComporteTableau());
	}
	
	@Test(expected = ExtractionInvalideException.class)
	public void page_ne_comporte_pas_tableau() throws ExtractionInvalideException {
		Document docTest = Mockito.mock(Document.class);
		Mockito.when(docTest.getElementsByTag("table")).thenReturn(null);
		donnee_HtmlTest.pageComporteTableau();
	}
	
	
	/**
	 * Test ou l'url est valide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void url_valide() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estUrlValide()).thenReturn(true);
		urlTest.estUrlValide();
	}
	
	/**
	 * Test ou l'url est invalide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test(expected = UrlInvalideException.class)
	public void url_invalide() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estUrlValide()).thenReturn(false);
		urlTest.estUrlValide();
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