package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import classes.Donnee;
import classes.Donnee_Html;
import classes.Url;
import exceptions.ArticleInexistantException;
import exceptions.ConversionInvalideException;
import exceptions.ExtractionInvalideException;
import exceptions.UrlInvalideException;

public class Donnee_HtmlTest {

	// On "mock" les classes qui vont être utilisées dans le test
	@Mock
	Donnee donneeTest;
	Donnee_Html donnee_HtmlTest;
	Url urlTest = Mockito.mock(Url.class);

	// Dit a Mockito de creer les mocks definis sous l'annotation @Mock
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	public void page_comporte_tableau() throws ExtractionInvalideException {
		// On instancie un objet (mock) de la classe Donnee_Html
		Donnee_Html donnee_HtmlTest  = new Donnee_Html(); 
		//Mockito.when(donnee_HtmlTest.pageComporteTableau()).thenCallRealMethod();
		assertTrue(donnee_HtmlTest.pageComporteTableau());
	}

	/**
	 * TODO terminer la fonction pageComporteTableau
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void page_ne_comporte_pas_tableau() throws ExtractionInvalideException {
		Document docTest = Mockito.mock(Document.class);
		Mockito.when(docTest.getElementsByTag("table")).thenReturn(null);
		//		assertThrows(ExtractionInvalideException.class,
		//	            ()->{
		////	            // on met ici ce qui doit renvoyer une exception
		//				donnee_HtmlTest.pageComporteTableau("");
		//	            });
	}

	/**
	 * Renvoie un message si le temps d execution depasse un temps maximal
	 * @param nbATest
	 * @return un fichier csv en sortie, dans le dossier src/ressources
	 * @throws ExtractionInvalideException 
	 * @throws ArticleInexistantException 
	 * @throws ConversionInvalideException 
	 * @throws UrlInvalideException 
	 * @throws IOException 
	 */
	//	@Test
	//	public void testTempsExec(long nbATest) {
	//		assertTrue("Temps d'execution de " + nbATest/1000 + " secondes", nbATest < 24000);
	//	}

	@Test
	public void mainTest() throws ExtractionInvalideException, UrlInvalideException, ConversionInvalideException, ArticleInexistantException, IOException {
//		Url newUrl = new Url(new URL("https://en.wikipedia.org/wiki/Kevin_Bacon"));
		Url newUrl = new Url(new URL("https://fr.wikipedia.org/wiki/Tableau_p%C3%A9riodique_des_%C3%A9l%C3%A9ments"));
		Donnee_Html test = new Donnee_Html();
		test.extraire(newUrl);
	}

}