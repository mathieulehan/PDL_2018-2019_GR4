package test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import classes.ComparerCSV;
import classes.Donnee_Html;
import classes.Donnee_Wikitable;
import classes.Donnee;

/**
 * Classe de tests unitaires JUnit, utilisant Mockito pour tester les methodes de la classe ComparerCSV 
 * @author mathi
 *
 */
public class ComparerCSVTest {
	
	// On "mock" les classes qui vont être utilisées dans le test
	ComparerCSV comparerCSVTest = Mockito.mock(ComparerCSV.class);
	Donnee donneeTest = Mockito.mock(Donnee.class);
	Donnee_Html donnee_HtmlTest = Mockito.mock(Donnee_Html.class);
	Donnee_Wikitable donnee_WikitableTest = Mockito.mock(Donnee_Wikitable.class);

	/**
	 * Le temps d'execution du parsing du html est superieur a celui du wikitext
	 * La methode doit renvoyer true, le parsing du html a le meilleur temps d'execution
	 */
	@Test
	public void comparaisonTempsExecutionTest1() {
		/* On "mock" les methodes qui vont être executees, on est donc surs que ce qu'elles vont renvoyer,
		excepte la methode à tester.*/
		Mockito.when(donneeTest.getTime()).thenCallRealMethod(); // la methode s'execute normalement
		Mockito.when(donnee_HtmlTest.getTime()).thenReturn((long) 10); // la methode retourne 10
		Mockito.when(donnee_WikitableTest.getTime()).thenReturn((long) 20); // la methode retourne 20
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		// Si tps d'execution html < wikitext, alors on renvoie true.
		Assert.assertTrue(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * Le temps d'execution du parsing du wikitext est superieur a celui du html
	 * La methode doit renvoyer false, le parsing du wikitext a le meilleur temps d'execution
	 */
	@Test
	public void comparaisonTempsExecutionTest2() {
		/* On "mock" les methodes qui vont etre executees, on est donc surs que ce qu'elles vont renvoyer,
		excepte la methode à tester.*/
		Mockito.when(donneeTest.getTime()).thenCallRealMethod(); // la methode s'execute normalement
		Mockito.when(donnee_HtmlTest.getTime()).thenReturn((long) 20); // la methode retourne 20
		Mockito.when(donnee_WikitableTest.getTime()).thenReturn((long) 10); // la methode retourne 10
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		// Si tps d'execution html > wikitext, alors on renvoie false.
		Assert.assertFalse(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * On obtient plus de lignes et de colonnes en parsant le html plutot qu'en parsant le wikitext
	 * La methode doit renvoyer true, le parsing du html renvoie les meilleures donnees
	 */
	@Test
	public void comparaisonDonneesTableau1() {
		Mockito.when(donnee_HtmlTest.getLignesEcrites()).thenReturn(500);
		Mockito.when(donnee_HtmlTest.getColonnesEcrites()).thenReturn(500);
		Mockito.when(donnee_WikitableTest.getLignesEcrites()).thenReturn(200);
		Mockito.when(donnee_WikitableTest.getColonnesEcrites()).thenReturn(200);
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertTrue(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * On obtient plus de lignes et de colonnes en parsant le wikitext plutot qu'en parsant le html
	 * La methode doit renvoyer true, le parsing du wikitext renvoie les meilleures donnees
	 */
	@Test
	public void comparaisonDonneesTableau2() {
		Mockito.when(donnee_HtmlTest.getLignesEcrites()).thenReturn(300);
		Mockito.when(donnee_HtmlTest.getColonnesEcrites()).thenReturn(300);
		Mockito.when(donnee_WikitableTest.getLignesEcrites()).thenReturn(500);
		Mockito.when(donnee_WikitableTest.getColonnesEcrites()).thenReturn(500);
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertFalse(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * On obtient plus de lignes parsant le wikitext mais plus de colonnes en parsant le html
	 * La methode doit renvoyer false, le parsing du wikitext renvoie plus de lignes
	 */
	@Test
	public void comparaisonDonneesTableau3() {
		Mockito.when(donnee_HtmlTest.getLignesEcrites()).thenReturn(300);
		Mockito.when(donnee_HtmlTest.getColonnesEcrites()).thenReturn(500);
		Mockito.when(donnee_WikitableTest.getLignesEcrites()).thenReturn(500);
		Mockito.when(donnee_WikitableTest.getColonnesEcrites()).thenReturn(300);
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertFalse(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * On obtient plus de lignes parsant le html mais plus de colonnes en parsant le wikitext
	 * La methode doit renvoyer false, le parsing du html renvoie plus de lignes
	 */
	@Test
	public void comparaisonDonneesTableau4() {
		Mockito.when(donnee_HtmlTest.getLignesEcrites()).thenReturn(500);
		Mockito.when(donnee_HtmlTest.getColonnesEcrites()).thenReturn(300);
		Mockito.when(donnee_WikitableTest.getLignesEcrites()).thenReturn(300);
		Mockito.when(donnee_WikitableTest.getColonnesEcrites()).thenReturn(500);
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertTrue(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * Le parsing du html s'execute plus rapidement que celui du wikitext
	 * On recupere plus de lignes et de cellules avec le parsing du html plutot qu'avec celui du wikitext
	 * On s'attend a ce que le html soit declare le meilleur format
	 */
	@Test
	public void meilleurFormat1() {
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(true);
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(true);
		Mockito.when(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertEquals(donnee_HtmlTest, comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * Le parsing du wikitext s'execute plus rapidement que celui du html
	 * On recupere plus de lignes et de cellules avec le parsing du wikitext plutot qu'avec celui du html
	 * On s'attend a ce que le wikitext soit declare le meilleur format
	 */
	@Test
	public void meilleurFormat2() {
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(false);
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(false);
		Mockito.when(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertEquals(donnee_WikitableTest, comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest));
	}

	/**
	 * Le parsing du wikitext s'execute plus rapidement que celui du html
	 * On recupere plus de lignes et de cellules avec le parsing du html plutot qu'avec celui du wikitext
	 * On s'attend a ce que la methode renvoie null, il n'y a pas de format meilleur que l'autre
	 */
	@Test
	public void meilleurFormat3() {
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(true);
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(false);
		Mockito.when(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertNull(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	/**
	 * Le parsing du html s'execute plus rapidement que celui du wikitext
	 * On recupere plus de lignes et de cellules avec le parsing du wikitext plutot qu'avec celui du html
	 * On s'attend a ce que la methode renvoie null, il n'y a pas de format meilleur que l'autre
	 */
	@Test
	public void meilleurFormat4() {
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(false);
		Mockito.when(comparerCSVTest.comparaisonTempsExecution(donnee_HtmlTest, donnee_WikitableTest)).thenReturn(true);
		Mockito.when(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		Assert.assertNull(comparerCSVTest.meilleurFormat(donnee_HtmlTest, donnee_WikitableTest));
	}
	
}
