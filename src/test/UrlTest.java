package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import classes.Url;
import exceptions.UrlInvalideException;

public class UrlTest {

	@Test(expected = UrlInvalideException.class)
	public void detection_langues_non_geree() throws MalformedURLException, UrlInvalideException {
		Url url = new Url(new URL("https://fy.wikipedia.org/wiki/Espagne"));
		url.estLangueValide();
	}
	
	@Test
	public void test_connexion_http_valide() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/Espagne"));
		assertTrue(url.testerConnexionHTTP());
	}
	
	//JE VEUX UrlInvalideException, pas MalformedURLException
	@Test(expected = MalformedURLException.class)
	public void detection_titre_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("//https://fr.wikipedia.org/wiki/"));
		url.estTitreValide();
	}
	
	//JE VEUX UrlInvalideException, pas MalformedURLException
	@Test(expected = MalformedURLException.class)
	public void detection_titre_incomplet2() throws UrlInvalideException, IOException {
		Url url = new Url(new URL("//https://fr.wikipedia.org/wiki/"));
		url.estUrlValide();
	}
	
}
