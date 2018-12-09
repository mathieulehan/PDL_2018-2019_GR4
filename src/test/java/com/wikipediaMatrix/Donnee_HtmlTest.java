package com.wikipediaMatrix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.wikipediaMatrix.Donnee;
import com.wikipediaMatrix.Donnee_Html;
import com.wikipediaMatrix.ExtractionInvalideException;
import com.wikipediaMatrix.Url;

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
		// On instancie un objet de la classe Donnee_Html
		Donnee_Html donnee_HtmlTest  = new Donnee_Html(); 
		donnee_HtmlTest.setHtml("<html><head></head><body><table class=\"wikitable\"></table></body></html>");
		assertTrue(donnee_HtmlTest.pageComporteTableau());
	}

	/**
	 * TODO terminer la fonction pageComporteTableau
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void page_ne_comporte_pas_tableau() throws ExtractionInvalideException {
		final Donnee_Html donnee_HtmlTest  = new Donnee_Html(); 
		donnee_HtmlTest.setHtml("");
		assertFalse(donnee_HtmlTest.pageComporteTableau());
	}
	
	/**
	 * On ne doit supprimer que les points virgules
	 */
	@Test
	public void suppressionPointsVirgules() {
		Donnee_Html donnee_HtmlTest = new Donnee_Html();
		String htmlFactice = "aZ012-*/'|[] {}();";
		donnee_HtmlTest.supprimerPointsVirgule(htmlFactice);
		assertTrue(donnee_HtmlTest.getHtml().equals("aZ012-*/'|[] {}() "));
	}
	
	/**
	 * La methode getNbTableaux doit renvoyer le bon nombre de tableaux
	 */
	@Test
	public void getNbTableaux() {
		Donnee_Html donnee_Html = new Donnee_Html();
		donnee_Html.setHtml("<table class=\"wikitable\"></table><table class=\"wikitable\"></table>");
		Document page = Jsoup.parseBodyFragment(donnee_Html.getHtml());
		Elements wikitables = page.getElementsByClass("wikitable");
		Elements tablesNonWiki = page.select("table:not([^])");
		for (Element element : tablesNonWiki) {
			wikitables.add(element);
		}
		assertTrue(wikitables.size() == 2);
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
}