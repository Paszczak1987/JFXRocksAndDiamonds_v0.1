package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class EnterPlayerName extends StateController{
	
	private GameState parent;
	@FXML
	private Label label;
	
	@FXML
	public void initialize() {
		label.setFocusTraversable(true);
	}
	
	public void labelKeyPressed(KeyEvent e) {
		System.out.println("ENTER_PLAYER_NAME");
		if(e.getCode() == KeyCode.SPACE )
			parent.mainWindow().changeState(GameStates.LOADING);
	}
	
	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void handle(long now) {
		// TODO Auto-generated method stub
	}
	
}
