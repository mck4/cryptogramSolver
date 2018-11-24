/* Quote Class:
 * For quotes, their authors and their cryptoquote
 */

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
	
	String qEasy;
	String qEasyCrypto;
	ArrayList<Character> easyHidden = new ArrayList<Character>();
	String qMed;
	ArrayList<Character> medHidden = new ArrayList<Character>();
	String qHard;
	ArrayList<Character> hardHidden = new ArrayList<Character>();
	
	// Letter to its frequency; sorted by least freq
	Map<Character, Integer> charFreq = new LinkedHashMap<Character, Integer>(); 
	
	// Key; sorted alphabetically
	Map<Character, Character> alphaToCrypto = new LinkedHashMap<Character, Character>(); 
	Map<Character, Character> cryptoToAlpha = new LinkedHashMap<Character, Character>(); 
	
	
	/**  Constructor **/
	public Quote(String q, String a) {
		this.quote = q;
		this.author = a;
		this.quoteLen = q.length();
		this.cryptoQuote = encrypt();
		
		findCharFreq();
		
		this.qEasy = toEasy();
		this.qMed = toMedium();
		this.qHard = toHard();
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
	
	public String updateEasy(char newCh) {
		char [] chArr = this.qEasy.toCharArray();
		char trying = cryptoToAlpha.get(newCh);
		System.out.println(trying);
		//TODO: add a character to 
		
		return "";
		
	}
	
	/** Encrypts the quote; should only be used once; gives alphaToCrypto its value **/
	public String encrypt(){
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

	/** Method to create the Easy quote puzzle: top 70% of least frequent charas to be shown **/
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
		
		return String.valueOf(newChArr);
	}
	
	/** Method to create the Medium quote puzzle: top 50% of least frequent charas to be shown **/
	public String toMedium(){
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

		return String.valueOf(newChArr);
	}

	/** Method to create the Hard quote puzzle: top 20% of least frequent charas to be shown **/
	public String toHard(){
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

		return String.valueOf(newChArr);
	}
	
	// Getters and Setters

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
}
