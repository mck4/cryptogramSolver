/** Dictionary.java **/
// Has vocabulary and information on words

package application.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Dictionary {
	// Variables
	final String [] mostFrequentLetters = {"E", "T", "A", "O", "I", "N"};
	final String [] contractionEnds = {"T", "S", "D", "M", "RE", "VE", "LL"}; 

	// Vocabulary
	ArrayList<Word> vocabulary = new ArrayList<Word>();
	ArrayList<String> vocabString = new ArrayList<String>();


	/** Constructor **/
	public Dictionary(ArrayList<Word> vocab) {
		this.vocabulary = vocab;
		getThreeLetterWords();
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

	/** Get String version of vocabulary **/
	public void getThreeLetterWords() {

		// For each wordcc
		for(Word w: this.vocabulary) 
			vocabString.add(w.getWord());

		// Sort
		Collections.sort(vocabString);

	}

	/** GETTERS AND SETTERS **/

	public String[] getMostFrequentLetters() {
		return mostFrequentLetters;
	}

	public String[] getContractionEnds() {
		return contractionEnds;
	}

	public ArrayList<Word> getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(ArrayList<Word> vocabulary) {
		this.vocabulary = vocabulary;
	}

	public ArrayList<String> getVocabString() {
		return vocabString;
	}

	public void setVocabString(ArrayList<String> vocabString) {
		this.vocabString = vocabString;
	}

}

