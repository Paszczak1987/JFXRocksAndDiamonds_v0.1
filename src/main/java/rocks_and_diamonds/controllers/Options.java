package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import rocks_and_diamonds.GameState;

public class Options extends StateController{
	private GameState parent;
	
	@FXML
	private StackPane stackPane;
	
	
	
	@FXML
	public void initialize() {
		
		
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
