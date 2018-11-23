package test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
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
		assertTrue(donnee_HtmlTest.pageComporteTableau(""));
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
	 * Test ou l'url est valide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void url_valide() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estUrlValide()).thenReturn(true);
		assertTrue(urlTest.estUrlValide());
	}
	
	/**
	 * Test ou le titre donc l'url sont invalide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void url_invalide_titre_invalide() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estTitreValide()).thenReturn(false);
		Mockito.when(urlTest.estLangueValide()).thenReturn(true);
		assertFalse(urlTest.estUrlValide());
	}
	
	/**
	 * Test ou la langue donc l'url sont invalide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void url_invalide_langue_invalide() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estTitreValide()).thenReturn(true);
		Mockito.when(urlTest.estLangueValide()).thenReturn(false);
		assertFalse(urlTest.estUrlValide());
	}
	
	/**
	 * Test ou le titre et la langue sont invalides donc l'url est invalide
	 * Aucune exception ne doit donc etre generee
	 * @param url l'url de la page wikipedia
	 * @throws UrlInvalideException
	 * @throws MalformedURLException 
	 */
	@Test
	public void url_invalide_titre_et_langue_invalides() throws UrlInvalideException, MalformedURLException {
		Mockito.when(urlTest.estTitreValide()).thenReturn(false);
		Mockito.when(urlTest.estLangueValide()).thenReturn(false);
		assertFalse(urlTest.estUrlValide());
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