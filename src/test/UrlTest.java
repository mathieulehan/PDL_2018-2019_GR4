package test;

import org.junit.Test;
import classes.Url;
import exceptions.LangueException;
import exceptions.UrlInvalideException;

public class UrlTest {

	@Test(expected = LangueException.class)
	public void detection_langues_non_geree() throws LangueException, UrlInvalideException {
		Url url = new Url("https://fr.wikipedia.org/wiki/Espagne");
		url.validationLangue();
	}
}
