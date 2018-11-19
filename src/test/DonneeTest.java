package test;

import org.mockito.Mockito;

import classes.ComparerCSV;
import classes.Donnee;
import classes.Donnee_Html;
import classes.Donnee_Wikitable;

public class DonneeTest {

	// On "mock" les classes qui vont être utilisées dans le test
	Donnee donneeTest = Mockito.mock(Donnee.class);
	Donnee_Html donnee_HtmlTest = Mockito.mock(Donnee_Html.class);
	Donnee_Wikitable donnee_WikitableTest = Mockito.mock(Donnee_Wikitable.class);

	void recupContenu1() {
		
	}
}
