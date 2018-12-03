/** Solver.java **/
// Solves the puzzles

package application.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class Solver {
	Dictionary dict;     // Solver has a dictionary
	Quote currQ;	     // The current puzzle 
	String currSolution; // Current solution so far (not filled) *Needs to be updated each time*
	String currCrypto;	 // crypto version of quote
	int currDifficulty;  // Current difficulty

	boolean solved = false;	// Has it been solved?

	ArrayList<String> charsFound = new ArrayList<String>(); // Characters found *Needs to be updated each time*

	// counters
	int checkforFilledInOneCount = 0;
	int checkforFilledInAndNeighborsCount = 0;
	int checkforContractionsCount = 0;
	int checkforTwoAndSearchCount = 0;
	
	// Copies from currQ
	ArrayList<String> cryptoWords = new ArrayList<String>();  // Cryptotext beneath the quote
	ArrayList<String> hiddenTokens = new ArrayList<String>(); // Half-filled Tokens of words in puzzle *Needs to be updated each time*

	/** Constructor **/
	public Solver(Dictionary d, Quote q, int diff) {

		this.dict = d;
		this.currQ = q;
		this.cryptoWords = q.getCryptoWords();
		this.currCrypto = q.getCryptoQuote();

		if(diff == 1) {
			this.currDifficulty = 1;
			this.currSolution = q.getqEasy();
			this.hiddenTokens = (ArrayList<String>) q.getEasyWordsHidden().clone();
		}
		else if(diff == 2) {
			this.currDifficulty = 2;
			this.currSolution = q.getqMed();
			this.hiddenTokens = (ArrayList<String>) q.getMedWordsHidden().clone();
		}
		else { //diff == 3
			this.currDifficulty = 3;
			this.currSolution = q.getqHard();
			this.hiddenTokens = (ArrayList<String>) q.getHardWordsHidden().clone();
		}

		collectCharsFound(this.currSolution);

	}

	/** Updates the list of characters found in solution **/
	public void collectCharsFound(String current){

		for(char chara: current.toCharArray()){
			if(Character.isLetter(chara)){
				if(!charsFound.contains(String.valueOf(chara)))
					charsFound.add(String.valueOf(chara));
			}
		}
	}

	/** Updates HiddenTokens array **/
	public void updateHiddenTokens() {
		// Deal directly with Strings first
		String [] tokens = this.currSolution.split(" ");

		// Get rid of unnecessary punctuation
		for(int i = 0; i < tokens.length; i++) {
			// Remove punctuation
			tokens[i] = tokens[i].replaceAll("[,?!.;\"]", "");
			// Add
			this.hiddenTokens.set(i, tokens[i]);
		}
	}

	/** Is the puzzle solved? **/
	public boolean isSolved(){
		if(this.currSolution.equals(currQ.getQuote()))
			solved = true;

		return solved;
	}

	/** 
	 * Find letters that are mostly filled in (ie all but 1 spaces) 
	 * Compare with dictionary, find one that matches all chars but one
	 * **/
	public int checkforFilledInOne() {
		// Match found flag
		boolean foundAMatch = false;

		// For each token 
		for(String s: this.hiddenTokens) {			
			// Break when the first match is found
			if(foundAMatch)
				break;

			// Keep track of # of matches found; here it should just be one
			int matchesFound = 0; 

			// Our matched string will go here
			String matchStr = "";

			// Convert token to char array
			char [] charas = s.toCharArray();

			// Count empty spaces in token
			int count = 0;
			for(char c: charas) {
				if(c == '_')
					count++;
			}

			// This word is mostly filled out so let's narrow it down
			if( count == 1 ) {

				// Compare current word to our dictionary
				for(Word w: this.dict.getVocabulary()) {

					// If this word is the same length...
					if(s.length() == w.getWordLen()) {

						// A match is found if numToMatch and matchCount are the same
						int numToMatch = s.length() - 1; 
						int matchCount = 0;

						// Look for words that might be a match
						for(int i = 0; i < s.length(); i++) {
							if(s.charAt(i) == w.getWord().charAt(i)){
								//System.out.println("s: " + s.charAt(i) + "; w: " + w.getWord().charAt(i));
								matchCount++;
							}
						}

						// An almost perfect match is found
						if(numToMatch == matchCount) {
							matchStr = w.getWord(); // Get the matched word
							matchesFound++;		    // Increment for each possible match found
						}							
					}
				}
			}
			// Here we'll just deal with cases where only one match is found
			if(matchesFound == 1) {

				// Characters go here
				char ans = ' ';
				char crypto = ' ';

				// Indexes go here
				int posOfChar = 0;
				int posOfWord = this.hiddenTokens.indexOf(s);

				// Find the blank
				for(int i = 0; i < s.length(); i++) {
					foundAMatch = true;
					if(s.charAt(i) == '_') {
						posOfChar = i;												  // Get position in word
						crypto = this.cryptoWords.get(posOfWord).charAt(posOfChar);   // Get corresponding crypto
						ans = matchStr.charAt(i);	// Get letter answer
					}
				}

				/* UPDATES WHEN ANSWER FOUND */

				// Update current solution
				this.currSolution = Quote.updatePuzzle(ans, crypto, currQ, currSolution);
				// Update hiddenTokens
				updateHiddenTokens();
				// Update chars found
				collectCharsFound(this.currSolution);

				checkforFilledInOneCount++; // Increment

				System.out.println("");
				System.out.println(this.currSolution);
				System.out.println(this.currCrypto);
			}	
		}

		return this.checkforFilledInOneCount;
	}

	/** 
	 * Find letters that are mostly filled in (ie all but 1 spaces) 
	 * Compare with dictionary, find one that matches all chars but one
	 * Accepts multiple matches and narrows it down by checking neighbors 
	 * **/
	public int checkforFilledInOneAndNeighbors() {
		// Match found flag
		boolean foundAMatch = false;

		// For each token 
		for(String s: this.hiddenTokens) {			
			// Break when the first match is found
			if(foundAMatch)
				break;

			// Our matched strings will go here **USES AN ARRAYLIST**
			ArrayList<String> matchStrs = new ArrayList<String>();
			ArrayList<String> matchNarrow = new ArrayList<String>();

			// Convert token to char array
			char [] charas = s.toCharArray();

			// Count empty spaces in token
			int count = 0;
			for(char c: charas) {
				if(c == '_')
					count++;
			}

			// This word is mostly filled out so let's narrow it down
			if( count == 1 ) {

				// Compare current word to our dictionary
				for(Word w: this.dict.getVocabulary()) {

					// If this word is the same length...
					if(s.length() == w.getWordLen()) {

						// A match is found if numToMatch and matchCount are the same
						int numToMatch = s.length() - 1; 
						int matchCount = 0;

						// Look for words that might be a match
						for(int i = 0; i < s.length(); i++) {
							if(s.charAt(i) == w.getWord().charAt(i)){

								matchCount++;
							}
						}

						// An almost perfect match is found
						if(numToMatch == matchCount) {
							matchStrs.add(w.getWord()); // Get the matched word, add to AL
						}							
					}
				}
			}
			if(!matchStrs.isEmpty()) {
				// Characters go here
				char ans = ' ';
				char crypto = ' ';

				// Indexes go here
				int posOfChar = 0;
				int posOfWord = this.hiddenTokens.indexOf(s);

				// Find the blank
				for(int i = 0; i < s.length(); i++) {
					foundAMatch = true;
					if(s.charAt(i) == '_') {
						posOfChar = i;												  // Get position in word
						crypto = this.cryptoWords.get(posOfWord).charAt(posOfChar);   // Get corresponding crypto
					}
				}

				// Iterate through matched Strings and narrow down
				for(String m: matchStrs) {
					// Get possible ans character from current matching word
					ans = m.charAt(posOfChar);

					// If the current character is in the list of characters found...
					if(charsFound.contains(String.valueOf(ans)))
						continue;			// This word is not a possible solution since the possible ans is already in the quote
					else 
						matchNarrow.add(m);	// If not, this is a possible candidate
				}

				// There is only one possible solution!
				if(matchNarrow.size() == 1) {
					// Get letter answer
					ans = matchNarrow.get(0).charAt(posOfChar);

					/* UPDATES WHEN ANSWER FOUND */

					// Update current solution
					this.currSolution = Quote.updatePuzzle(ans, crypto, currQ, currSolution);
					// Update hiddenTokens
					updateHiddenTokens();
					// Update chars found
					collectCharsFound(this.currSolution);

					checkforFilledInAndNeighborsCount++;

					System.out.println("");
					System.out.println(this.currSolution);
					System.out.println(this.currCrypto);

				}
			}

		}

		return this.checkforFilledInAndNeighborsCount;
	}

	/** CHECK FOR CONTRACTIONS **/
	public int checkforContractions() {
		// Match found flag
		boolean foundAMatch = false;
		int index = 0;

		// For each token 
		for(String s: this.hiddenTokens) {			
			// Break when the first match is found
			if(foundAMatch)
				break;

			boolean contractionFound = false;

			// Convert token to char array
			char [] charas = s.toCharArray();

			// Count empty spaces in token
			int count = 0;
			for(char c: charas) {
				if(c == '_')
					count++;
				// If contraction found and there's an empty space in the word, break
				if(c == '\'' && count > 0) {
					contractionFound = true;
					break;
				}
			}

			// This word is mostly filled out so let's narrow it down
			if(contractionFound) {

				// Compare current word to our dictionary
				for(Word w: this.dict.getVocabulary()) {

					// If this word is the same length...
					if(s.length() == w.getWordLen() && w.isAContraction) {
						foundAMatch = true;


						// Look for words that might be a match
						for(int i = 0; i < s.length(); i++) {
							if(Character.isLetter(s.charAt(i))){
								if(s.charAt(i) != w.getWord().charAt(i)) {
									foundAMatch = false;
									break;
								}
							}
						}

						// An almost perfect match is found
						if(foundAMatch) {
							char ans = ' ';
							char crypto = ' ';
							for(int i = 0; i< s.length(); i++) {
								if(s.charAt(i) == '_') {

									/* UPDATES WHEN ANSWER FOUND */

									ans = w.getWord().charAt(i);
									crypto = this.cryptoWords.get(index).charAt(i);

									// Update current solution
									this.currSolution = Quote.updatePuzzle(ans, crypto, currQ, currSolution);
								}
							}

							/* UPDATES WHEN ANSWER FOUND */

							// Update hiddenTokens
							updateHiddenTokens();
							// Update chars found
							collectCharsFound(this.currSolution);

							checkforContractionsCount++;

							System.out.println("");
							System.out.println(this.currSolution);
							System.out.println(this.currCrypto);

						}							
					}

				}
			}
			index ++;
		}

		return this.checkforContractionsCount;
	}

	/**
	 *  Searches for words with two empty spaces and then checks for possible solutions. 
	 * It narrows down the solution by checking the effect the solutions of those
	 * have on the other words. If it produces results that are not likely to be words; the
	 * solution is rejected
	 **/
	public int checkforTwoAndSearch() {
		// Match found flag
		boolean foundAMatch = false;
		int hiddenTokensIndx = 0;

		// For each token 
		for(String s: this.hiddenTokens) {			
			// Break when the first match is found
			if(foundAMatch)
				break;

			// Convert token to char array
			char [] charas = s.toCharArray();

			// Count empty spaces in token
			int count = 0;
			for(char c: charas) {
				if(c == '_')
					count++;
			}

			// Select words with two empty spaces
			if(count == 2) {

				// Will hold the indexes for the two empty spots
				ArrayList<Integer> indexes = new ArrayList<Integer>();

				// Get position of empty spaces
				count = 0;
				for(int i = 0; i <s.length(); i++) {
					if(charas[i] == '_') {
						indexes.add(i);
					}
				}

				// Compare current word to our dictionary
				for(Word w: this.dict.getVocabulary()) {

					// Only compare with words of the same length
					if(s.length() == w.getWordLen()) {

						// Assume the word is a match until proven false
						boolean possibleMatch = true;

						// Look for words that might be a match
						for(int i = 0; i < s.length(); i++) {
							// If the character of the current puzzle token (with blanks) is a letter, compare
							if(Character.isLetter(s.charAt(i))) {

								// If each char that's a letter matches, a match is found; otherwise there's no match
								if(s.charAt(i) != w.getWord().charAt(i)) {
									possibleMatch = false;
									break;
								}
							}
						}	

						// Found a potential match
						if(possibleMatch) {

							// First position charas
							char crypto1 = this.cryptoWords.get(hiddenTokensIndx).charAt(indexes.get(0));
							char alphaPos1 = w.getWord().charAt(indexes.get(0));

							// Second position charas
							char crypto2 = this.cryptoWords.get(hiddenTokensIndx).charAt(indexes.get(1));
							char alphaPos2 = w.getWord().charAt(indexes.get(1));

							// Builds the string with the two possible charas
							String temp = Quote.updatePuzzle(alphaPos1, crypto1, currQ, currSolution);						
							temp = Quote.updatePuzzle(alphaPos2, crypto2, currQ, temp);

							// Assumes string generated (temp) is not a possible answer until proven
							boolean isPossible= false;

							// Tokenize possible string
							ArrayList<String> tempStrings = new ArrayList<String>();
							Quote.collectTokens(temp, tempStrings);
							int numofWordinQuote = tempStrings.size();
							int numofWordinQuoteMatches = 0;

							// Check if each word is actually a word (ie in our dictionary)
							for(String token: tempStrings) {
								int dictIndx = 0;
								for(Word d1: this.dict.vocabulary) {


									// First find words of same length	
									if(token.length() == d1.getWordLen()) {
										boolean possibleWord = true;

										int charIndx = 0;
										char [] tokenChArr = token.toCharArray();

										// Compare character by character, excluding anything but letters
										for(int i = 0; i < tokenChArr.length; i++) {

											// Excluding anything but letters
											if(Character.isLetter(tokenChArr[i])) {
												if(tokenChArr[i] != d1.getWord().charAt(i)) {
													possibleWord = false;
													break;  // Stop comparing at first mismatch
												}
											}
										}

										// If this is still true
										if(possibleWord) 
											numofWordinQuoteMatches++;
									}
								}
							}

							// If each word has some match in the dictionary....
							if(numofWordinQuoteMatches == tempStrings.size()) {
								foundAMatch = true;
								/* UPDATES WHEN ANSWER FOUND */

								// Update current solution
								this.currSolution = temp;

								// Update hiddenTokens
								updateHiddenTokens();

								// Update chars found
								collectCharsFound(this.currSolution);

								checkforTwoAndSearchCount++;

								System.out.println("");
								System.out.println(this.currSolution);
								System.out.println(this.currCrypto);

							}
						}
					}
				}
			}
			hiddenTokensIndx++;
		}
		return this.checkforTwoAndSearchCount;
	}

	/** GETTERS & SETTERS **/

	public String toString(){
		return this.currSolution;
	}

	public Dictionary getDict() {
		return dict;
	}

	public Quote getCurrQ() {
		return currQ;
	}

	public void setDict(Dictionary d) {
		this.dict = d;
	}

	public void setCurrent(Quote q) {
		this.currQ = q;
	}

	public String getCurrSolution() {
		return currSolution;
	}

	public int getCurrDifficulty() {
		return currDifficulty;
	}

	public ArrayList<String> getCryptoWords() {
		return cryptoWords;
	}

	public ArrayList<String> getHiddenTokens() {
		return hiddenTokens;
	}

	public void setCurrQ(Quote currQ) {
		this.currQ = currQ;
	}

	public void setCurrSolution(String currSolution) {
		this.currSolution = currSolution;
	}

	public void setCurrDifficulty(int currDifficulty) {
		this.currDifficulty = currDifficulty;
	}

	public void setCryptoWords(ArrayList<String> cryptoWords) {
		this.cryptoWords = cryptoWords;
	}

	public void setHiddenTokens(ArrayList<String> hiddenTokens) {
		this.hiddenTokens = hiddenTokens;
	}



}
