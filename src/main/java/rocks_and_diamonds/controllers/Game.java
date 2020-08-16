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
	private int countdownToNextLevel;
	private long lastTime;
	private long frameTime;
	private int millisecondsSum;

	// Game fields
	private List<Item> map;
	private List<Item> stones;
	private int stoneToRemove;
	private List<Item> diamonds;
	private Player player;
	private int levelNr;
	private int score;
	private boolean pause;
	private boolean nextLevel;
	private boolean endGame;
	private KeyEvent keyEvent;
	private double boundShift;
	private boolean defaultMessageFlag;
	private String defaultMessage;
	
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

		this.levelNr = 1;
		this.pause = true;
		this.nextLevel = false;
		this.endGame = false;
		this.boundShift = 2;

		player = new Player(RECT_SIZE);
		map = new ArrayList<Item>();
		stones = new ArrayList<Item>();
		diamonds = new ArrayList<Item>();
		
		loadLevel();
		stoneToRemove = stones.size();
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
		//countdownToNextLevel = 20;

		defaultMessage = "[Enter] to start\n[P] to pause/unpause\n[ESC] to main menu\n[Q] to abandon";
		
		lvlLabel.setTextFill(Color.GOLD);
		
		scoreLabel.setTextFill(Color.CHARTREUSE);
		scoreLabel.setText(String.valueOf(score));
		
		statusLabel.setTextFill(Color.GOLD);
		statusLabel.setText("Ready");
		
		diamondsLabel.setTextFill(Color.MAGENTA);
		diamondsLabel.setText("");
		
		defaultMessageFlag = true;
		setDefaultMessage(Color.CYAN, defaultMessage);
		message.setWrapText(true);
	}

