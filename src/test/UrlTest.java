package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import classes.Url;
import exceptions.UrlInvalideException;

public class UrlTest {
	
	@Test
	public void titre_url_egal_espagne() throws IOException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		assertEquals("Espagne", url.getTitre());
	}
	
	@Test
	public void test_connexion_http_valide() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		assertTrue(url.testerConnexionHTTP());
	}
	
	@Test(expected = UrlInvalideException.class)
	public void detection_langues_non_geree() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("https://fy.wikipedia.org/wiki/Espagne"));
		url.estLangueValide();
	}
	
	@Test(expected = UrlInvalideException.class)
	public void detection_titre_incomplet() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("//https://fr.wikipedia.org/wiki/"));
		url.estTitreValide();
	}
	
	@Test(expected = UrlInvalideException.class)
	public void detection_titre_langue_incomplet() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("//https://.wikipedia.org/wiki/"));
		url.estUrlValide();
	}
	
}
