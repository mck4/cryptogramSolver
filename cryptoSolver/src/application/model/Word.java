/** Word.java **/

package application.model;

public class Word implements Comparable<Word> {
	
	String word;
	int wordLen;
	boolean isAContraction = false;
	
	public Word(String word) {
		this.word = word;
		this.wordLen = word.length();
		if(this.word.contains("'"))
			this.isAContraction = true;
	}
	
	public String toString(){
		return word;
	}
	
	@Override
	public int compareTo(Word w) 
	{ 
		return this.getWord().compareTo(w.getWord()); 
	} 
	

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