// -----------------------------------------
//					CONTROLS
//-----------------------------------------	
	public void gamePaneOnKeyPressed(KeyEvent e) {

		this.keyEvent = e;

		// -----------------------------------------
		// playerMove() jest wywolywany w petli gry
		// -----------------------------------------

		if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
			
			if(GameData.gameIsGoing == false) {
				GameData.gameIsGoing = true;
				Options options = (Options)(parent.mainWindow().getGameState(GameStates.OPTIONS).getController());
				options.disableDifficultyValues(true);
				pauseOrPlay(true);
			}
			
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.P) {
			
			if(GameData.gameIsGoing && nextLevel == false)
				if(!pause)
					pauseOrPlay(false);
				else
					pauseOrPlay(true);
		}else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.Q) {
			stop();
			resetGame();
			Options options = (Options)(parent.mainWindow().getGameState(GameStates.OPTIONS).getController());
			options.disableDifficultyValues(false);
			Menu menu = (Menu)(parent.mainWindow().getGameState(GameStates.MENU).getController());
			menu.setNewGamebtnTxt("New Game");
			parent.mainWindow().changeState(GameStates.MENU);
		} else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ESCAPE) {
			
			if(GameData.gameIsGoing && nextLevel == false)
				pauseOrPlay(false);
			if(endGame) {
				GameData.standingAddAndSort();
				GameData.writeToFile();
				HallOfFame hallOfFame = (HallOfFame)(parent.mainWindow().getGameState(GameStates.HALLOFFAME).getController());
				hallOfFame.update();
				resetGame();
				Options options = (Options)(parent.mainWindow().getGameState(GameStates.OPTIONS).getController());
				options.disableDifficultyValues(false);
				Menu menu = (Menu)(parent.mainWindow().getGameState(GameStates.MENU).getController());
				menu.setNewGamebtnTxt("New Game");
			}
			parent.mainWindow().changeState(GameStates.MENU) ;
		
		} else if(e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.G) {
//			System.out.println("stones: "+stones.size()+" remove:"+stoneToRemove);
//			System.out.println("P:"+player.howMuchDiamondsHave()+" M:"+diamonds.size());
//			GameData.readFromFile();
		}

	}

	public void pauseOrPlay(boolean play) {
		if (timeToCount > 0) {
			if (play == false) {
				pause = true;
				statusLabel.setTextFill(Color.TOMATO);
				statusLabel.setText("Paused");
				stop();
			} else {
				if (timeToCount > 0) {
					pause = false;
					statusLabel.setTextFill(Color.PALEGREEN);
					statusLabel.setText("Playing");
					start();
				}
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
		}else if (color.equals(Items.GREY_WALL.getColor())) {
			item = new Item(Items.GREY_WALL, RECT_SIZE, x, y); 
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
		
		if(stoneToRemove < stones.size()) {
			stones.remove(stoneToRemove);
			stoneToRemove = stones.size();
		}
		
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
				
				if (map.get(i).equals(stone)) // wykluczamy kamien dla ktorego sprawdzamy kolizje
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
					
				}else if(item.getName() == Items.GREY_WALL) {
					
					if (doesCollide(item, sBounds)) {
						item.playAnimation();
						stone.getBody().setOpacity(0.0d);
						if (item.getAnimationStatus()) {
							setScore(item);
							map.remove(item);
							map.remove(stone);	
							stoneToRemove = stones.indexOf(stone);
						}
						updateLevel();
					}
					
				}else if(item.getName() == Items.PLAYER) {
					//jesli kamien spadnie na playera
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

			if (item.getName() == Items.WALL || item.getName() == Items.GREY_WALL) { // Jesli WALL
				
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
					if (item.getAnimationStatus()) {
						setScore(item);
						map.remove(item);
					}
					updateLevel();
				}
				
			}else if(item.getName() == Items.STONE) {// Jesli STONE
				
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

			} else if (item.getName() == Items.RED_DIAMOND || item.getName() == Items.GREEN_DIAMOND // Jesli DIAMOND
					|| item.getName() == Items.BLUE_DIAMOND || item.getName() == Items.YELLOW_DIAMOND) {
				
				if (doesCollide(item, pBounds)) {
					player.takeDiamond(item);
					diamondsLabel.setText(diamondsLabel.getText()+"| ");
					setScore(item);
					diamonds.remove(item);
					map.remove(item);
					updateLevel();
				}
				
			}else if(item.getName() == Items.DOOR) {
				
				if (doesCollide(item, pBounds)) {
					if(defaultMessageFlag == true) {
						defaultMessageFlag = false;
						if(player.getDiamonds().size() < 2) {
							setDefaultMessage(Color.RED, "You need to collect at least two diamonds");				
						}else {										
							//player.getAnimation().stop();
							if(timeToCount < 20)
								countdownToNextLevel = 20;
							else
								countdownToNextLevel = timeToCount;
							nextLevel = true;
						}
						
					}
				} else {
					if(defaultMessageFlag == false)
						defaultMessageFlag = true;
						setDefaultMessage(Color.CYAN, defaultMessage);
				}
					
				
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
		if(GameData.gameIsGoing) {
			if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT)
				player.move(keyEvent);			
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isGamePaused() {
		return pause;
	}
	
	private void setDefaultMessage(Color c, String message) {
		this.message.setTextFill(c);
		this.message.setText(message);
	}
	
	public void resetGame() {
		
		if(levelNr != 1)
			levelNr = 1;
		pause = true;
		endGame = false;
		score = 0;
		GameData.setScore(score);
		
		map.clear();
		stones.clear();
		diamonds.clear();
		
		loadLevel();
		stoneToRemove = stones.size();
		updateLevel();

		setDifficulty();
		GameData.gameIsGoing = false;
		nextLevel = false;
		
		scoreLabel.setText(String.valueOf(score));
		
		statusLabel.setTextFill(Color.GOLD);
		statusLabel.setText("Ready");

		diamondsLabel.setText("");
	
		setDefaultMessage(Color.CYAN, defaultMessage);
		player.getDiamonds().clear();
		
	}
	
	private void nextLevel() {
		levelNr++;
		
		map.clear();
		stones.clear();
		diamonds.clear();
		player.setDefaultSkin();
		
		loadLevel();
		pause = true;
		endGame = false;
		stoneToRemove = stones.size();
		updateLevel();			

		setDifficulty();
		GameData.gameIsGoing = false;
		nextLevel = false;	
		
		statusLabel.setTextFill(Color.GOLD);
		statusLabel.setText("Ready");

		diamondsLabel.setText("");
	
		setDefaultMessage(Color.CYAN, defaultMessage);
		player.getDiamonds().clear();
		
	}
	
	private void setScore(Item item) {
		if(item == null)
			score+=15;
		else if(item.getName() == Items.GREEN_DIAMOND) 
			score += 75;
		else if(item.getName() == Items.YELLOW_DIAMOND)
			score += 150;
		else if(item.getName() == Items.BLUE_DIAMOND)
			score += 225;
		else if(item.getName() == Items.RED_DIAMOND)
			score += 300;
		else if(item.getName() == Items.GREY_WALL)
			score += 5;
		else if(item.getName() == Items.DIRT)
			score += 1;
		
		GameData.setScore(score);
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
			
			if(nextLevel == false)
				playerMove();
			
			{//zmiana koloru licznika czasu
				if(timeToCount < 10)
					timeLabel.setTextFill(Color.RED);
				else if(timeToCount < GameData.getDifficulty()/2)
					timeLabel.setTextFill(Color.YELLOW);
				timeLabel.setText(String.valueOf(timeToCount));				
			}//zmiana koloru licznika czasu
			
			{//Zmiana instrukcji jesli mamy dwa diamenty lub gdy juz dwoch nie uzbieramy
				if((diamonds.size() < 2 && player.getDiamonds().size() < 1 ) || (diamonds.size() < 1 && player.getDiamonds().size() < 2)) {
					statusLabel.setTextFill(Color.TOMATO);
					statusLabel.setText("Gems lack");
					setDefaultMessage(Color.RED, "Game Over\n[ESC] to main menu");
					stop();
				}else if(player.getDiamonds().size() > 1) {
					setDefaultMessage(Color.CYAN, defaultMessage+"\nNow you can go to the next level");
				}
			}//Zmiana instrukcji jesli mamy dwa diamenty lub gdy juz dwoch nie uzbieramy
			
		}
		
		if(nextLevel == false) {
			//Obecny poziom
			if (millisecondsSum > 1000) {
				
				timeToCount--;
				
				if (timeToCount == 0) {
					timeLabel.setText("0");
					statusLabel.setTextFill(Color.TOMATO);
					statusLabel.setText("Time out");
					setDefaultMessage(Color.RED, "Game Over\n[ESC] to main menu");
					endGame = true;
					stop();
				}
				
				millisecondsSum = 0;
			}		
		}else {
			//Nastêpny poziom
			
			statusLabel.setTextFill(Color.PALEGREEN);
			statusLabel.setText("Next level");
			setDefaultMessage(Color.CHARTREUSE, "Good job! You are going to the next level.");	
			
			if(millisecondsSum > 100) {
				
				timeToCount = timeToCount > 0 ? timeToCount - 1 : 0;
				countdownToNextLevel = countdownToNextLevel > 0 ? countdownToNextLevel - 1 : 0;
				
				if(timeToCount > 0) {
					setScore(null);
				}
				
				if(timeToCount == 0 && countdownToNextLevel == 0) {
					timeLabel.setText("0");
					stop();
					if(levelNr < 4)
						nextLevel();
					else {
						statusLabel.setTextFill(Color.PALEGREEN);
						statusLabel.setText("Completed");
						setDefaultMessage(Color.CHARTREUSE, "You have beat the game.\n[ESC] to main menu");
						endGame = true;
						stop();
					}
				}
				
				millisecondsSum = 0;
				
			}
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
