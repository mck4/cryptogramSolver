/** Word.java **/
// Has info on each word

package application.model;

public class Word implements Comparable<Word> {
	
	String word;	// The word
	int wordLen;	// Its length
	boolean isAContraction = false; // Is it a contraction?
	
	/** Constructor **/
	public Word(String word) {
		this.word = word;
		this.wordLen = word.length();
		if(this.word.contains("'"))
			this.isAContraction = true;
	}
	
	/** toString **/
	public String toString(){
		return word;
	}
	
	/** compare method **/
	@Override
	public int compareTo(Word w) 
	{ 
		return this.getWord().compareTo(w.getWord()); 
	} 
	
	/** GETTERS AND SETTERS **/

	public String getWord() {
		return word;
	}

	public int getWordLen() {
		return wordLen;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setWordLen(int wordLen) {
		this.wordLen = wordLen;
	}

}
