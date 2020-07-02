package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Menu extends StateController {
	
	private GameState parent;
	
	@FXML
	private StackPane stackPane;
	@FXML
	private SplitPane splitPane;
	@FXML
	private AnchorPane anchorPaneMenu;
	@FXML
	private AnchorPane anchorPanePicture;
	@FXML
	private VBox vBox;
	@FXML
	private Button reasumeGame;
	@FXML
	private Button newGame;
	@FXML
	private Button options;
	@FXML
	private Button hallOfFame;
	@FXML
	private Button quite;
	
	
	public void newGameOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.GAME);
	}

	
	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void handle(long now) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}
	
}
