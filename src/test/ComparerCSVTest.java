package test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import classes.ComparerCSV;
import classes.Donnee_Html;
import classes.Donnee_Wikitable;
import classes.Donnee;

public class ComparerCSVTest {

	/**
	 * Test de la fonction comparaisonTempsExecution de la classe ComparerCSV, via Mockito.
	 */

	// On "mock" les classes qui vont être utilisées dans le test
	ComparerCSV comparerCSVTest = Mockito.mock(ComparerCSV.class);
	Donnee donneeTest = Mockito.mock(Donnee.class);
	Donnee_Html donnee_HtmlTest = Mockito.mock(Donnee_Html.class);
	Donnee_Wikitable donnee_WikitableTest = Mockito.mock(Donnee_Wikitable.class);

	@Test
	// TODO à finir
	public void comparaisonTempsExecutionTest1() {
		/* On "mock" les methodes qui vont être executees, on est donc surs que ce qu'elles vont renvoyer,
		excepte la methode à tester.*/
		Mockito.when(donneeTest.getTime()).thenCallRealMethod(); // la methode s'execute normalement
		Mockito.when(donnee_HtmlTest.getTime()).thenReturn((long) 10); // la methode retourne 10
		Mockito.when(donnee_WikitableTest.getTime()).thenReturn((long) 20); // la methode retourne 20
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		// Si tps d'execution html < wikitext, alors on renvoie true.
		Assert.assertTrue(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
	
	@Test
	public void comparaisonTempsExecutionTest2() {
		/* On "mock" les methodes qui vont etre executees, on est donc surs que ce qu'elles vont renvoyer,
		excepte la methode à tester.*/
		Mockito.when(donneeTest.getTime()).thenCallRealMethod(); // la methode s'execute normalement
		Mockito.when(donnee_HtmlTest.getTime()).thenReturn((long) 20); // la methode retourne 20
		Mockito.when(donnee_WikitableTest.getTime()).thenReturn((long) 10); // la methode retourne 10
		Mockito.when(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest)).thenCallRealMethod();
		// Si tps d'execution html > wikitext, alors on renvoie false.
		Assert.assertFalse(comparerCSVTest.comparaisonDonneesTableau(donnee_HtmlTest, donnee_WikitableTest));
	}
}
