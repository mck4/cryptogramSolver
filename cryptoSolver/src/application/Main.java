/** Main.java **/

/** Cryptogram Solver by 
 * Zoey Abrigo & Carolina Kimbrough
 */
package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	public static Stage stage;
	
	/**  Loads the FXML file, sets the scene onto stage **/
	@Override
	public void start(Stage primaryStage) {
		
		stage = primaryStage; // Initializing the stage
		
		try {
			
			// Load the FXML document
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation( getClass().getResource("/CryptoMain.fxml") );
			
			// Load the layout from the FXML and add it to the scene
			AnchorPane layout = (AnchorPane) loader.load();
			Scene scene = new Scene( layout );
			
			// Style sheet
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// Set the scene to stage and show the stage to user
			stage.setScene( scene );
			stage.setTitle( "Cryptogram Solver" );
			//stage.getIcons().add(new Image("file:../../sudoku.png"));
			
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Everything starts here **/
	public static void main(String[] args) {
		launch(args);
	}
}
