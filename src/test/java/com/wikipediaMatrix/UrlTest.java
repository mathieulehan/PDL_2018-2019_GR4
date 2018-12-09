package com.wikipediaMatrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.*;

public class UrlTest {

	@Test(expected = UrlInvalideException.class)
	public void detection_langues_non_geree() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fy.wikipedia.org/wiki/Espagne"));
		url.estUrlValide();
	}
	
	@Test(expected = UrlInvalideException.class)
	public void page_non_wikipedia() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://www.google.com"));
		url.estUrlValide();
	}

	@Test
	public void titre_inexistant() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://fr.wikipedia.org/wiki/"));
		assertFalse(url.estUrlValide());
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
	
	@Test
	public void titre_langue_incomplet() throws UrlInvalideException, MalformedURLException {
		Url url = new Url(new URL("https://.wikipedia.org/wiki/"));
		assertFalse(url.estUrlValide());
	}
	
	@Test
	public void verification_336_urls_valides() throws UrlInvalideException, IOException   {
		String BASE_WIKIPEDIA_URL = "output/url_file.txt";
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
	public void tester_connexion_336_urls() throws UrlInvalideException, IOException {
		String BASE_WIKIPEDIA_URL = "output/url_file.txt";
		BufferedReader br = new BufferedReader(new FileReader(BASE_WIKIPEDIA_URL));
	    String url;
	    int articleExistant = 0, articleInexistant = 0;
	    while ((url = br.readLine()) != null) {
	    	Url WikiUrl = new Url(new URL(url));
	    	try {
				WikiUrl.testerConnexionHTTP();
		    	articleExistant++;
			} catch (ArticleInexistantException e) {
				// TODO Auto-generated catch block
				articleInexistant++;
			}
	    }
	    br.close();
	    assertEquals(articleExistant, 314);
	    assertEquals(articleInexistant, 22);
	    System.out.println("URLs sans article : " + articleExistant);
	}
}
