package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import classes.Donnee_Html;

class ExtractWikitableTest {

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
}
