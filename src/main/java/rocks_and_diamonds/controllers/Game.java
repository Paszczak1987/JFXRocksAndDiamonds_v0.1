package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

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
import rocks_and_diamonds.items.Item;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Game extends StateController {

	private GameState parent;
	private int levelNr;
	private Item player;
	private List<Item> map;

	//Timer fields
	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int millisecondsSum;
	
	//Game fields
	private boolean pause = true;
	private String collisionType = "NONE";
	private KeyEvent event;

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
		player = new Item(RECT_SIZE);
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
		
		this.event = e;
		
		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.SPACE)
			parent.mainWindow().changeState(GameStates.QUIT);
		else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
			if(timeToCount > 0)
				pause = false;
				timeLabel.setStyle("-fx-font-size: 15px");
				start();
				
		} else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.P) {
			pause = true;
			timeLabel.setStyle("-fx-font-size: 12px");
			timeLabel.setText("Pauza\n[ENTER] by wznowiæ.");
			stop();
		}
			
	}

	private void updateMap(Color color, int x, int y) {
		Item item;
		if (color.equals(Items.PLAYER.getColor())) {
			player.setPosition(x, y);
			map.add(player);
		} else if (color.equals(Items.WALL.getColor())) {
			item = new Item(Items.WALL, RECT_SIZE, x, y);
			map.add(item);
		} else if (color.equals(Items.DIRT.getColor())) {
			item = new Item(Items.DIRT, RECT_SIZE, x, y);
			map.add(item);
		}
	}

	private void loadLevel() {
		Image level = new Image("Pictures/Levels/lvl" + levelNr + ".bmp");
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				Color color = level.getPixelReader().getColor(x, y);
				updateMap(color, x, y);
			}
		}
	}

	public void displayLevel() {
		for (Item item : map) {
			gamePane.getChildren().add(item.getBody());
		}
	}

	public void checkCollision() {
		
		double playerXl = player.getBody().getX();
		double playerXr = player.getBody().getX() + RECT_SIZE;
		double playerYu = player.getBody().getY();
		double playerYd = player.getBody().getY() + RECT_SIZE;
		
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		for (int i = 0; i < map.size(); i++) {
			Item item = map.get(i);
			if (item.getItem() != Items.PLAYER) {	//odrzucamy playera 
				double itemXl = item.getBody().getX();
				double itemXr = item.getBody().getX() + RECT_SIZE;
				double itemYu = item.getBody().getY();
				double itemYd = item.getBody().getY() + RECT_SIZE;	
				if (playerXl == itemXr && playerYu == itemYu) {			// 1. LEFT
					if (item.getItem() == Items.WALL && !left)
						left = true;
				} else if (playerXr == itemXl && playerYu == itemYu) {	// 2. RIGHT
					if (item.getItem() == Items.WALL && !right)
						right = true;
				} else if (playerYd == itemYu && playerXl == itemXl) {	// 3. DOWN
					if (map.get(i).getItem() == Items.WALL && !down)
						down = true;
				} else if (playerYu == itemYd && playerXl == itemXl) {	// 4. UP
					if (map.get(i).getItem() == Items.WALL && !up)
						up = true;
				}
			}
		}
		if((!left && !right) && (!up && !down)) {
			collisionType = "NONE";
		}else if((left && right) && down) {
			collisionType = "WALL_LEFT_RIGHT_DOWN";
		}else if((left && up) && down) {
			collisionType = "WALL_LEFT_UP_DOWN";
		}else if((left && up) && right) {
			collisionType = "WALL_LEFT_UP_RIGHT";
		}else if((up && right)&& down) {
			collisionType = "WALL_UP_RIGHT_DOWN";
		}else if(left && up) {
			collisionType = "WALL_LEFT_UP";
		}else if(left && right) {
			collisionType = "WALL_LEFT_RIGHT";
		}else if(left && down) {
			collisionType = "WALL_LEFT_DOWN";			
		}else if(up && right) {
			collisionType = "WALL_UP_RIGHT";
		}else if(right && down) {
			collisionType = "WALL_RIGHT_DOWN";
		}else if(up && down) {
			collisionType = "WALL_UP_DOWN";
		}else if(left) {	
			collisionType = "WALL_LEFT";
		}else if(up) {
			collisionType = "WALL_UP";
		}else if(right) {
			collisionType = "WALL_RIGHT";
		}else if(down) {
			collisionType = "WALL_DOWN";
		}	
	}
	
	private void playerMove() {
		KeyCode code = event.getCode();
		if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT) {
			if (collisionType.equals("NONE"))
				player.move(event);
			else if (collisionType.equals("WALL_LEFT")) {
				if (code != KeyCode.LEFT)
					player.move(event);
			} else if (collisionType.equals("WALL_UP")) {
				if (code != KeyCode.UP)
					player.move(event);
			} else if (collisionType.equals("WALL_RIGHT")) {
				if (code != KeyCode.RIGHT)
					player.move(event);
			} else if (collisionType.equals("WALL_DOWN")) {
				if (code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_UP")) {
				if (code != KeyCode.LEFT && code != KeyCode.UP)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_RIGHT")) {
				if (code != KeyCode.LEFT && code != KeyCode.RIGHT)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_DOWN")) {
				if (code != KeyCode.LEFT && code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_UP_RIGHT")) {
				if (code != KeyCode.UP && code != KeyCode.RIGHT)
					player.move(event);
			} else if (collisionType.equals("WALL_RIGHT_DOWN")) {
				if (code != KeyCode.RIGHT && code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_UP_DOWN")) {
				if (code != KeyCode.UP && code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_RIGHT_DOWN")) {
				if ((code != KeyCode.LEFT && code != KeyCode.RIGHT) && code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_UP_DOWN")) {
				if ((code != KeyCode.LEFT && code != KeyCode.UP) && code != KeyCode.DOWN)
					player.move(event);
			} else if (collisionType.equals("WALL_LEFT_UP_RIGHT")) {
				if ((code != KeyCode.LEFT && code != KeyCode.UP) && code != KeyCode.RIGHT)
					player.move(event);
			} else if (collisionType.equals("WALL_UP_RIGHT_DOWN")) {
				if ((code != KeyCode.UP && code != KeyCode.RIGHT) && code != KeyCode.DOWN)
					player.move(event);
			}
		}
	}

	// Gameloop/Timer
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

}
