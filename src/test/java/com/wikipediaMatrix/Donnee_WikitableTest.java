package test.java.com.wikipediaMatrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.junit.Test;
import org.mockito.Mockito;

import main.java.com.wikipediaMatrix.Donnee_Html;
import main.java.com.wikipediaMatrix.Donnee_Wikitable;
import main.java.com.wikipediaMatrix.ExtractionInvalideException;
import main.java.com.wikipediaMatrix.Url;

public class Donnee_WikitableTest {

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
				test.wikitableVersCSV("Comparison_between_Esperanto_and_Ido",wikitable);

			}catch(ExtractionInvalideException e) {
				e.printStackTrace();
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * La methode pageCompoteTableau doit renvoyer true si la page contient au monis un tableau
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void page_comporte_tableau() throws ExtractionInvalideException {
		// On instancie un objet de la classe Donnee_Html
		Donnee_Wikitable donnee_WikitextTest  = new Donnee_Wikitable();
		donnee_WikitextTest.setWikitable("{| class=\"wikitable\" |}");
		assertTrue(donnee_WikitextTest.pageComporteTableau());
	}
	
	/**
	 * La methode pageComporteTableau doit renvoyer false si la page ne contient pas de tableau
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void page_ne_comporte_pas_tableau() throws ExtractionInvalideException {
		Donnee_Wikitable donnee_WikitextTest = new Donnee_Wikitable(); 
		donnee_WikitextTest.setWikitable("");
		assertFalse(donnee_WikitextTest.pageComporteTableau());
	}
	
	/**
	 * La methode supprDonneesInutuiles doit supprimer toutes les donnees qui ne sont pas utiles pour la suite
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void supprDonneesInutuiles() throws ExtractionInvalideException {
		Donnee_Wikitable donnee_WikitextTest = new Donnee_Wikitable();
		String test = donnee_WikitextTest.wikitableReplace("scope=col;style=\"text-align:center\"align=\"center\"|&nbsp;<br /></center>|-/");
		assertEquals(test, ", | -/");
	}
	
	/**
	 * La methode supprEspacesInutuiles doit supprimer tous les espaces qui ne sont pas utiles pour la suite
	 * @throws ExtractionInvalideException
	 */
	@Test
	public void supprEspacesInutuiles() throws ExtractionInvalideException {
		Donnee_Wikitable donnee_WikitextTest = new Donnee_Wikitable();
		String test = donnee_WikitextTest.supprimerEspaceDebut("   wikitexte");
		assertEquals(test, "wikitexte");
	}
}
