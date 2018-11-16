package test;

import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import classes.Url;
import exceptions.UrlInvalideException;

public class UrlTest {
	
	/**
	 * PROBLEME : Comparaison de 2 string invalide
	 * Ce test renvoie FALSE lorsque url.getTitre() == "Espagne" (or c'est TRUE la bonne reponse)
	 * Essaie avec "toString()", "intern()"et differentes assertions -> FALSE à chaque fois
	 */
	@Test
	public void titre_url_egal_espagne() throws MalformedURLException  {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		assertTrue(url.getTitre().equals("Espagne"));
	}
	
	/*
	@Test
	public void test_connexion_http_valide() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		assertTrue(url.testerConnexionHTTP());
	}
	*/
	
	@Test(expected = UrlInvalideException.class)
	public void detection_langues_non_geree() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fy.wikipedia.org/wiki/Espagne"));
		url.estLangueValide();
	}
	
	@Test(expected = UrlInvalideException.class)
	public void detection_langues_non_geree2() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://.wikipedia.org/wiki/Espagne"));
		url.estLangueValide();
	}
	
	@Test(expected = MalformedURLException.class)
	public void detection_titre_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/"));
		url.estTitreValide();
	}
	
	@Test(expected = MalformedURLException.class)
	public void detection_titre_langue_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://.wikipedia.org/wiki/"));
		url.estUrlValide();
	}
	
}
