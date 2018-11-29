/** Quote.java **/
// Has info on each quote puzzle

package application.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;


public class Quote {
	String quote;	    // Regular, unaltered quote
	String cryptoQuote; // Encrypted quote
	String author;		// Author
	int quoteLen;		// length of quote in chars
	
	// Easy
	String qEasy;
	ArrayList<Character> easyHidden = new ArrayList<Character>();	// Hidden chars
	ArrayList<String> easyWordsHidden = new ArrayList<String>();	// tokens of Crypto words
	// Medium
	String qMed;
	ArrayList<Character> medHidden = new ArrayList<Character>();	// Hidden chars
	ArrayList<String> medWordsHidden = new ArrayList<String>();		// tokens of Crypto words
	// Hard
	String qHard;
	ArrayList<Character> hardHidden = new ArrayList<Character>();	// Hidden chars
	ArrayList<String> hardWordsHidden = new ArrayList<String>();	// tokens of Crypto words
	
	// Letter to its frequency; sorted by least freq
	Map<Character, Integer> charFreq = new LinkedHashMap<Character, Integer>(); 
	
	// Key; first is sorted alphabetically
	Map<Character, Character> alphaToCrypto = new LinkedHashMap<Character, Character>(); 
	Map<Character, Character> cryptoToAlpha = new LinkedHashMap<Character, Character>(); 
	
	// The individual words in the quote
	ArrayList<Word> words = new ArrayList<Word>();
	ArrayList<String> cryptoWords = new ArrayList<String>();
	
	
	/**  Constructor  **/
	public Quote(String q, String a) {
		this.quote = q;
		this.author = a;
		this.quoteLen = q.length();
		this.cryptoQuote = encrypt();
		
		findCharFreq();
		collectWords();
		 
		this.qEasy = toEasy();
		this.qMed = toMedium();
		this.qHard = toHard();
		//System.out.println(easyHidden);
		//System.out.println(qEasy);
	}

	/** Method to print the quote **/
	public String printQuote() {
		return "\"" + this.quote +  "\"" + " -" + this.author + "\n" + "\"" + this.cryptoQuote + "\"";
	}
	
	/** toString; displays detailed information about the quote **/
	public String toString() {
		
		StringBuilder ret = new StringBuilder("");
		
		ret.append("String quote: " + this.quote + "\n");
		ret.append("String author: " + this.author + "\n");
		ret.append("int quoteLen: " + this.quoteLen + "\n");
		ret.append("Map<Character, Integer> charFreq: \n" + this.charFreq + "\n");
		ret.append("Map<Character, Character> alphaToCrypto: \n" + this.alphaToCrypto + "\n");
		ret.append("String cryptoQuote: " + this.cryptoQuote + "\n");
		ret.append("String qEasy: " + this.qEasy + "\n");
		ret.append("String easyHidden: " + this.easyHidden + "\n");
		ret.append("String qMed: " + this.qMed + "\n");
		ret.append("String medHidden: " + this.medHidden + "\n");
		ret.append("String qHard: " + this.qHard + "\n");
		ret.append("String hardHidden: " + this.hardHidden + "\n");
		
		return ret.toString();
	}
	
	/** Class method **/
	public static String updatePuzzle(char posAns, char crypto, Quote q, String str) {
		char [] chArrPlain = str.toCharArray();
		char [] chArrCrypto = q.cryptoQuote.toCharArray();

		for(int i = 0; i < q.quoteLen; i++) {
			if(chArrCrypto[i] == crypto) {
				chArrPlain[i] = posAns;
			}
		}
	
		return String.valueOf(chArrPlain);
	}

