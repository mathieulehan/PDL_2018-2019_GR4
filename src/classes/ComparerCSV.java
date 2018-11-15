package classes;
/**
 * Classe permettant d'effectuer diverses comparaison entre les deux méthodes de récupération de table wikipedia
 * @author mathi
 *
 */
public class ComparerCSV {

	public boolean comparaisonTempsExecution(Donnee_Html html, Donnee_Wikitable wikitable) {
		return html.getTime() < wikitable.getTime();
	}
	
	public boolean comparaisonDonneesTableau(Donnee_Html html, Donnee_Wikitable wikitable) {
		return false;
	}
	
	public Donnee meilleurFormat(Donnee_Html html, Donnee_Wikitable wikitable) {
		if(comparaisonTempsExecution(html, wikitable) && comparaisonDonneesTableau(html, wikitable)){
			return html;
		}else if(!comparaisonTempsExecution(html, wikitable) && !comparaisonDonneesTableau(html, wikitable)) {
			return wikitable;
		}else {
			return null;
		}
	}
	
}
