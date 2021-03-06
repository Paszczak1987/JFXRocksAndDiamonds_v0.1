package rocks_and_diamonds;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import rocks_and_diamonds.controllers.StateController;

public class GameState {

	private MainWindow parent;
	private GameStates stateName;
	private StackPane stackPane;
	private Scene scene;
	private StateController controller;

	public GameState(MainWindow parent, GameStates state, URL fxmlPathForScene) throws IOException {

		this.parent = parent;
		this.stateName = state;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(fxmlPathForScene); 
		stackPane = loader.load();
		controller = loader.getController();
		controller.setParent(this);
		scene = new Scene(stackPane);

		// Ladowanie CSSow
		// scene.getStylesheets().add(this.getClass().getResource("/CSS/"+stateName.toString()+".css").toExternalForm());
	}

	public Scene getScene() {
		return scene;
	}

	public GameStates getState() {
		return stateName;
	}

	public MainWindow mainWindow() {
		return parent;
	}

	public StateController getController() {
		return controller;
	}

}// class GameState
