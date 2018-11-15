package classes;

/**
 * Classe permettant d'effectuer diverses comparaison entre les deux methodes de recuperation de table wikipedia
 * @author mathi
 *
 */
public class ComparerCSV {

	public boolean comparaisonTempsExecution(Donnee_Html html, Donnee_Wikitable wikitable) {
		return html.getTime() < wikitable.getTime();
	}
	
	/**
	 * 
	 * @param html L'objet gerant le html
	 * @param wikitable L'objet gerant le wikitext
	 * @return true si le html renvoie le meilleur tableau
	 */
	public boolean comparaisonDonneesTableau(Donnee_Html html, Donnee_Wikitable wikitable) {
		int lignesHtml = html.getLignesEcrites();
		int colonnesHtml= html.getColonnesEcrites();
		int lignesWikitext= wikitable.getLignesEcrites();
		int colonnesWikitext = wikitable.getColonnesEcrites();
		// TODO voir pour ajouter des comparaisons autres que lignes + colonnes
		return lignesHtml > lignesWikitext & colonnesHtml > colonnesWikitext;
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
