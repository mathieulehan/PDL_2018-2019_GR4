package classes;

import java.io.IOException;
import java.net.URL;

import exceptions.LangueException;

abstract class Donnee{
		
	private long originalTime;
	
	abstract void extraire(String langue, String titre)  throws IOException, LangueException;
	
	abstract String recupContenu(URL url) throws IOException;
	
	abstract boolean pageComporteTableau(String donnee);

	public void start(){
		this.originalTime = System.currentTimeMillis();
	}

	public long getTime(){
		return System.currentTimeMillis() - originalTime;
	}
}