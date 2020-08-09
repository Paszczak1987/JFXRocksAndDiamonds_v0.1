package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Options extends StateController{
	
	private GameState parent;
	
	@FXML
	private StackPane stackPane;
	@FXML
	private HBox hBox;	
	@FXML
	private VBox vBox1;
	@FXML
	private VBox vBox2;
	@FXML
	private Button music;
	@FXML
	private Button texture;
	@FXML
	private Button difficulty;
	@FXML
	private Button backToMenu;
	@FXML
	private TextField tfmusic;
	@FXML
	private TextField tftexture;
	@FXML
	private TextField tfdifficulty;
	
	private List<Button> buttons;
	private int btnIndex;
	private String btnGeneralStyle;
	private DropShadow btnShadow;
	
	
	@FXML
	public void initialize() {
		
		buttons = new ArrayList<Button>();
		buttons.add(music);
		buttons.add(texture);
		buttons.add(difficulty);
		buttons.add(backToMenu);
		
		btnIndex = 0;
		btnGeneralStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		focusOnButton(btnIndex);

		for (Button btn : buttons) {
			btn.setFocusTraversable(false);

			btn.setCursor(Cursor.HAND);
		}

		{// KeyListener na menuBox
			vBox1.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					buttonOnKeyEvent(e);
				}
			};
			vBox1.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		} // KeyListener na menuBox
		
	}
	
	
	public void musicOnMouseClicked() {
		
	}
	
	public void textureOnMouseClicked() {
		
	}
	
	public void difficultyOnMouseClicked() {
		
	}
	
	public void returnOnMouseClicked() {
			parent.mainWindow().changeState(GameStates.MENU);
	}
	
	public void onEnter(MouseEvent e) {
		for(int i = 0; i < buttons.size(); i++) {
			if(e.getTarget().hashCode() == buttons.get(i).hashCode())
				btnIndex = i;
		}
		focusOnButton(btnIndex);
	}
	
	public void buttonOnKeyEvent(KeyEvent e) {
		if(e.getCode() == KeyCode.DOWN) {
			if(btnIndex < buttons.size() - 1)
				btnIndex++;
			else
				btnIndex = 0;
		}else if (e.getCode() == KeyCode.UP){
			if(btnIndex > 0)
				btnIndex--;
			else
				btnIndex = buttons.size() - 1;
		}else if (e.getCode() == KeyCode.ENTER) {
			String id = buttons.get(btnIndex).getId();
			if (id.equals("music")) {
				
			} else if (id.equals("texture")) {
				
			}
			else if (id.equals("difficulty")) {
			
			}
			else if (id.equals("backToMenu")) {
				parent.mainWindow().changeState(GameStates.MENU);
			}
				
		}
		
		focusOnButton(btnIndex);		
	}
	
	private void focusOnButton(int btnIndex) {
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			if(i == btnIndex) {
				btn.setStyle(btnGeneralStyle+"-fx-text-fill: black;");
				btn.setEffect(btnShadow);
			}else {
				btn.setStyle(btnGeneralStyle+"-fx-text-fill: grey;");
				btn.setEffect(null);
			}
		}
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
