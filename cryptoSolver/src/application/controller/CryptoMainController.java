/** CryptoMainController.java **/

package application.controller;


import java.util.ArrayList;
import java.util.Arrays;
import application.model.Dictionary;
import application.model.PuzzleBox;
import application.model.Quote;
import application.model.Solver;
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

	// Buttons
	@FXML private VBox puzzles;
	@FXML private Label displayauthor;
	@FXML private Label display;
	@FXML private Label displaynum;
	@FXML private Label displaycrypto;
	@FXML private TextArea postitnote;
	@FXML private ComboBox<String> choices;

	// Variables
	public static int easy = 1, medium = 2, hard = 3;
	public static Quote currentQ;  
	public static Solver solver;
	public static Dictionary dictionary;
	public static int difficultyMode = easy; // 1 - Easy, 2 - Medium, 3 - Hard
	
	//
	int checkForFilledInOne = 0;
	
	ArrayList<String> comboBoxChoices = new ArrayList<String>();

	
	/**  This method loads automatically when program is run **/
	public void initialize() {
		
		// Combo box display
		comboBoxChoices.addAll(Arrays.asList("Easy", "Medium", "Hard", "Solution"));
		choices.setItems(FXCollections.observableArrayList(comboBoxChoices));
		choices.getSelectionModel().selectFirst();
	
		// Load puzzles
		PuzzleBox puzzlebox = new PuzzleBox("quotes/new_quotes.txt");
		
		// Load dictionary & add vocab from the puzzles
		dictionary = new Dictionary(Dictionary.getVocabulary(puzzlebox.getQuotePuzzles()));
			
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
			
			// Actions when button is pressed
			newLabel.setOnAction(value -> { // Reset combo box choice to first one
											choices.getSelectionModel().selectFirst(); 
											// Generate new solver with new puzzle
											solver = null;
											solver = new Solver(dictionary, q, easy); 
											// Display
											displaynum.setText(puzzleNumstr);
											display.setText(solver.getCurrSolution());
											displaycrypto.setText(q.getCryptoQuote());
											displayauthor.setText(q.getAuthor());
											// Set as current Quote
											currentQ = q; });
			// Add button to display
			puzzles.getChildren().add(newLabel);
			puzzleNum++;
			
			if (puzzlebox.getQuotePuzzles().indexOf(q) == 19)
				break;
		}
		
	
	}
	

	/** Method when difficulty on combobox is selected **/
	@FXML
    void cbxChoice(ActionEvent event) {
		// String value of combo box choice
		String difficulty = choices.getSelectionModel().selectedItemProperty().getValue();
		
		// Set difficulty based on choice
		if(difficulty.equals("Easy")) {
			display.setText(currentQ.getqEasy());
			difficultyMode = easy; // easy
			solver = null;
			solver = new Solver(dictionary, currentQ, easy);
		}
		else if (difficulty.equals("Medium")){
			display.setText(currentQ.getqMed());
			difficultyMode = medium; // medium
			solver = null;
			solver = new Solver(dictionary, currentQ, medium);
		}
		else if (difficulty.equals("Hard")){
			display.setText(currentQ.getqHard());
			difficultyMode = hard; // hard
			solver = null;
			solver = new Solver(dictionary, currentQ, hard);
		}
		else { // Solution
			display.setText(currentQ.getQuote());
		}

    }

	/** Method to handle ... **/
	@Override
	public void handle(ActionEvent event) {
		
		//System.out.println("Pressy");
		checkForFilledInOne = solver.checkforFilledInOne();
		display.setText(solver.getCurrSolution());
		
		postitnote.setText("Here we check for words with only 1 solution possible, prioritizing larger words " + checkForFilledInOne + " time(s).");
		//System.out.println(solver.getCurrSolution());
		
	}
	
	
}
