package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Menu extends StateController {

	private GameState parent;

	@FXML
	private VBox menuBox;
	@FXML
	private StackPane stackPane;
	@FXML
	private Button game;
	@FXML
	private Button options;
	@FXML
	private Button hallOfFame;
	@FXML
	private Button quit;

	private List<Button> buttons;
	private int btnIndex;
	private String btnGeneralStyle;
	private DropShadow btnShadow;

	public void initialize() {

		buttons = new ArrayList<Button>();
		buttons.add(game);
		buttons.add(options);
		buttons.add(hallOfFame);
		buttons.add(quit);
		
		btnIndex = 0;
		btnGeneralStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		focusOnButton(btnIndex);

		for (Button btn : buttons) {
			btn.setFocusTraversable(false);
			btn.setCursor(Cursor.HAND);
		}

		{// KeyListener na menuBox
			menuBox.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					buttonOnKeyEvent(e);
				}
			};
			menuBox.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		} // KeyListener na menuBox

	}

	public void gameOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.GAME);
		game.setText("Reasume Game");
	}

	public void optionsOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.OPTIONS);
	}

	public void hallOfFameOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.HALLOFFAME);
	}

	public void quitOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.QUIT);
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
			if (id.equals("game")) {
				parent.mainWindow().changeState(GameStates.GAME);
				game.setText("Reasume Game");
			} else if (id.equals("options"))
				parent.mainWindow().changeState(GameStates.OPTIONS);
			else if (id.equals("hallOfFame"))
				parent.mainWindow().changeState(GameStates.HALLOFFAME);
			else if (id.equals("quit"))
				parent.mainWindow().changeState(GameStates.QUIT);
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
