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

	// game loop fields
	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int millisecondsSum;

	// Game fields
	private List<Item> map;
	private List<Item> stones;
	private List<Item> diamonds;
	private Player player;
	private int levelNr;
	private int score;
	private boolean pause;
	private KeyEvent keyEvent;
	private double boundShift;
	String defaultMessage;
	
	@FXML
	private Pane gamePane;
	@FXML
	private VBox sideDisplay;
	@FXML
	private Label lvlLabel;
	@FXML
	private Label timeLabel;
	@FXML
	private Label scoreLabel;
	@FXML
	private Label diamondsLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label message;

	@FXML
	public void initialize() {

		this.levelNr = 0;
		this.pause = true;
		this.boundShift = 2;

		player = new Player(RECT_SIZE);
		map = new ArrayList<Item>();
		stones = new ArrayList<Item>();
		diamonds = new ArrayList<Item>();
		
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

		defaultMessage = "[Enter] to start\n[P] to pause/unpause\n[ESC] to main menu";
		
		statusLabel.setTextFill(Color.GOLD);
		statusLabel.setText("Ready");
		
		diamondsLabel.setText(String.valueOf(player.howMuchDiamondsHave()));
		
		message.setTextFill(Color.CYAN);
		message.setText(defaultMessage);
	}

// -----------------------------------------
//					CONTROLS
//-----------------------------------------	
	public void gamePaneOnKeyPressed(KeyEvent e) {

		this.keyEvent = e;

		// -----------------------------------------
		// playerMove() jest wywo³ywany w pêtli gry
		// -----------------------------------------

		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
			if(GameData.gameIsGoing == false) {
				GameData.gameIsGoing = true;
				Options options = (Options)(parent.mainWindow().getGameState(GameStates.OPTIONS).getController());
				options.disableDifficultyValues();
				pauseOrPlay(true);
			}
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.P) {
			if(GameData.gameIsGoing)
				if(!pause)
					pauseOrPlay(false);
				else
					pauseOrPlay(true);
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ESCAPE) {
			if(GameData.gameIsGoing)
				pauseOrPlay(false);
			parent.mainWindow().changeState(GameStates.MENU);
		} else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.G) {
			System.out.println("P:"+player.howMuchDiamondsHave()+" M:"+diamonds.size());
		}

	}

	public void pauseOrPlay(boolean play) {
		if(play == false) {
			pause = true;
			statusLabel.setTextFill(Color.RED);
			statusLabel.setText("Paused");
			stop();			
		}else {
			if (timeToCount > 0) {
				pause = false;
				statusLabel.setTextFill(Color.CHARTREUSE);
				statusLabel.setText("Playing");
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
			diamonds.add(item);
		} else if (color.equals(Items.GREEN_DIAMOND.getColor())) {
			item = new Item(Items.GREEN_DIAMOND, RECT_SIZE, x, y);			
			diamonds.add(item);
		} else if (color.equals(Items.BLUE_DIAMOND.getColor())) {
			item = new Item(Items.BLUE_DIAMOND, RECT_SIZE, x, y);			
			diamonds.add(item);
		} else if (color.equals(Items.YELLOW_DIAMOND.getColor())) {
			item = new Item(Items.YELLOW_DIAMOND, RECT_SIZE, x, y);
			diamonds.add(item);
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
		lvlLabel.setText(String.valueOf(levelNr));
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

// -----------------------------------------
// 					STONES
// -----------------------------------------
	private void stonesCollisions() {
		for(Item stone: stones) {
			double sXl = stone.getBody().getX();
			double sXr = stone.getBody().getX() + RECT_SIZE;
			double sYu = stone.getBody().getY();
			double sYd = stone.getBody().getY() + RECT_SIZE;
			Bounds sBounds = new BoundingBox(sXl + boundShift, sYu + boundShift, RECT_SIZE - boundShift * 2, RECT_SIZE - boundShift * 2);

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

				if (item.getName() == Items.WALL || item.getName() == Items.STONE || item.getName() == Items.DIRT || item.getName() == Items.DOOR) {
					
					if (sXl == iXr && sYu == iYu) { // 1. LEFT SIDE
						left = (left == false ? true : left);
					} else if (sXr == iXl && sYu == iYu) { // 2. RIGHT SIDE
						right = (right == false ? true : right);
					} else if (sYd == iYu && sXl == iXl) { // 3. DOWN SIDE
						down = (down == false ? true : down);
					}
					
				}else if(item.getName() == Items.PLAYER) {
					//jeœli kamieñ spadnie na layera
					if (sYd == iYu && ((iXl + 2 > sXl && iXl + 2 < sXr) || (iXr - 2 > sXl && iXr - 2 < sXr))) {
						down = (down == false ? true : down);
					}
					
				}else if (item.getName() == Items.RED_DIAMOND || item.getName() == Items.GREEN_DIAMOND
						|| item.getName() == Items.BLUE_DIAMOND || item.getName() == Items.YELLOW_DIAMOND) {
					
					if (doesCollide(item, sBounds)) {
						diamonds.remove(item);
						map.remove(item);
						updateLevel();
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
		
// -----------------------------------------
//					PLAYER
//-----------------------------------------
	private void playerCollisions() {
		// Player bounds
		double pXl = player.getBody().getX();
		double pXr = player.getBody().getX() + RECT_SIZE;
		double pYu = player.getBody().getY();
		double pYd = player.getBody().getY() + RECT_SIZE;
		Bounds pBounds = new BoundingBox(pXl + boundShift, pYu + boundShift, RECT_SIZE - boundShift * 2, RECT_SIZE - boundShift * 2);

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
				
				if(pXl == iXr && pYu == iYu) {
					if(item.getCollision().equals("DOWN_LEFT"))
						left = (left == false ? true : left);
				}else if((pXl > iXl && pXl < iXr ) && (pYu+boundShift > iYu && pYu+boundShift <iYd)) { // 1. LEFT SIDE
					item.move("LEFT");
					left = (left == false ? true : left);
				}else if(pXr == iXl && pYu == iYu) {
					if(item.getCollision().equals("DOWN_RIGHT"))
						right = (right == false ? true : right);
				}else if((pXr > iXl && pXr < iXr) && (pYu+boundShift > iYu && pYu+boundShift <iYd)) { // 2. RIGHT SIDE
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
					player.takeDiamond(item);
					setScore(item);
					diamonds.remove(item);
					map.remove(item);
					updateLevel();
				}
				
			}else if(item.getName() == Items.DOOR) {
				
				if (doesCollide(item, pBounds)) 
					System.out.println("door");
				
			}

		} // collision loop

		if ((!left && !right) && (!up && !down)) {
			player.setCollision("NONE");
		} else if ((left && right) && down) {
			player.setCollision("WALL_LEFT_RIGHT_DOWN");
		} else if ((left && up) && down) {
			player.setCollision("WALL_LEFT_UP_DOWN");
		} else if ((left && up) && right) {
			player.setCollision("WALL_LEFT_UP_RIGHT");
		} else if ((up && right) && down) {
			player.setCollision("WALL_UP_RIGHT_DOWN");
		} else if (left && up) {
			player.setCollision("WALL_LEFT_UP");
		} else if (left && right) {
			player.setCollision("WALL_LEFT_RIGHT");
		} else if (left && down) {
			player.setCollision("WALL_LEFT_DOWN");
		} else if (up && right) {
			player.setCollision("WALL_UP_RIGHT");
		} else if (right && down) {
			player.setCollision("WALL_RIGHT_DOWN");
		} else if (up && down) {
			player.setCollision("WALL_UP_DOWN");
		} else if (left) {
			player.setCollision("WALL_LEFT");
		} else if (up) {
			player.setCollision("WALL_UP");
		} else if (right) {
			player.setCollision("WALL_RIGHT");
		} else if (down) {
			player.setCollision("WALL_DOWN");
		}
	}

	private void playerMove() {
		KeyCode code = keyEvent.getCode();
		if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT)
			player.move(keyEvent);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isGamePaused() {
		return pause;
	}
	
	private void setScore(Item diamond) {
		if(diamond.getName() == Items.GREEN_DIAMOND) 
			score += 75;
		else if(diamond.getName() == Items.YELLOW_DIAMOND)
			score += 150;
		else if(diamond.getName() == Items.BLUE_DIAMOND)
			score += 225;
		else if(diamond.getName() == Items.RED_DIAMOND)
			score += 300;
		GameData.setScore(score);
		scoreLabel.setTextFill(Color.CHARTREUSE);
		scoreLabel.setText(String.valueOf(score));
	}

	public void setDifficulty() {
		timeToCount = GameData.getDifficulty();
		timeLabel.setTextFill(Color.CHARTREUSE);
		timeLabel.setText(String.valueOf(timeToCount));
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
			
			if(timeToCount < 10)
				timeLabel.setTextFill(Color.RED);
			else if(timeToCount < GameData.getDifficulty()/2)
				timeLabel.setTextFill(Color.YELLOW);
			timeLabel.setText(String.valueOf(timeToCount));
		}

		if (millisecondsSum > 1000) {

			timeToCount--;

			if (timeToCount == 0) {
				timeLabel.setText("");
				statusLabel.setTextFill(Color.RED);
				statusLabel.setText("Time out");
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
