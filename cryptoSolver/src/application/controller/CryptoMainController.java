/** MainController.java **/
package application.controller;

import java.awt.Insets;

import application.model.Dictionary;
import application.model.PuzzleBox;
import application.model.Quote;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CryptoMainController implements EventHandler<ActionEvent> {

	@FXML
    private VBox puzzles;
	

	/**  This method loads automatically when program is run **/
	public void initialize() {
		
		PuzzleBox puzzlebox = new PuzzleBox("quotes/new_quotes.txt");
		Dictionary dictionary = new Dictionary(Dictionary.getVocabulary(puzzlebox.getQuotePuzzles()));
		
		
		// Adds the buttons dynamically
		int puzzleNum = 1;
		for(Quote q: puzzlebox.getQuotePuzzles()) {
			Label newLabel = new Label();
			newLabel.setStyle("-fx-border-color:  #b8860b;");
			newLabel.setMaxWidth(155);
			newLabel.setMaxHeight(33);
			newLabel.setPadding(new javafx.geometry.Insets(2.0, 5.0, 2.0, 5.0));
			newLabel.setText("Puzzle #" +  puzzleNum + "\n" + q.getAuthor());
			puzzles.getChildren().add(newLabel);
			puzzleNum++;
		}
		
	
	}

	/** Method to handle ... **/
	@Override
	public void handle(ActionEvent event) {
		
	}
	
	
}
