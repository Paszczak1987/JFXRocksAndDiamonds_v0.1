package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rocks_and_diamonds.items.Items;
import rocks_and_diamonds.items.Player;
import rocks_and_diamonds.items.Item;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Game extends StateController {

	private GameState parent;
	private List<Item> map;

	// Timer fields
	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int millisecondsSum;

	// Game fields
	private Player player;
	private int levelNr;
	private boolean pause;
	private String collisionType;
	private KeyEvent keyEvent;
	
	@FXML
	private Pane gamePane;
	@FXML
	private VBox sideDisplay;
	@FXML
	private Label label;
	@FXML
	private Label timeLabel;

	@FXML
	public void initialize() {
		
		this.levelNr = 0;
		this.pause = true;
		
		player = new Player(RECT_SIZE);
		map = new ArrayList<Item>();

		loadLevel();
		displayLevel();

		{// KeyListener na gamePane
			gamePane.setFocusTraversable(true);
			EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					gamePaneOnKeyPressed(e);
				}
			};
			gamePane.addEventHandler(KeyEvent.ANY, keyPressed);
		} // KeyListener na gamePane

		this.timeToCount = 90;

		timeLabel.setStyle("-fx-font-size: 12px");
		timeLabel.setText("Wciœnij [ENTER]\njeœli jesteœ gotowy.");
	}

	public void gamePaneOnKeyPressed(KeyEvent e) {

		this.keyEvent = e;
		
		//-----------------------------------------
		// playerMove() jest wywo³ywany w pêtli gry
		//-----------------------------------------
		
		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.SPACE)
			parent.mainWindow().changeState(GameStates.QUIT);
		else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
			if (timeToCount > 0) {
				pause = false;
				timeLabel.setStyle("-fx-font-size: 15px");
				start();
			}
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.P) {
			pauseGame();
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ESCAPE) {
			pauseGame();
			parent.mainWindow().changeState(GameStates.MENU);
		}

	}
	
	private void pauseGame() {
		pause = true;
		timeLabel.setStyle("-fx-font-size: 12px");
		timeLabel.setText("Pauza\n[ENTER] by wznowiæ.");
		stop();
	}

	private void loadItem(Color color, int x, int y) {
		Item item = null;
		if (color.equals(Items.PLAYER.getColor())) {
			player.setPosition(x, y);
			map.add(player);
		} else if (color.equals(Items.WALL.getColor())) {
			item = new Item(Items.WALL, RECT_SIZE, x, y);			
		} else if (color.equals(Items.DIRT.getColor())) {
			item = new Item(Items.DIRT, RECT_SIZE, x, y);			
		} else if (color.equals(Items.RED_DIAMOND.getColor())) {
			item = new Item(Items.RED_DIAMOND, RECT_SIZE, x, y);						
		} else if (color.equals(Items.GREEN_DIAMOND.getColor())) {
			item = new Item(Items.GREEN_DIAMOND, RECT_SIZE, x, y);			
		} else if (color.equals(Items.BLUE_DIAMOND.getColor())) {
			item = new Item(Items.BLUE_DIAMOND, RECT_SIZE, x, y);			
		} else if (color.equals(Items.YELLOW_DIAMOND.getColor())) {
			item = new Item(Items.YELLOW_DIAMOND, RECT_SIZE, x, y);
		}
		
		if(item != null)
			map.add(item);
	}

	private void loadLevel() {
		Image level = new Image("Pictures/Levels/lvl" + levelNr + ".bmp");
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				Color color = level.getPixelReader().getColor(x, y);
				loadItem(color, x, y);
			}
		}
	}

	public void displayLevel() {
		for (Item item : map) {
			gamePane.getChildren().add(item.getBody());
		}
	}

	public void updateLevel() {
		gamePane.getChildren().clear();
		displayLevel();
	}

	public void checkCollision() {
		//Player bounds
		double pXl = player.getBody().getX();
		double pXr = player.getBody().getX() + RECT_SIZE;
		double pYu = player.getBody().getY();
		double pYd = player.getBody().getY() + RECT_SIZE;

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		for (int i = 0; i < map.size(); i++) {
			
			if(map.get(i).getName() == Items.PLAYER) // Wykluczamy Playera
				i++;
			
			Item item = map.get(i);
			
			//Item bounds
			double iXl = item.getBody().getX();
			double iXr = item.getBody().getX() + RECT_SIZE;
			double iYu = item.getBody().getY();
			double iYd = item.getBody().getY() + RECT_SIZE;
			int shift = 2;

			if (item.getName() == Items.WALL) { // Jeœli WALL
				if (pXl == iXr && pYu == iYu) { // 1. LEFT SIDE
					left = (left == false ? true : left);
				} else if (pXr == iXl && pYu == iYu) { // 2. RIGHT SIDE
					right = (right == false ? true : right);
				} else if (pYd == iYu && pXl == iXl) { // 3. DOWN SIDE
					down = (down == false ? true : down);
				} else if (pYu == iYd && pXl == iXl) { // 4. UP SIDE
					up = (up == false ? true : up);
				}
			} else if (item.getName() == Items.DIRT) { // Jeœli DIRT
				
				if (item.getBody().intersects(pXl + shift, pYu + shift, RECT_SIZE - shift * 2, RECT_SIZE - shift * 2)) {
						item.playAnimation();
					if(item.getAnimationStatus()) 
						map.remove(item);
					updateLevel();
				}
			} else if ((item.getName() == Items.RED_DIAMOND || item.getName() == Items.GREEN_DIAMOND) || (item.getName() == Items.BLUE_DIAMOND || item.getName() == Items.YELLOW_DIAMOND)) {
				if (item.getBody().intersects(pXl + shift, pYu + shift, RECT_SIZE - shift * 2, RECT_SIZE - shift * 2)) {
					map.remove(item);
				}

			}
		}

		if ((!left && !right) && (!up && !down)) {
			collisionType = "NONE";
		} else if ((left && right) && down) {
			collisionType = "WALL_LEFT_RIGHT_DOWN";
		} else if ((left && up) && down) {
			collisionType = "WALL_LEFT_UP_DOWN";
		} else if ((left && up) && right) {
			collisionType = "WALL_LEFT_UP_RIGHT";
		} else if ((up && right) && down) {
			collisionType = "WALL_UP_RIGHT_DOWN";
		} else if (left && up) {
			collisionType = "WALL_LEFT_UP";
		} else if (left && right) {
			collisionType = "WALL_LEFT_RIGHT";
		} else if (left && down) {
			collisionType = "WALL_LEFT_DOWN";
		} else if (up && right) {
			collisionType = "WALL_UP_RIGHT";
		} else if (right && down) {
			collisionType = "WALL_RIGHT_DOWN";
		} else if (up && down) {
			collisionType = "WALL_UP_DOWN";
		} else if (left) {
			collisionType = "WALL_LEFT";
		} else if (up) {
			collisionType = "WALL_UP";
		} else if (right) {
			collisionType = "WALL_RIGHT";
		} else if (down) {
			collisionType = "WALL_DOWN";
		}
	}

	private void playerMove() {
		KeyCode code = keyEvent.getCode();
		if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT) {
			player.move(keyEvent, collisionType);
		}
	}

	// Gameloop/Timer
	@Override
	public void handle(long now) {
		if (lastTime == 0) {
			lastTime = System.currentTimeMillis();
			return;
		}

		millisecondsSum += frameTime;

		{// update
			checkCollision();
			playerMove();
			timeLabel.setText("Time: " + timeToCount + "s left");
		}

		if (millisecondsSum > 1000) {

			timeToCount--;

			if (timeToCount == 0) {
				stop();
				return;
			}

			millisecondsSum = 0;
		}

		frameTime = System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
	}

}
