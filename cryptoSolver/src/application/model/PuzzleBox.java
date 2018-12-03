/** PuzzleBox.java
 * 
 * This parses the quote's textfile, creating the actual
 * list of possible puzzles.
 * 
 *  **/


package application.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class PuzzleBox {
	ArrayList<Quote> quotePuzzles = new ArrayList<Quote>();
	
	/** Constructor **/
	public PuzzleBox(String filename) {
		parseFile(filename);
	}

	/** Parse a file and get the quotes **/
	public void parseFile(String filename){
		try {
			// Open the file
			Scanner infile = new Scanner(new FileReader("quotes/new_quotes.txt"));

			while (infile.hasNext()) {
				// Parse
				String line = infile.nextLine();
				String [] tokens = line.split("\\|");	// Quotes are delimited with the '|' character
				
				// Create new Quote
				Quote newQuote = new Quote(tokens[0].toUpperCase(), tokens[1]);
				quotePuzzles.add(newQuote);
			}
		}
		catch (Exception e){
			System.out.println("Oops");
		}

	}
	
	/** GETTERS & SETTERS **/
	
	public ArrayList<Quote> getQuotePuzzles(){
		return this.quotePuzzles;
	}

	public void setQuotePuzzles(ArrayList<Quote> quotePuzzles) {
		this.quotePuzzles = quotePuzzles;
	}
}
