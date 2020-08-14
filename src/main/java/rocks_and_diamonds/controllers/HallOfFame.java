package rocks_and_diamonds.controllers;


import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class HallOfFame extends StateController{
	
	private GameState parent;
	
	@FXML
	private StackPane stackPane;
	@FXML
	private VBox vBox;
	@FXML
	private Label label;
	@FXML
	private TextArea textArea;
	@FXML
	private Button backToMenu; 
	private String btnGeneralStyle;
	private DropShadow btnShadow;
	
	@FXML
	public void initialize() {
		
		btnGeneralStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		
		textArea.setFocusTraversable(false);
		textArea.setEditable(false);
		backToMenu.setFocusTraversable(false);
		backToMenu.setCursor(Cursor.HAND);
	}
	
	
	
	public void returnOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.MENU);
	}
	
	public void onEnter(MouseEvent e) {
		focusOnButton();
	}

	private void focusOnButton() {
		backToMenu.setStyle(btnGeneralStyle+"-fx-text-fill: black;");
		backToMenu.setEffect(btnShadow);	
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
