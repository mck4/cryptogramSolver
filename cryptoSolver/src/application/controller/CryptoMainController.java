/** MainController.java **/
package application.controller;


import java.util.ArrayList;
import java.util.Arrays;
import application.model.Dictionary;
import application.model.PuzzleBox;
import application.model.Quote;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CryptoMainController implements EventHandler<ActionEvent> {

	@FXML private VBox puzzles;
	@FXML private Label displayauthor;
	@FXML private Label display;
	@FXML private Label displaynum;
	@FXML private Label displaycrypto;
	@FXML private TextArea postitnote;
	
	@FXML private ComboBox<String> choices;

	public static Quote currentQ;   
	
	ArrayList<String> comboBoxChoices = new ArrayList<String>();

	
	/**  This method loads automatically when program is run **/
	public void initialize() {
		
		comboBoxChoices.addAll(Arrays.asList("Easy", "Medium", "Hard", "Solution"));
		choices.setItems(FXCollections.observableArrayList(comboBoxChoices));
		choices.getSelectionModel().selectFirst();
		
		
		// Load puzzles
		PuzzleBox puzzlebox = new PuzzleBox("quotes/new_quotes.txt");
		
		// Load dictionary & add vocab from the puzzles
		Dictionary dictionary = new Dictionary(Dictionary.getVocabulary(puzzlebox.getQuotePuzzles()));
		
		
		// Adds the buttons linking to the puzzles dynamically
		int puzzleNum = 1;
		for(Quote q: puzzlebox.getQuotePuzzles()) {
			// Create a new button
			Button newLabel = new Button();
			// Set look of the button
			newLabel.setStyle("-fx-border-color:  #b8860b;");
			newLabel.setMaxWidth(155);
			newLabel.setMaxHeight(33);
			newLabel.setPadding(new javafx.geometry.Insets(2.0, 5.0, 2.0, 5.0));
			newLabel.setAlignment(Pos.BASELINE_LEFT);
			// Set button text
			newLabel.setText("Puzzle #" +  puzzleNum + "\n" + q.getAuthor());
			
			// Right text is changed when the button is clicked
			String puzzleNumstr = "Puzzle #" + puzzleNum;
			newLabel.setOnAction(value -> {
						choices.getSelectionModel().selectFirst(); // Reset combo box choice to first one
						displaynum.setText(puzzleNumstr);
						display.setText(q.getqEasy());
						displaycrypto.setText(q.getCryptoQuote());
						displayauthor.setText(q.getAuthor());
						currentQ = q;
						});
			// Add button
			puzzles.getChildren().add(newLabel);
			puzzleNum++;
		}
		
	
	}
	

	/** Method when difficulty on combobox is selected **/
	@FXML
    void cbxChoice(ActionEvent event) {
		String difficulty = choices.getSelectionModel().selectedItemProperty().getValue();
		if(difficulty.equals("Easy")) {
			display.setText(currentQ.getqEasy());
			
		}
		else if (difficulty.equals("Medium")){
			display.setText(currentQ.getqMed());
			//currentQ.updateEasy('X');
		}
		else if (difficulty.equals("Hard")){
			display.setText(currentQ.getqHard());
		}
		else {
			display.setText(currentQ.getQuote());
		}

    }

	/** Method to handle ... **/
	@Override
	public void handle(ActionEvent event) {

	}
	
	
}
