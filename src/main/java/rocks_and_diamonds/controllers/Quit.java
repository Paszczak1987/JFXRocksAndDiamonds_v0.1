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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Quit extends StateController {
	
	private GameState parent;
	@FXML
	private StackPane stackPane;
	@FXML
	private VBox vBox;
	@FXML
	private Label label;
	@FXML
	private HBox hBox;
	@FXML
	private Label labelYes;
	@FXML
	private Label labelNo;
	
	private List<Label> labels;
	private int lbIndex;
	private String lbGeneralStyle;
	private DropShadow lbShadow;
	
	
	public void initialize() {
		
		labels = new ArrayList<Label>();
		labels.add(labelYes);
		labels.add(labelNo);
		
		
		lbIndex = 0;
		lbShadow = new DropShadow(BlurType.values()[0], Color.rgb(215, 156, 36), 0, 3.0f, 3.0f, 3.0f);
		focusOnLabel(lbIndex);

		for (Label lb : labels) {
			lb.setFocusTraversable(false);

			lb.setCursor(Cursor.HAND);
		}

		{// KeyListener na menuBox
			hBox.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					labelOnKeyEvent(e);
				}
			};
			hBox.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		} // KeyListener na menuBox
	}
	
	public void onEnter(MouseEvent e) {
		for(int i = 0; i < labels.size(); i++) {
			if(e.getTarget().hashCode() == labels.get(i).hashCode())
				lbIndex = i;
		}
		focusOnLabel(lbIndex);
	}
	
	private void focusOnLabel(int lbIndex) {
		for(int i = 0; i < labels.size(); i++) {
			Label lb = labels.get(i);
			if(i == lbIndex) {
				lb.setStyle(lbGeneralStyle+"-fx-text-fill: black;");
				lb.setEffect(lbShadow);
			}else {
				lb.setStyle(lbGeneralStyle+"-fx-text-fill: grey;");
				lb.setEffect(null);
			}
		}		
	}
	
	public void labelOnKeyEvent(KeyEvent e) {
		if(e.getCode() == KeyCode.RIGHT) {
			if(lbIndex < labels.size() - 1)
				lbIndex++;
			else
				lbIndex = 0;
		}else if (e.getCode() == KeyCode.LEFT){
			if(lbIndex > 0)
				lbIndex--;
			else
				lbIndex = labels.size() - 1;
		}else if (e.getCode() == KeyCode.ENTER) {
			String id = labels.get(lbIndex).getId();
			if (id.equals("labelYes")) {
				parent.mainWindow().close();
			} else if (id.equals("labelNo")) {
				parent.mainWindow().changeState(GameStates.MENU);
			}	
		}
		
		focusOnLabel(lbIndex);		
	}
	
	
	public void noOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.MENU);
	}
	
	public void yesOnMouseClicked() {
		parent.mainWindow().close();
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
