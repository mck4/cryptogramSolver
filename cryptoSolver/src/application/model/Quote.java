/* Quote Class:
 * For quotes, their authors and their cryptoquote
 */

package application.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Quote {
	String quote;	// Regular, unaltered quote
	String cryptoQuote; // Encrypted quote
	String author;		// Author
	ArrayList<Character> alphaKey = new ArrayList<Character>();		// Shuffled Alphabet
	char [] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();  // Regular Alphabet
	
	// Constructor
	public Quote(String q, String a) {
		this.quote = q;
		this.author = a;
		this.cryptoQuote = encrypt();
	}

	// toString
	public String toString(){
		return "\"" + this.quote +  "\"" + " -" + this.author + "\n" + "\"" + this.cryptoQuote + "\"";
	}
	
	// Encrypts the quote; should only be used once....
	public String encrypt(){
		
		alphaKey.addAll(Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'));
		Collections.shuffle(alphaKey);
		
		// Copy original quote into char array so we can modify it
		char [] encryptCharArr = this.quote.toCharArray();
		
		// Get length of quote
		int len = encryptCharArr.length;
	
		for(int i =0; i<len; i++){
			
			// If this character is a letter, modify
			if(Character.isLetter(this.quote.charAt(i))) {
				char temp = quote.charAt(i); 					  // Get current alphabet letter
				int indx = alphaKey.indexOf(Character.valueOf(temp)); // Get position of shuffled alpha (0-25)
				encryptCharArr[i] = alphabet[indx];					  // Use this index to get corresponding letter
			}	
		}
		
		// Convert char [] to string
		String encrypt = String.valueOf(encryptCharArr);
	
		return encrypt;
	}
	
	// Getters and Setters
	
	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getCryptoQuote() {
		return cryptoQuote;
	}

	public void setCryptoQuote(String cryptoQuote) {
		this.cryptoQuote = cryptoQuote;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ArrayList<Character> getAlphaKey() {
		return alphaKey;
	}

	public void setAlphaKey(ArrayList<Character> alphaKey) {
		this.alphaKey = alphaKey;
	}
}
