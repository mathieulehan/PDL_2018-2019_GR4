package com.wikipediaMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.*;

public class WikiExtractMainTest {	

	int nbTablesHtml = 0;
	int nbLignesHtml = 0;
	int nbColonnesHtml = 0;
	int nbTablesWikitext = 0;
	int nbLignesWikitext = 0;
	int nbColonnesWikitext = 0;

	/**
	 * Methode demarrant le parsing en csv des wikitables de 336 pages wikipedia, a partir du html et du wikitext
	 * @throws ExtractionInvalideException
	 * @throws UrlInvalideException
	 * @throws ConversionInvalideException
	 * @throws ArticleInexistantException
	 * @throws IOException
	 * @throws ResultatEstNullException 
	 */
	@Test
	public void lancerExtraction() throws ExtractionInvalideException, UrlInvalideException, ConversionInvalideException, ArticleInexistantException, IOException, ResultatEstNullException {
		String BASE_WIKIPEDIA_URL = "src/output/url_file.txt";
		BufferedReader br = new BufferedReader(new FileReader(BASE_WIKIPEDIA_URL));
		String url;
		int urlActuelle = 1;
		long debutExe = System.currentTimeMillis();
		while ((url = br.readLine()) != null) {
			boolean hasPage = true;
			Url WikiUrl = new Url(new URL(url));
			if(WikiUrl.estUrlValide()) {
				Donnee_Html testHtml = new Donnee_Html();
				Donnee_Wikitable testWikitable = new Donnee_Wikitable();
				ComparerCSV comparerCsv = new ComparerCSV(testHtml, testWikitable);
				//lancerExtractionHtml(testHtml, WikiUrl, urlActuelle, hasPage);
				lancerExtractionWikitext(testWikitable, WikiUrl, urlActuelle, hasPage);
				comparerCsv.informationsExtraction();
				nbTablesHtml += comparerCsv.getTablesHtml();
				nbColonnesHtml += comparerCsv.getColonnesHtml();
				nbLignesHtml += comparerCsv.getLignesHtml();
				nbTablesWikitext += comparerCsv.getTablesWikitable();
				nbColonnesWikitext += comparerCsv.getColonnesWikitable();
				nbLignesWikitext += comparerCsv.getLignesWikitable();
			}
		}
		br.close();
		System.out.println("Temps d'execution : " + (System.currentTimeMillis() - debutExe)/1000 + " secondes");
		System.out.println("-----------STATISTIQUES-----------");
		System.out.println("- HTML");
		System.out.println("Nombre de tableaux parsés: " + nbTablesHtml + ", lignes parsées : " + nbLignesHtml + ", colonnes parsées : " + nbColonnesHtml);
		System.out.println("- WIKITEXT");
		System.out.println("Nombre de tableaux parsés: " + nbTablesWikitext + ", lignes parsées : " + nbLignesWikitext + ", colonnes parsées : " + nbColonnesWikitext);
	}

	private void lancerExtractionHtml(Donnee_Html testHtml, Url page, int urlActuelle, boolean hasPage) throws UrlInvalideException, ExtractionInvalideException, ConversionInvalideException, ArticleInexistantException, IOException, ResultatEstNullException {
		String langue = page.getLangue();
		String titre = page.getTitre();
		/* On recupere le nombre calcule de lignes et de colonnes de tous
		les tableaux de l'url*/
		URL url = new URL("https://"+langue+".wikipedia.org/wiki/"+titre+"?action=render");
		System.out.println(titre);
		try {
			testHtml.setHtml(testHtml.recupContenu(url));
		} catch (ExtractionInvalideException erreurExtraction) {
			System.out.println("ERREUR : " + erreurExtraction.toString());
			hasPage = false;
		}
		if(hasPage)testHtml.extraire(page);
		System.out.print(urlActuelle + " URLs parsées !  ");
		urlActuelle++;
	}

	private void lancerExtractionWikitext(Donnee_Wikitable testWikitable, Url page, int urlActuelle, boolean hasPage) throws MalformedURLException, UrlInvalideException, ExtractionInvalideException, IOException {
		testWikitable.extraire(page);
	}

}
