package rocks_and_diamonds.items;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;

public class Player extends Item {
	
	private KeyCode direction;
	
	public Player(int size) {
		this.size = size;
		name = Items.PLAYER;
		textures.add(new Image("Pictures/Player/adventurer_stand.png", size, size, false, false));
		textures.add(new Image("Pictures/Player/adventurer_walk1.png", size, size, false, false));
		textures.add(new Image("Pictures/Player/adventurer_walk2.png", size, size, false, false));
		skin = textures.get(0);
		body = new Rectangle(size,size);
		body.setFill(new ImagePattern(skin));
		//body.setViewOrder(1);
		timeline = new Timeline(new KeyFrame(Duration.millis(4), this::doStep));
		timeline.setCycleCount(size);
	}

	public KeyCode getDirection() {
		return direction;
	}
	
	// Metoda wywoywana przez Timeline co ka¿dy cykl
	protected void doStep(ActionEvent actionEvent) {

		frameCounter++;
		
		// zmiana kierunku Teksturki
		if (direction == KeyCode.LEFT)
			body.setScaleX(-1);
		else if (direction == KeyCode.RIGHT)
			body.setScaleX(1);
		
		if (direction == KeyCode.LEFT)
			body.setX(body.getX() - dx);
		else if (direction == KeyCode.RIGHT)
			body.setX(body.getX() + dx);
		else if (direction == KeyCode.UP)
			body.setY(body.getY() - dy);
		else if (direction == KeyCode.DOWN)
			body.setY(body.getY() + dy);
		
		animate();

	}
	
	public void animate() {

		frameCounter = (frameCounter == size ? 0 : frameCounter);

		if (frameCounter % (size / 2) == 0) {
			if (skin.equals(textures.get(0)))
				skin = textures.get(1);
			else if (skin.equals(textures.get(1)))
				skin = textures.get(2);
			else
				skin = textures.get(1);
			body.setFill(new ImagePattern(skin));
		}
	}
	
	private void afterAnimation(ActionEvent actionEvent) {
		setDefaultSkin();
	}

	public void changeTextures(String character) {
		textures.clear();
		if(character.equals("MALE")) {
			textures.add(new Image("Pictures/Player/adventurer_stand.png", size, size, false, false));
			textures.add(new Image("Pictures/Player/adventurer_walk1.png", size, size, false, false));
			textures.add(new Image("Pictures/Player/adventurer_walk2.png", size, size, false, false));
		}else if(character.equals("FEMALE")) {
			textures.add(new Image("Pictures/Player/female_stand.png", size, size, false, false));
			textures.add(new Image("Pictures/Player/female_walk1.png", size, size, false, false));
			textures.add(new Image("Pictures/Player/female_walk2.png", size, size, false, false));
		}
		setDefaultSkin();
	}
	
	private void executeMove(KeyEvent keyEvent) {
		if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
			timeline.play();
			timeline.setOnFinished(null);
		} else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
			if (timeline.getStatus() == Animation.Status.RUNNING)
				timeline.setOnFinished(this::afterAnimation);
			else
				setDefaultSkin();
		}
	}

	public void move(KeyEvent keyEvent, String collision) {
		// blokada zmiany kierunku przed koñcem animacji
		if (timeline.getStatus() == Animation.Status.STOPPED)
			direction = keyEvent.getCode();

		if (collision.equals("NONE")) {
			executeMove(keyEvent);
		} else if (collision.equals("WALL_LEFT")) {
			if (direction != KeyCode.LEFT)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_UP")) {
			if (direction != KeyCode.UP)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_RIGHT")) {
			if (direction != KeyCode.RIGHT)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_DOWN")) {
			if (direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_UP")) {
			if (direction != KeyCode.LEFT && direction != KeyCode.UP)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_RIGHT")) {
			if (direction != KeyCode.LEFT && direction != KeyCode.RIGHT)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_DOWN")) {
			if (direction != KeyCode.LEFT && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_UP_RIGHT")) {
			if (direction != KeyCode.UP && direction != KeyCode.RIGHT)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_RIGHT_DOWN")) {
			if (direction != KeyCode.RIGHT && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_UP_DOWN")) {
			if (direction != KeyCode.UP && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_RIGHT_DOWN")) {
			if ((direction != KeyCode.LEFT && direction != KeyCode.RIGHT) && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_UP_DOWN")) {
			if ((direction != KeyCode.LEFT && direction != KeyCode.UP) && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_LEFT_UP_RIGHT")) {
			if ((direction != KeyCode.LEFT && direction != KeyCode.UP) && direction != KeyCode.RIGHT)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		} else if (collision.equals("WALL_UP_RIGHT_DOWN")) {
			if ((direction != KeyCode.UP && direction != KeyCode.RIGHT) && direction != KeyCode.DOWN)
				executeMove(keyEvent);
			else
				setDefaultSkin();
		}

	}
	
}// Player
