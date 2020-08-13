package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rocks_and_diamonds.items.Items;
import rocks_and_diamonds.items.Player;
import rocks_and_diamonds.items.Item;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Game extends StateController {

	private GameState parent;
	private List<Item> map;
	private List<Item> stones;

	// Timer fields
	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int millisecondsSum;

	// Game fields
	private Player player;
	private int levelNr;
	private boolean pause;
	private boolean gameIsGoing;
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
		this.gameIsGoing = false;

		player = new Player(RECT_SIZE);
		map = new ArrayList<Item>();
		stones = new ArrayList<Item>();

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

		setDifficulty();

		timeLabel.setStyle("-fx-font-size: 12px");
		timeLabel.setText("Wciœnij [ENTER]\njeœli jesteœ gotowy.");
	}

	public void gamePaneOnKeyPressed(KeyEvent e) {

		this.keyEvent = e;

		// -----------------------------------------
		// playerMove() jest wywo³ywany w pêtli gry
		// -----------------------------------------

		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.SPACE)
			parent.mainWindow().changeState(GameStates.QUIT);
		else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
			if(gameIsGoing == false) {
				gameIsGoing = true;
				Options o = (Options)(parent.mainWindow().getGameState(GameStates.OPTIONS).getController());
				o.disableDifficultyValues();
			}
			pauseOrPlay(true);
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.P) {
			if(gameIsGoing)
				if(!pause)
					pauseOrPlay(false);
				else
					pauseOrPlay(true);
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ESCAPE) {
			if(gameIsGoing)
				pauseOrPlay(false);
			parent.mainWindow().changeState(GameStates.MENU);
		} else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.G) {
			for(int i = 0; i< stones.size(); i++) {
				System.out.println(i+" c:"+stones.get(i).getCollision()+" d:"+stones.get(i).getDir());
			}
		}

	}

	public void pauseOrPlay(boolean play) {
		if(play == false) {
			pause = true;
			timeLabel.setStyle("-fx-font-size: 12px");
			timeLabel.setText("Pauza\n[ENTER] by wznowiæ.");
			stop();			
		}else {
			if (timeToCount > 0) {
				pause = false;
				timeLabel.setStyle("-fx-font-size: 15px");
				start();
			}
		}
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
		} else if (color.equals(Items.STONE.getColor())) {
			item = new Item(Items.STONE, RECT_SIZE, x, y);
			stones.add(item);
		}else if(color.equals(Items.DOOR.getColor())) {
			item = new Item(Items.DOOR, RECT_SIZE, x, y);	
		}

		if (item != null)
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

	private void displayLevel() {
		for (Item item : map) {
			gamePane.getChildren().add(item.getBody());
		}
	}

	public void updateLevel() {
		gamePane.getChildren().clear();
		displayLevel();
	}

	private boolean doesCollide(Item item, Bounds playerBounds) {
		return item.getBody().intersects(playerBounds);
	}

	private void stonesCollisions() {
		for(Item stone: stones) {
			double sXl = stone.getBody().getX();
			double sXr = stone.getBody().getX() + RECT_SIZE;
			double sYu = stone.getBody().getY();
			double sYd = stone.getBody().getY() + RECT_SIZE;

			boolean left = false;
			boolean right = false;
			boolean down = false;
			
			for (int i = 0; i < map.size(); i++) {// collision loop
				
				if (map.get(i).equals(stone)) // wykluczamy kamieñ dla którego sprawdzamy kolizje
					i++;
				Item item = map.get(i);
				// Item bounds
				double iXl = item.getBody().getX();
				double iXr = item.getBody().getX() + RECT_SIZE;
				double iYu = item.getBody().getY();

				if (item.getName() == Items.WALL || item.getName() == Items.STONE || item.getName() == Items.DIRT) { // Jeœli WALL lub STONE
					if (sXl == iXr && sYu == iYu) { // 1. LEFT SIDE
						left = (left == false ? true : left);
					} else if (sXr == iXl && sYu == iYu) { // 2. RIGHT SIDE
						right = (right == false ? true : right);
					} else if (sYd == iYu && sXl == iXl) { // 3. DOWN SIDE
						down = (down == false ? true : down);
					}
				}else if(item.getName() == Items.PLAYER) {
					if (sYd == iYu && ((iXl+2 > sXl && iXl+2 < sXr)||(iXr-2 > sXl && iXr-2 < sXr))) { // 3. DOWN SIDE
						down = (down == false ? true : down);
					}
				}

			} // collision loop
			
			if((!left && !right) && !down) {
				stone.setCollision("NONE");
			} else if(left && down) {
				stone.setCollision("DOWN_LEFT");
			} else if(right && down) {
				stone.setCollision("DOWN_RIGHT");
			}else if(left && right) {
				stone.setCollision("LEFT_RIGHT");				
			}else if(left) {
				stone.setCollision("LEFT");
			}else if(right) {
				stone.setCollision("RIGHT");	
			} else if(down) {
				stone.setCollision("DOWN");
			}
			stone.move("DOWN");
		}
	}
	
	private void playerCollisions() {
		// Player bounds
		double pXl = player.getBody().getX();
		double pXr = player.getBody().getX() + RECT_SIZE;
		double pYu = player.getBody().getY();
		double pYd = player.getBody().getY() + RECT_SIZE;
		double shift = 2;
		Bounds pBounds = new BoundingBox(pXl + shift, pYu + shift, RECT_SIZE - shift * 2, RECT_SIZE - shift * 2);

		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;

		for (int i = 0; i < map.size(); i++) { // collision loop

			if (map.get(i).getName() == Items.PLAYER) // Wykluczamy Playera
				i++;
			Item item = map.get(i);
			// Item bounds
			double iXl = item.getBody().getX();
			double iXr = item.getBody().getX() + RECT_SIZE;
			double iYu = item.getBody().getY();
			double iYd = item.getBody().getY() + RECT_SIZE;

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

				if (doesCollide(item, pBounds)) {
					item.playAnimation();
					if (item.getAnimationStatus())
						map.remove(item);
					updateLevel();
				}
				
			}else if(item.getName() == Items.STONE) {// Jeœli STONE
				if((pXl > iXl && pXl < iXr ) && (pYu+shift > iYu && pYu+shift <iYd)) { // 1. LEFT SIDE
					item.move("LEFT");
					left = (left == false ? true : left);
				}else if((pXr > iXl && pXr < iXr) && (pYu+shift > iYu && pYu+shift <iYd)) { // 2. RIGHT SIDE
					item.move("RIGHT");					
					right = (right == false ? true : right);
				} else if (pYd == iYu && pXl == iXl) { // 3. DOWN SIDE
					down = (down == false ? true : down);
				} else if (pYu == iYd && pXl == iXl) { // 4. UP SIDE
					up = (up == false ? true : up);
				}

			} else if (item.getName() == Items.RED_DIAMOND || item.getName() == Items.GREEN_DIAMOND // Jeœli DIAMOND
					|| item.getName() == Items.BLUE_DIAMOND || item.getName() == Items.YELLOW_DIAMOND) {
				if (doesCollide(item, pBounds)) {
					map.remove(item);
					updateLevel();
				}
				
			}else if(item.getName() == Items.DOOR) {
				if (doesCollide(item, pBounds)) {
					System.out.println("door");
				}
			}

		} // collision loop

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
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isGameGoingOn() {
		return gameIsGoing;
	}
	
	public boolean isGamePaused() {
		return pause;
	}

	public void setDifficulty() {
		timeToCount = GameData.getDifficulty();
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
	
			stonesCollisions();
			playerCollisions();
			
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