	/** Makes a list (array) of the words in the quote **/
	public void collectWords() {
		
		// Deal directly with Strings first
		ArrayList<String> strV = new ArrayList<String>();
		String [] tokens = this.quote.split(" ");
		
		// Get rid of unnecessary punctuation
		for(int i = 0; i < tokens.length; i++) {
			
			tokens[i] = tokens[i].replaceAll("[,?!.;\"]", "");
			
			// Only add the word if it's unique 
			if(!strV.contains(tokens[i]))
				strV.add(tokens[i]);
		}
		
		// Build Word AL
		for(String w: strV) {
			Word word = new Word(w);
			words.add(word);
		}
	}
	
	/** Subroutine to make a list (array) of the words/tokens in the encrypted quotes
	 *  Populates the given ArrayList with strings taken from given string **/
	public static void collectTokens(String str, ArrayList<String> arrL) {
		
		// Deal directly with Strings first
		String [] tokens = str.split(" ");
		
		// Get rid of unnecessary punctuation
		for(int i = 0; i < tokens.length; i++) {
			// Remove punctuation
			tokens[i] = tokens[i].replaceAll("[,?!.;\"]", "");
			// Add
			arrL.add(tokens[i]);
		}

	}

	/** Given a crypto Char returns the regular alpha version **/
	public Character alphaOf(char crypto) {
		
		return this.cryptoToAlpha.get(crypto);
	}

	/** Given a regular alphabet Char, returns the crypto version **/
	public Character cryptoOf(char letter) {

		return this.alphaToCrypto.get(letter);
	}

