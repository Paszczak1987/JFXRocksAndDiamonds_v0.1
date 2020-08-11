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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Options extends StateController{
	
	private GameState parent;

	@FXML
	private VBox optionsBtnBox;
	@FXML
	private Button music;
	@FXML
	private Button texture;
	@FXML
	private Button difficulty;
	@FXML
	private Button backToMenu;
	@FXML
	private TextField musicValue;
	@FXML
	private TextField textureValue;
	@FXML
	private TextField difficultyValue;
	
	private List<Button> buttons;
	private List<TextField> options;
	private int btnIndex;
	private String btnGeneralStyle;
	private DropShadow btnShadow;
	private String optionGeneralStyle;
	
	@FXML
	public void initialize() {
		
		buttons = new ArrayList<Button>();
		buttons.add(music);
		buttons.add(texture);
		buttons.add(difficulty);
		buttons.add(backToMenu);
		
		btnIndex = 0;
		btnGeneralStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 0.0f, 1.0f, 1.0f);
		focusOnButton(btnIndex);
		
		for (Button btn : buttons) {
			btn.setFocusTraversable(false);
			btn.setCursor(Cursor.HAND);
		}
		
		options = new ArrayList<TextField>();
		options.add(musicValue);
		options.add(textureValue);
		options.add(difficultyValue);
		
		optionGeneralStyle = "-fx-background-color: rgb(31,29,29);";
		
		for(TextField option: options)
			option.setStyle(optionGeneralStyle+" -fx-text-fill: white;");
		

		{// KeyListener na optionsBtnBox
			optionsBtnBox.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					buttonOnKeyEvent(e);
				}
			};
			optionsBtnBox.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		} // KeyListener na optionsBtnBox
		
		updateValues();
		
	}
	
	private void updateValues() {
		if(GameData.isMusicOn())
			musicValue.setText("ON");
		else
			musicValue.setText("OFF");
		textureValue.setText(GameData.getTexture());
		difficultyValue.setText(GameData.getDifficulty()+"s");
	}
	
	public void disableDifficultyValues() {
		difficultyValue.setStyle(optionGeneralStyle+" -fx-text-inner-color: grey;");
	}
	
	public void musicOnMouseClicked() {
		GameData.musicOnOff();
		updateValues();
	}
	
	public void textureOnMouseClicked() {
		GameData.changeTexture();
		updateValues();
		Game game = (Game)(parent.mainWindow().getGameState(GameStates.GAME).getController());
		game.getPlayer().changeTextures(GameData.getTexture());
		game.updateLevel();
	}
	
	public void difficultyOnMouseClicked() {
		Game game = (Game)(parent.mainWindow().getGameState(GameStates.GAME).getController());
		if(!game.isGameGoingOn()) {
			GameData.changeDifficulty();
			game.setDifficulty();
			updateValues();			
		}
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
			if (id.equals("music"))
				musicOnMouseClicked();
			else if (id.equals("texture"))
				textureOnMouseClicked();
			else if (id.equals("difficulty"))
				difficultyOnMouseClicked();
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
