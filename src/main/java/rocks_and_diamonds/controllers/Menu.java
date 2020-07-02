package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Menu extends StateController {
	
	private GameState parent;
	
	@FXML
	private StackPane stackPane;
	@FXML
	private SplitPane splitPane;
	@FXML
	private AnchorPane anchorPaneMenu;
	@FXML
	private AnchorPane anchorPanePicture;
	@FXML
	private VBox vBox;
	@FXML
	private Button reasumeGame;
	@FXML
	private Button newGame;
	@FXML
	private Button options;
	@FXML
	private Button hallOfFame;
	@FXML
	private Button quit;
	
	private List<Button> buttons;
	
	public void initialize() {
		
		buttons = new ArrayList<Button>();
		buttons.add(reasumeGame);
		buttons.add(newGame);
		buttons.add(options);
		buttons.add(hallOfFame);
		buttons.add(quit);
		
		{// KeyListener na Buttony
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					buttonOnKeyEvent(e);
				}
			};
			for(Button btn: buttons)
				btn.addEventHandler(KeyEvent.KEY_PRESSED, keyPressed);
		}// KeyListener na Buttony
		
	}
	
	public void newGameOnMouseClicked() {
		parent.mainWindow().changeState(GameStates.GAME);
	}
	
	public void buttonOnKeyEvent(KeyEvent e) {
		for(Button btn: buttons) {
			if(e.getCode() == KeyCode.ENTER) {
				if(btn.isFocused()) {
					if(btn.getId().equals("reasumeGame"))
						System.out.println("RESUME GAME !!!"); // to siê wypierdoli
					else if (btn.getId().equals("newGame"))
						parent.mainWindow().changeState(GameStates.GAME);
					else if (btn.getId().equals("options"))
						parent.mainWindow().changeState(GameStates.OPTIONS);
					else if (btn.getId().equals("hallOfFame"))
						System.out.println("HALL OF FAME !!!"); // tu zmienimy stan na HallOFFame, którego jeszcze nie ma
					else if (btn.getId().equals("quit"))
						parent.mainWindow().changeState(GameStates.QUIT);
				}
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
