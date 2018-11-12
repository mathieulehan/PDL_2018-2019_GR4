package classes;

import java.io.IOException;
import java.net.URL;

import exceptions.LangueException;

abstract class Donnee{
	
	abstract void extraire(String langue, String titre)  throws IOException, LangueException;
	
	abstract String recupContenu(URL url) throws IOException;
}
