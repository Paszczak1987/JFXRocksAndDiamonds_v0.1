package rocks_and_diamonds.controllers;


import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class HallOfFame extends StateController{
	
	private GameState parent;
	
	@FXML
	private Label title;
	@FXML
	private GridPane table;
	@FXML
	private Button btnBack; 
	@FXML
	private Button btnUp; 
	@FXML
	private Button btnDown; 
	@FXML
	private HBox btnBox; 
	
	private int indexOfPlayer;
	private List<String> names;
	private List<Integer> score;
	private List<Label> nameLabels;
	private List<Label> scoreLabels;
	private List<Button> buttons;
	private int btnIndex;
	private String btnStyle;
	private DropShadow btnShadow;
	private String labelStyle;
	
	@FXML
	public void initialize() {
		
		title.setTextFill(Color.GOLD);
		
		names = new ArrayList<String>();
		score = new ArrayList<Integer>();
		nameLabels = new ArrayList<Label>();
		scoreLabels = new ArrayList<Label>();
		
		buttons = new ArrayList<Button>();
		buttons.add(btnBack);
		buttons.add(btnUp);
		buttons.add(btnDown);
		btnIndex = 0;
		
		btnStyle = "-fx-background-color: lightgray;";
		btnShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		focusOnButton(btnIndex);

		labelStyle = "-fx-text-fill: white; -fx-font-size: 22; -fx-font-weight: bold; -fx-font-style: italic;";
	
		for(Button btn: buttons) {
			btn.setFocusTraversable(false);
			btn.setCursor(Cursor.HAND);
		}
		
		{// KeyListener na btnBox
			btnBox.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					buttonOnKeyEvent(e);
				}
			};
			btnBox.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		} // KeyListener na btnBox
		
		for(int i = 0; i < table.getChildren().size(); i++) {
			Label label = (Label)table.getChildren().get(i);
			if(i%2 == 0)
				nameLabels.add(label);
			else
				scoreLabels.add(label);
			
			label.setStyle(labelStyle);
			label.setText("");
		}
		
		update();
			
	}
	
	public void update() {
		names.clear();
		score.clear();
		
		for(Pair<String,Integer> record: GameData.getStandings()) {
			names.add(record.getKey());
			score.add(record.getValue());
		}
		
		for(int i = 0; i < nameLabels.size(); i++) {
			if(i < names.size()) {
				nameLabels.get(i).setText(names.get(i));
				scoreLabels.get(i).setText(score.get(i).toString());
			}
		}
	}
	
	public void buttonOnKeyEvent(KeyEvent e) {
		if(e.getCode() == KeyCode.RIGHT) {
			if(btnIndex < buttons.size() - 1)
				btnIndex++;
			else
				btnIndex = 0;
		}else if (e.getCode() == KeyCode.LEFT){
			if(btnIndex > 0)
				btnIndex--;
			else
				btnIndex = buttons.size() - 1;
		}else if (e.getCode() == KeyCode.ENTER) {
			String id = buttons.get(btnIndex).getId();
			if (id.equals("btnBack")) {
				returnOnMouseClicked();
			} else if (id.equals("btnUp"))
				upOnMouseClicked();
			else if (id.equals("btnDown"))
				downOnMouseClicked();
		}
		
		focusOnButton(btnIndex);
		
	}
	
	public void onEnter(MouseEvent e) {
		for(int i = 0; i < buttons.size(); i++) {
			if(e.getTarget().hashCode() == buttons.get(i).hashCode())
				btnIndex = i;
		}
		focusOnButton(btnIndex);
	}
	
	public void returnOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.MENU);
	}
	
	public void upOnMouseClicked() {
		if(indexOfPlayer > 0) {
			indexOfPlayer--;
			for(int i = 0; i < nameLabels.size(); i++) {
				if(i < names.size()) {
					nameLabels.get(i).setText(names.get(i+indexOfPlayer));
					scoreLabels.get(i).setText(score.get(i+indexOfPlayer).toString());
				}
					
			}
		}
	}
	
	public void downOnMouseClicked() {
		if(indexOfPlayer + nameLabels.size() < names.size()) {
			indexOfPlayer++;
			for(int i = 0; i < nameLabels.size(); i++) {
				if(i < names.size()) {
					nameLabels.get(i).setText(names.get(i+indexOfPlayer));
					scoreLabels.get(i).setText(score.get(i+indexOfPlayer).toString());
				}
					
			}
		}
	}

	private void focusOnButton(int btnIndex) {
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			if(i == btnIndex) {
				btn.setStyle(btnStyle+"-fx-text-fill: black;");
				btn.setEffect(btnShadow);
			}else {
				btn.setStyle(btnStyle+"-fx-text-fill: grey;");
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
