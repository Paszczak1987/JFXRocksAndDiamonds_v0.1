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

	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int msSum;

	@FXML
	private Pane gamePane;
	@FXML
	private VBox sideDisplay;
	@FXML
	private Label label;

	private KeyEvent e;

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

	}

	public void gamePaneOnKeyPressed(KeyEvent e) {
		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.SPACE)
			parent.mainWindow().changeState(GameStates.QUIT);
		if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.Q)
			check();
		if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
			this.e = e;
			start();
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
	
	private void check() {
		for(int i = 0; i< map.size(); i++) {
			System.out.println(i+":"+map.get(i).getBody().getBoundsInLocal());
			
		}
		
		
	}

	public void checkCollision() {
		
//		double playerXl = player.getBody().getX() - 2;
//		double playerXr = player.getBody().getX() + RECT_SIZE + 2;
//		
//		for (int i = 0; i < map.size(); i++) {
////			if (map.get(i).getItem() == Items.WALL)
//				if (player.getBody().getBoundsInLocal().intersects(map.get(i).getBody().getBoundsInLocal())) {
//					System.out.println(player.getDirection() + " collide");
//				}
//
////			if(playerXr >= map.get(i).getBody().getX()) {
////				if(map.get(i).getItem() == Items.WALL) 
////					System.out.println("kolizja z prawej");
////				
////			}else if(playerX <= map.get(i).getBody().getX()) {
////				if(map.get(i).getItem() == Items.WALL) 
////					System.out.println("kolizja z lewej");
////				
////			}
//		}
	}

	// game loop / timer
	public void handle(long now) {
		if (lastTime == 0) {
			lastTime = System.currentTimeMillis();
			return;
		}

		msSum += frameTime;

		// System.out.println(moving);
		checkCollision();
		player.move(e);

		if (msSum > 1000) {
			timeToCount--;

			if (timeToCount == 0) {
				stop();
				return;
			}

			msSum = 0;
		}

		frameTime = System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

}
