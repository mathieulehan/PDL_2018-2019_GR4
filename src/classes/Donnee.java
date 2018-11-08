package classes;

import java.io.IOException;
import java.net.URL;

abstract class Donnee{
	
	abstract void extraire(String langue, String titre)  throws IOException;
	
	abstract String recupContenu(URL url) throws IOException;
}
