/** Solver.java **/

package application.model;

import java.util.ArrayList;

public class Solver {
	Dictionary dict;     // Solver has a dictionary
	Quote currQ;	     // The current puzzle 
	String currSolution; // Current solution so far (not filled) *Needs to be updated each time*
	int currDifficulty;  // Current difficulty
	ArrayList<String> charsFound = new ArrayList<String>();
	
	// counters
	int checkforFilledInOneCount;
	int checkforFilledInAndNeighborsCount;
	
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
		
		collectCharsFound(this.currSolution);
		
	}
	
	public void collectCharsFound(String current){
		
		for(char chara: current.toCharArray()){
			if(Character.isLetter(chara)){
				if(!charsFound.contains(String.valueOf(chara)))
					charsFound.add(String.valueOf(chara));
			}
		}
		
	}
	
	/** Find letters that are mostly filled in (ie all but 1 spaces) 
	 * Compare with dictionary, find one that matches all chars but one**/
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
				
				System.out.println(this.currSolution);
			}	
		}

		return this.checkforFilledInOneCount;
	}
	
	/** Find letters that are mostly filled in (ie all but 1 spaces) 
	 * Compare with dictionary, find one that matches all chars but one
	 * Accepts multiple matches and narrows it down by checking neighbors **/
	public int checkforFilledInOneAndNeighbors() {
		// Match found flag
		boolean foundAMatch = false;
		
		// For each token 
		for(String s: this.hiddenTokens) {			
			// Break when the first match is found
			if(foundAMatch)
				break;
			
			// Keep track of # of matches found; here it should just be one
			int matchesFound = 0; 
			
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
								//System.out.println("s: " + s.charAt(i) + "; w: " + w.getWord().charAt(i));
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
				
				//System.out.println(matchesFound);
				//System.out.println(matchStrs);
				//System.out.println(s);
				//System.out.println(charsFound);
				
				
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
				
				System.out.println(matchNarrow);
				
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
				}
			}
			
		}

		return this.checkforFilledInAndNeighborsCount;
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
