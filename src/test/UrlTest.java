package test;

import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import classes.Url;
import exceptions.UrlInvalideException;

public class UrlTest {

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

	@Test(expected = MalformedURLException.class)
	public void detection_titre_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/"));
		url.estTitreValide();
	}

	@Test
	public void titre_url_egal_espagne() throws MalformedURLException, UrlInvalideException  {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		url.estUrlValide();
		assertTrue(url.getTitre().equals("Espagne"));
	}

	@Test(expected = UrlInvalideException.class)
	public void detection_langues_inexistante() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://.wikipedia.org/wiki/Espagne"));
		url.estUrlValide();
	}
	
	@Test(expected = MalformedURLException.class)
	public void detection_titre_langue_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://.wikipedia.org/wiki/"));
		url.estUrlValide();
	}

}
