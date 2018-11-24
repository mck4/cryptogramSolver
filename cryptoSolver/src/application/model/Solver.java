package application.model;

public class Solver {
	// Solver has a dictionary
	Dictionary dict;
	
	public Solver(Dictionary d) {
		this.dict = d;
	}
	
	public void checkforOneLetterWords(Quote q) {
		for(Word w: q.getWords()){
			if(w.getWord().length() == 1) {
				char crypto = w.getWord().charAt(0);
				String trying = Quote.updatePuzzle('a', crypto, q);
				System.out.println(trying);
			}
		}
		
	}

}
