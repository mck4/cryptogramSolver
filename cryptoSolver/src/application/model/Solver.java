/** Solver.java **/

package application.model;

import java.util.ArrayList;

public class Solver {
	Dictionary dict;     // Solver has a dictionary
	Quote currQ;	     // The current puzzle 
	String currSolution; // Current solution so far (not filled) *Needs to be updated each time*
	int currDifficulty;  // Current difficulty
	
	// counters
	int checkforFilledInOneCount;
	
	// Copies from currQ
	ArrayList<String> cryptoWords = new ArrayList<String>();  // Cryptotext beneath the quote
	ArrayList<String> hiddenTokens = new ArrayList<String>(); // Half-filled Tokens of words in puzzle *Needs to be updated each time*
	
	/** Constructor **/
	public Solver(Dictionary d, Quote q, int diff) {
		
		this.dict = d;
		this.currQ = q;
		this.cryptoWords = q.getCryptoWords();
		this.checkforFilledInOneCount = 0;
		
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
		
	}
	
	/** Check for one-letter words
	public void checkforOneLetterWords(int difficulty) {
		
		for(Word w: currQ.getWords()) {
			if (w.getWord().length() == 1) {
				char temp = w.getWord().charAt(0);
				char crypto = currQ.cryptoOf(temp);
				String trying = Quote.updatePuzzle('A', crypto, currQ, difficulty);
				System.out.println(trying);
			}
		}
	} **/
	
	/** Find letters that are mostly filled in (ie all but 1 spaces) 
	 * Compare with dictionary, find one that matches all chars but one**/
	public int checkforFilledInOne() {
		boolean foundAMatch = false;
		System.out.println(hiddenTokens);
		// For each token 
		for(String s: this.hiddenTokens) {
			if(foundAMatch)
				break;
			int matchesFound = 0; // Should be one
			String matchStr = "";
			// Convert to char array
			char [] charas = s.toCharArray();
			int count = 0;
			
			// Count empty spaces
			for(char c: charas){
				if(c == '_')
					count++;
			}
			// This word is mostly filled out so let's narrow it down
			if( count == 1 ) {
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
							matchStr = w.getWord();
							matchesFound++;
							//System.out.println(matchesFound);
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
				
				//System.out.println("Match found! word: "+ matchStr + "; str: " + s);
				for(int i = 0; i < s.length(); i++) {
					foundAMatch = true;
					if(s.charAt(i) == '_') {
						posOfChar = i;
						ans = matchStr.charAt(i);
						crypto = this.cryptoWords.get(posOfWord).charAt(posOfChar);
					}
				}
				//System.out.println(ans + " " + crypto + " " );
				
				// Update current solution
				this.currSolution = Quote.updatePuzzle(ans, crypto, currQ, currSolution);
				checkforFilledInOneCount++;
				// Update hiddenTokens
				updateHiddenTokens();
				System.out.println(this.currSolution);
			}	
		}

		return this.checkforFilledInOneCount;
	}
	
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
