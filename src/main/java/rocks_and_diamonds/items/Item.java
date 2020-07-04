package rocks_and_diamonds.items;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Item {
	
	protected Items name;
	protected int size;
	protected List<Image> textures;
	protected Image skin;
	protected Rectangle body;

	protected Timeline timeline;
	protected final int dx;
	protected final int dy;
	protected int frameCounter;
	private boolean isAnimationFinished;
	private boolean isAnimationStarted;

	{
		textures = new ArrayList<Image>();
		timeline = new Timeline();
		dx = 1;
		dy = 1;
		frameCounter = 0;
	}

	public Item() {}

	public Item(Items name, int size, int x, int y) {
		this.name = name;
		this.size = size;
		if (this.name == Items.PLAYER) {
			textures.add(new Image("Pictures/Player/adventurer_stand.png", size, size, false, false));
		} else if (this.name == Items.WALL) {
			textures.add(new Image("Pictures/Textures/brick_grey.png", size, size, false, false));
		} else if (this.name == Items.DIRT) {
			textures.add(new Image("Pictures/Textures/dirt_default.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/dirt_crushed_01.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/dirt_crushed_02.png", size, size, false, false));			
			timeline.setCycleCount(4);
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(76), this::doStep));
		}
		skin = textures.get(0);
		body = new Rectangle(size, size);
		body.setFill(new ImagePattern(skin));
		setPosition(x, y);
	}

	public Items getName() {
		return name;
	}

	public Rectangle getBody() {
		return body;
	}
	
	public boolean getAnimationStatus() {
		return isAnimationFinished;
	}
	
	public Timeline getAnimation() {
		return timeline;
	}

	public void setPosition(int x, int y) {
		body.setX(x * size);
		body.setY(y * size);
	}

	public void setPositionX(int x) {
		body.setX(x * size);
	}

	public void setPositionY(int y) {
		body.setX(y * size);
	}

	// Metoda wywoywana przez Timeline co ka¿dy cykl
	protected void doStep(ActionEvent actionEvent) {
		frameCounter++;
		timeline.setOnFinished(this::afterAnimation);
		animate();
	}

	private void setDefaultSkin() {
		if (!skin.equals(textures.get(0))) {
			skin = textures.get(0);
			body.setFill(new ImagePattern(skin));
		} else
			return;
	}

	private void animate() {
		
		if (frameCounter % 2 != 0) {
			if(skin.equals(textures.get(1)))
				skin = textures.get(2);
			else if(skin.equals(textures.get(0)))
				skin = textures.get(1);
			body.setFill(new ImagePattern(skin));
		}
	}
	
	private void afterAnimation(ActionEvent actionEvent) {
		isAnimationFinished = true;
	}
	
	public void playAnimation() {
		if(isAnimationStarted == false) {
			timeline.play();
			isAnimationStarted = true;
		}
	}

}// Item
