package test;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

public class Donnee_WikitableTest {
	
	@Test
	void testPageNonExistante() {
		/*ExtractWikitable testExtractPageNonExistante = new ExtractWikitable();
		assertThrows(IOException.class,() -> {
			testExtractPageNonExistante.extractWikiTable("en", "render", "erreurPage");
	    });*/
	}
	
	@Test
	void erreurLangue() {
		/*ExtractWikitable testErreurLangue = new ExtractWikitable();
		assertThrows(UnknownHostException.class,() -> {
			testErreurLangue.extractWikiTable("erreurLangue", "render", "Wikipedia:Unusual_articles/Places_and_infrastructure");
	    });*/
		
	}
	
	/**
	 * Renvoie un message si le temps d execution depasse un temps maximal
	 * @param nbATest
	 */
	@Test
	void testTempsExec(long nbATest) {
		assertTrue("Temps d'execution de " + nbATest/1000 + " secondes", nbATest < 24000);
	}
	
	/**
	 * Compare le nombre de tableaux recuperes en HTML et en Wikitext
	 * @param nbATester
	 * @param nbRef
	 */
	@Test
	void testNbTableaux(int nbATester, int nbRef) {
		int diff = nbRef - nbATester;
		assertTrue("Nombre de tableaux different : " + diff, diff == 0);
	}
	
	/**
	 * Renvoie le nombre de cellule comportant des differences entre recuperation HTML et Wikitext dans celles testees
	 * @param cellulesATest
	 * @param cellulesRef
	 */
	@Test
	void testCellules(String[] cellulesATest, String[] cellulesRef) {
		int diff = 0;
		for (int i = 0; i < cellulesRef.length; i++) {
			if(cellulesATest[i] != cellulesRef[i]) {
				diff++;
			}
		}
		assertTrue("Nombre de cellules differentes", diff == 0);
	}
	
	

}
