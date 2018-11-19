package classes;

import exceptions.FormatsEquivalentsException;

/**
 * Classe permettant d'effectuer diverses comparaison entre les deux methodes de recuperation de table wikipedia
 * @author mathi & thomas
 *
 */
public class ComparerCSV {

	/**
	 * @param html L'objet gerant le html
	 * @param wikitable L'objet gerant le wikitext
	 * @return true si le tps d'execution du parsing du html est inferieur a celui du wikitext
	 * TODO que faire en cas d'egalite parfaite meme si peu probable ?
	 */
	public boolean comparaisonTempsExecution(Donnee_Html html, Donnee_Wikitable wikitable) {
		return (html.getTime() < wikitable.getTime());
	}

	/**
	 * @param html L'objet gerant le html
	 * @param wikitable L'objet gerant le wikitext
	 * @return true si le html renvoie le meilleur tableau
	 * TODO que faire en cas d'egalite parfaite meme si peu problable ? -> renvoyer exception
	 * @throws FormatsEquivalentsException 
	 */
	public boolean comparaisonDonneesTableau(Donnee_Html html, Donnee_Wikitable wikitable) throws FormatsEquivalentsException {
		int lignesHtml = html.getLignesEcrites();
		int colonnesHtml= html.getColonnesEcrites();
		int lignesWikitext= wikitable.getLignesEcrites();
		int colonnesWikitext = wikitable.getColonnesEcrites();
		if((lignesHtml > lignesWikitext) && (colonnesHtml > colonnesWikitext)){
			return true;
		} else if ((lignesHtml < lignesWikitext) && (colonnesHtml < colonnesWikitext)) {
			return false;
		} else if (((lignesHtml > lignesWikitext) && (colonnesHtml < colonnesWikitext)) | ( (lignesHtml < lignesWikitext) && (colonnesHtml > colonnesWikitext))){
			// TODO pour le moment, priorité au html
			return lignesHtml > lignesWikitext; 
		} else { // on a une egalite
			throw new FormatsEquivalentsException("Nombre de lignes et de colonnes égaux");
		}
		// TODO voir pour ajouter des comparaisons autres que lignes + colonnes
	}

	public Donnee meilleurFormat(Donnee_Html html, Donnee_Wikitable wikitable) throws FormatsEquivalentsException {
		if(comparaisonTempsExecution(html, wikitable) && comparaisonDonneesTableau(html, wikitable)){
			return html;
		}else if(!comparaisonTempsExecution(html, wikitable) && !comparaisonDonneesTableau(html, wikitable)) {
			return wikitable;
		}else {
			throw new FormatsEquivalentsException("Pas de meilleur format");
		}
	}

}
