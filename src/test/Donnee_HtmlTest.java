package test;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Donnee_HtmlTest {
	
	@Test
	void testTempsExec(long nbATest) {
		assertTrue("Temps d'execution de " + nbATest/1000 + " secondes", nbATest < 24000);
	}
	

}
