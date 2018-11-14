package application.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class PuzzleBox {
	ArrayList<Quote> quotePuzzles = new ArrayList<Quote>();

	public PuzzleBox(String filename) {
		parseFile(filename);
	}

	public void parseFile(String filename){
		try {
			// Open the file
			Scanner infile = new Scanner(new FileReader("quotes/new_quotes.txt"));

			while (infile.hasNext()) {
				String line = infile.nextLine();

				String [] tokens = line.split("\\|");

				Quote newQuote = new Quote(tokens[0].toUpperCase(), tokens[1]);
				quotePuzzles.add(newQuote);
			}
		}
		catch (Exception e){
			System.out.println("Oops");
		}

	}
}
