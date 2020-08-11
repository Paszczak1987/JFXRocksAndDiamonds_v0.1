package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class EnterPlayerName extends StateController{
	
	private GameState parent;

	@FXML
	private TextField textField;	
	@FXML
	private Button button;
	private String btnGeneralStyle;
	private DropShadow btnShadow;
	
	@FXML
	public void initialize() {
		
		btnGeneralStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		button.setStyle("-fx-background-color: lightgray");
		
		textField.setStyle("-fx-background-color: white;");
		
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
		GameData.setPlayerName(textField.getText());
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
			button.setStyle(btnGeneralStyle+"-fx-text-fill: black;");
			button.setEffect(btnShadow);
			button.setCursor(Cursor.HAND);
		}else {
			button.setStyle(btnGeneralStyle+"-fx-text-fill: gray;");
			button.setEffect(null);
			button.setCursor(Cursor.DEFAULT);
		}
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
	}
	
}
