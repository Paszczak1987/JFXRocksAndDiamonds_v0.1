package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rocks_and_diamonds.GameState;

public class Quit implements StateController {
	private GameState parent;
	
	@FXML
	private Label label;
	
	@FXML
	public void initialize() {
		label.setFocusTraversable(true);
	}
	
	public void labelKeyPressed(KeyEvent e) {
		System.out.println("QUIT");
		if(e.getCode() == KeyCode.SPACE )
			parent.mainWindow().close();
	}
	
	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}
}
