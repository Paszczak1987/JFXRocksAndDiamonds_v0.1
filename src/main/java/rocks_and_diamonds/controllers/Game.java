package rocks_and_diamonds.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
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

public class Game extends AnimationTimer implements StateController {
	private static final int RECT_SIZE = 44;
	private GameState parent;
	private int levelNr;
	private Item player;
	private List<Rectangle> map;
	@FXML
	private Pane gamePane;
	@FXML
	private VBox sideDisplay;
	@FXML
	private Label label;

	@FXML
	public void initialize() {
		levelNr = 0;
		player = new Item(RECT_SIZE);
		map = new ArrayList<Rectangle>();
		gamePane.setStyle("-fx-background-color: #000000");

		loadLevel();
		displayLevel();

		label.setFocusTraversable(true);
	}

	public void labelKeyPressed(KeyEvent e) {
		System.out.println("GAME");
		if (e.getCode() == KeyCode.SPACE)
			parent.mainWindow().changeState(GameStates.QUIT);
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

	private void updateMap(Color color, int x, int y) {
		Item item;
		if (color.equals(Items.PLAYER.getColor())) {

			player.setXY(x, y);
			map.add(player.getBody());

		} else if (color.equals(Items.WALL.getColor())) {

			item = new Item(Items.WALL, RECT_SIZE, x, y);
			map.add(item.getBody());

		} else if (color.equals(Items.DIRT.getColor())) {

			item = new Item(Items.DIRT, RECT_SIZE, x, y);
			map.add(item.getBody());

		} else {

			Rectangle rectangle = new Rectangle(RECT_SIZE, RECT_SIZE);
			rectangle.setFill(color);
			rectangle.setX(x*RECT_SIZE);
			rectangle.setY(y*RECT_SIZE);
			map.add(rectangle);

		}
	}

	public void displayLevel() {
		for (Rectangle rec : map) {
			gamePane.getChildren().add(rec);
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

}