	/** Encrypts the quote; should only be used once; gives alphaToCrypto its value **/
	public String encrypt() {
		
		ArrayList<Character> alphaKey = new ArrayList<Character>();		// Shuffled Alphabet will go here
		char [] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();  // Regular Alphabet
		
		alphaKey.addAll(Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'));
		Collections.shuffle(alphaKey);
		
		// Copy original quote into char array so we can modify it
		char [] encryptCharArr = this.quote.toCharArray();
		
		// Get length of quote
		int len = encryptCharArr.length;

		// Maps letters to their encrypted versions; sorted alphabetically; alphaToCrypto given its values
		for(int j = 0; j < alphabet.length; j++) {
			int indx = alphaKey.indexOf(Character.valueOf(alphabet[j]));
			this.alphaToCrypto.put(alphabet[j], alphabet[indx]);
		}

		// Maps letters to their encrypted versions; sorted alphabetically; alphaToCrypto given its values
		for(Character key: alphaToCrypto.keySet()) {
			this.cryptoToAlpha.put(alphaToCrypto.get(key), key);
		}

		// Get encrypted version of quote
		for(int i = 0; i < len; i++) {
			// If this character is a letter, modify
			if(Character.isLetter(this.quote.charAt(i))) {
				encryptCharArr[i] = alphaToCrypto.get(this.quote.charAt(i));
			}	
		}

		// Convert char [] to string
		String encrypt = String.valueOf(encryptCharArr);
		
		// Make array(list) of cryptowords
		collectTokens(encrypt, cryptoWords);
		
		return encrypt;
	}

	/** Method to get the character frequency; gives this.charFreq its value **/
	public void findCharFreq() {
		
		// Temporary hash map to get characters and their values; unsorted
		Map<Character, Integer> unsortedChFreq = new HashMap<Character,Integer>();
		
		// Make Character ArrayList of characters in quote
		char [] tempChArr = this.quote.toCharArray();
		ArrayList<Character> chArr = new ArrayList<Character>(this.quoteLen);
		for(int i = 0; i < quoteLen; i++)
			chArr.add(tempChArr[i]);

		// Go through Characters in chArr and map characters to the # of times they appear
		for(Character chara: chArr) {
			if (Character.isLetter(chara)) {
				if(unsortedChFreq.containsKey(chara))
					unsortedChFreq.put(chara, unsortedChFreq.get(chara) + 1); // Update value in hash table
				else
					unsortedChFreq.put(chara, 1);							  // Add new entry in hash table
			}
		}
		
		// Sort hashmap by frequency, with lowest first
		this.charFreq = unsortedChFreq
				.entrySet()
				.stream()
				.sorted(comparingByValue())
				.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,LinkedHashMap::new));
	}

	/** Method to create the Easy quote puzzle:
	 * top 70% of least frequent charas to be shown **/
	public String toEasy() {
		
		// Variables
		double percentage = .7;		// Percentage of characters revealed
		int numOfCharsRevealed = (int) (this.charFreq.size() * percentage); // int number of characters revealed
		
		// Keyset from charFreq hashmap (which is sorted by least frequent on top)
		ArrayList<Character> charsinQuote = new ArrayList<Character>(this.charFreq.keySet());
		
		// The top 70% of least frequent charas to be shown will be here
		ArrayList<Character> charsRevealed = new ArrayList<Character>(numOfCharsRevealed);	
		
		// Build charsRevealed
		for(int i = 0; i < numOfCharsRevealed; i++) 
			charsRevealed.add(charsinQuote.get(i));
		
		// Put remainder in list of hidden charas
		for(int i = numOfCharsRevealed; i < charsinQuote.size(); i++)
			easyHidden.add(charsinQuote.get(i));
		
		// One character array for comparing, the other for modifying
		char [] oldChArr = this.quote.toCharArray();
		char [] newChArr = this.quote.toCharArray();

		// Build the easyQuote
		for(int j = 0; j < this.quoteLen; j++) {
			if(Character.isLetter(oldChArr[j])) {
				if(!charsRevealed.contains(oldChArr[j])) {
					newChArr[j] = '_';
				}
			}
		}
		
		// Make list of tokens from encrypted quote
		String easyStr = String.valueOf(newChArr);
		collectTokens(easyStr, easyWordsHidden);
				
		return easyStr;
	}
	
	/** Method to create the Medium quote puzzle
	 * top 50% of least frequent charas to be shown **/
	public String toMedium() {
		
		// Variables
		double percentage = .5;		// Percentage of characters revealed
		int numOfCharsRevealed = (int) (this.charFreq.size() * percentage); // int number of characters revealed

		// Keyset from charFreq hashmap (which is sorted by least frequent on top)
		ArrayList<Character> charsinQuote = new ArrayList<Character>(this.charFreq.keySet());
		
		// The top 70% of least frequent charas to be shown will be here
		ArrayList<Character> charsRevealed = new ArrayList<Character>(numOfCharsRevealed);	

		// Build charsRevealed
		for(int i = 0; i < numOfCharsRevealed; i++) 
			charsRevealed.add(charsinQuote.get(i));

		// Put remainder in list of hidden charas
		for(int i = numOfCharsRevealed; i < charsinQuote.size(); i++)
			medHidden.add(charsinQuote.get(i));

		// One character array for comparing, the other for modifying
		char [] oldChArr = this.quote.toCharArray();
		char [] newChArr = this.quote.toCharArray();

		// Build the easyQuote
		for(int j = 0; j < this.quoteLen; j++) {
			if(Character.isLetter(oldChArr[j])) {
				if(!charsRevealed.contains(oldChArr[j])) {
					newChArr[j] = '_';
				}
			}
		}

		// Make list of tokens from encrypted quote
		String medStr = String.valueOf(newChArr);
		collectTokens(medStr, medWordsHidden);

		return medStr;
	}

	/** Method to create the Hard quote puzzle: 
	 * top 20% of least frequent charas to be shown **/
	public String toHard() {
		
		// Variables
		double percentage = .2;		// Percentage of characters revealed
		int numOfCharsRevealed = (int) (this.charFreq.size() * percentage); // int number of characters revealed

		// Keyset from charFreq hashmap (which is sorted by least frequent on top)
		ArrayList<Character> charsinQuote = new ArrayList<Character>(this.charFreq.keySet());
		
		// The top 70% of least frequent charas to be shown will be here
		ArrayList<Character> charsRevealed = new ArrayList<Character>(numOfCharsRevealed);	

		// Build charsRevealed
		for(int i = 0; i < numOfCharsRevealed; i++) 
			charsRevealed.add(charsinQuote.get(i));

		// Put remainder in list of hidden charas
		for(int i = numOfCharsRevealed; i < charsinQuote.size(); i++)
			hardHidden.add(charsinQuote.get(i));

		// One character array for comparing, the other for modifying
		char [] oldChArr = this.quote.toCharArray();
		char [] newChArr = this.quote.toCharArray();

		// Build the easyQuote
		for(int j = 0; j < this.quoteLen; j++) {
			if(Character.isLetter(oldChArr[j])) {
				if(!charsRevealed.contains(oldChArr[j])) {
					newChArr[j] = '_';
				}
			}
		}

		// Make list of tokens from encrypted quote
		String hardStr = String.valueOf(newChArr);
		collectTokens(hardStr, hardWordsHidden);

		return hardStr;
	}

	/** Getters and Setters **/

	public Map<Character, Character> getCryptoToAlpha() {
		return cryptoToAlpha;
	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public String getQuote() {
		return quote;
	}

	public String getCryptoQuote() {
		return cryptoQuote;
	}

	public String getAuthor() {
		return author;
	}

	public int getQuoteLen() {
		return quoteLen;
	}

	public String getqEasy() {
		return qEasy;
	}

	public ArrayList<Character> getEasyHidden() {
		return easyHidden;
	}

	public String getqMed() {
		return qMed;
	}

	public ArrayList<Character> getMedHidden() {
		return medHidden;
	}

	public String getqHard() {
		return qHard;
	}

	public ArrayList<Character> getHardHidden() {
		return hardHidden;
	}

	public Map<Character, Integer> getCharFreq() {
		return charFreq;
	}

	public Map<Character, Character> getAlphaToCrypto() {
		return alphaToCrypto;
	}
	
	public void setCryptoToAlpha(Map<Character, Character> cryptoToAlpha) {
		this.cryptoToAlpha = cryptoToAlpha;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}


	public void setQuote(String quote) {
		this.quote = quote;
	}

	public void setCryptoQuote(String cryptoQuote) {
		this.cryptoQuote = cryptoQuote;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setQuoteLen(int quoteLen) {
		this.quoteLen = quoteLen;
	}

	public void setqEasy(String qEasy) {
		this.qEasy = qEasy;
	}

	public void setEasyHidden(ArrayList<Character> easyHidden) {
		this.easyHidden = easyHidden;
	}

	public void setqMed(String qMed) {
		this.qMed = qMed;
	}

	public void setMedHidden(ArrayList<Character> medHidden) {
		this.medHidden = medHidden;
	}

	public void setqHard(String qHard) {
		this.qHard = qHard;
	}

	public void setHardHidden(ArrayList<Character> hardHidden) {
		this.hardHidden = hardHidden;
	}

	public void setCharFreq(Map<Character, Integer> charFreq) {
		this.charFreq = charFreq;
	}

	public void setAlphaToCrypto(Map<Character, Character> alphaToCrypto) {
		this.alphaToCrypto = alphaToCrypto;
	}

	public ArrayList<String> getEasyWordsHidden() {
		return easyWordsHidden;
	}

	public ArrayList<String> getMedWordsHidden() {
		return medWordsHidden;
	}

	public ArrayList<String> getHardWordsHidden() {
		return hardWordsHidden;
	}

	public void setEasyWordsHidden(ArrayList<String> easyWordsHidden) {
		this.easyWordsHidden = easyWordsHidden;
	}

	public void setMedWordsHidden(ArrayList<String> medWordsHidden) {
		this.medWordsHidden = medWordsHidden;
	}

	public void setHardWordsHidden(ArrayList<String> hardWordsHidden) {
		this.hardWordsHidden = hardWordsHidden;
	}

	public ArrayList<String> getCryptoWords() {
		return cryptoWords;
	}

	public void setCryptoWords(ArrayList<String> cryptoWords) {
		this.cryptoWords = cryptoWords;
	}
}
