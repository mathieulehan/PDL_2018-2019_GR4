package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		url.estUrlValide();
	}

	@Test(expected = MalformedURLException.class)
	public void detection_titre_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/"));
		url.estUrlValide();
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
	
	@Test
	public void verification_336_urls() throws UrlInvalideException, IOException   {
		String BASE_WIKIPEDIA_URL = "src/ressources/url_file.txt";
		BufferedReader br = new BufferedReader(new FileReader(BASE_WIKIPEDIA_URL));
	    String url;
	    int nurl = 0;
	    while ((url = br.readLine()) != null) {
	    	Url WikiUrl = new Url(new URL(url));
		    WikiUrl.estUrlValide();
		    nurl++;
	    }
	    br.close();
	    assertEquals(nurl, 336);
	}
	
	@Test
	public void tester_connexion_336_urls() throws UrlInvalideException, IOException   {
		String BASE_WIKIPEDIA_URL = "src/ressources/url_file.txt";
		BufferedReader br = new BufferedReader(new FileReader(BASE_WIKIPEDIA_URL));
	    String url;
	    int nurl = 0;
	    while ((url = br.readLine()) != null) {
	    	Url WikiUrl = new Url(new URL(url));
	    	if (!WikiUrl.testerConnexionHTTP()) {
	    		System.out.println("Erreur connexion wikipedia : " + WikiUrl.getURL());
	    	}
		    nurl++;
	    }
	    br.close();
	    assertEquals(nurl, 336);
	}

}
