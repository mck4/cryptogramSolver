package application.model;

import java.util.ArrayList;
import java.util.Collections;

public class Dictionary {
	// Hard-coding some rules any human would know
	final String [] oneLetterWords = {"A", "I"};
	final String [] twoLetterWords = {"AM", "AN", "AS", "AT", "BE", "BY", "DO", "GO", 
									  "HE", "IF", "IN", "IS", "IT", "ME", "MY", "NO", 
									  "OF", "ON", "OR", "SO", "TO", "UP", "US", "WE"};
	final String [] mostFrequentLetters = {"E", "T", "A", "O", "I", "N"};
	final String [] contractionEnds = {"T", "S", "D", "M", "RE", "VE", "LL"};
	 
	ArrayList<Word> vocabulary = new ArrayList<Word>();
	
	public Dictionary(ArrayList<Word> vocab) {
		this.vocabulary = vocab;
		
	}
	
	/** Builds a vocabulary using the words already existing inside the quotes **/
	public static ArrayList<Word> getVocabulary(ArrayList<Quote> quotes) {
		// Deal directly with Strings first
		ArrayList<String> strV = new ArrayList<String>();
		
		// For each quote
		for(Quote q: quotes) {
			// Split the quotes into individual words
			String [] words = q.getQuote().split(" ");
					
			// Get rid of unnecessary punctuation
			for(int i = 0; i < words.length; i++) {
				words[i] = words[i].replaceAll("[,?!.;\"]", "");
				// Only add the word if it's unique 
				if(!strV.contains(words[i]))
					strV.add(words[i]);
			}
		}
		
		Collections.sort(strV); // Sort
		ArrayList<Word> vocab = new ArrayList<Word>(strV.size()); // Create the AL to return
		
		// Build Word AL
		for(String w: strV){
			Word word = new Word(w);
			vocab.add(word);
		}
		
		return vocab;
	}
	

}

