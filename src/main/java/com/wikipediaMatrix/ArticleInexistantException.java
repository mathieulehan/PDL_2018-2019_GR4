package main.java.com.wikipediaMatrix; 
 
/**
 * 
 * @author Groupe 4
 *
 */
public class ArticleInexistantException extends Exception { 
 
	String message; 
	 
	public ArticleInexistantException(String message) { 
		super(); 
		this.message = message; 
	} 
} 
