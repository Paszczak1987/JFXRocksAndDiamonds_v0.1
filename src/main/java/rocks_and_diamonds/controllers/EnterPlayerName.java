package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class EnterPlayerName extends StateController{
	
	private GameState parent;
	
	@FXML
	private StackPane stackpane;	
	@FXML
	private Rectangle rectangle;	
	@FXML
	private StackPane stackPane;	
	@FXML
	private VBox vBox;	
	@FXML
	private Label label;	
	@FXML
	private HBox hBox;	
	@FXML
	private TextField textField;	
	@FXML
	private Button button;
	
	@FXML
	public void initialize() {
		textField.setStyle("-fx-focus-color: transparent;");
		start();
	}
	
	public void buttonOnMouseClicked() {
		if(!textField.getText().equals(""))
			goToLoading();
	}
	
	public void textFieldOnKeyPressed(KeyEvent e) {
		if(e.getCode() == KeyCode.ENTER && !textField.getText().equals(""))
			goToLoading();
	}
	
	private void goToLoading() {
		stop();
		parent.mainWindow().setPlayerName(textField.getText());
		parent.mainWindow().changeState(GameStates.LOADING);
		parent.mainWindow().getGameState().getController().start();	//start Timera
		parent.mainWindow().getGameState().getController().play();	//start Animacji 	
	}
	
	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void handle(long now) {
		if(!textField.getText().equals("")) {
			button.setOpacity(0.7);
			button.setDisable(false);
			button.setCursor(Cursor.HAND);
		}else {
			button.setDisable(true);
			button.setOpacity(0.3);
		}
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
	}
	
}
