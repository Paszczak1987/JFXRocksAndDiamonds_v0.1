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

public class Item implements Comparable<Item> {

	private Items 		name;
	private int 		size;
	private List<Image>	textures;
	private Image 		skin;
	private Rectangle 	body;
	
	private Timeline 	timeline;
	private KeyCode		direction; 
	private final int 	dx;
	private final int 	dy;
	private int 		frameCounter;
	
	{
		textures = new ArrayList<Image>();
		timeline = new Timeline(new KeyFrame(Duration.millis(4), this::doStep));
		dx = 1;
		dy = 1;
		frameCounter = 0;
	}
	
	public Item(int size) {
		name = Items.PLAYER;
		this.size = size;
		textures.add(new Image("Pictures/Player/adventurer_stand.png", size, size, false, false));
		textures.add(new Image("Pictures/Player/adventurer_walk1.png", size, size, false, false));
		textures.add(new Image("Pictures/Player/adventurer_walk2.png", size, size, false, false));
		skin = textures.get(0);
		body = new Rectangle(size, size);
		body.setFill(new ImagePattern(skin));
		timeline.setCycleCount(size);
	}

	public Item(Items name, int size, int x, int y) {
		this.name = name;
		this.size = size;
		if (this.name == Items.PLAYER) {
			textures.add(new Image("Pictures/Player/adventurer_stand.png", size, size, false, false));
		} else if (this.name == Items.WALL) {
			textures.add(new Image("Pictures/Textures/brick_grey.png", size, size, false, false));
		} else if (this.name == Items.DIRT) {
			textures.add(new Image("Pictures/Textures/dirt.png", size, size, false, false));
		}
		skin = textures.get(0);
		body = new Rectangle(size, size);
		setPosition(x, y);
		body.setFill(new ImagePattern(skin));
		timeline.setCycleCount(size);
	}

	public Items getItem() {
		return name;
	}

	public Rectangle getBody() {
		return body;
	}
	
	public KeyCode getDirection() {
		return direction;
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
	
	//Metoda wywoywana przez Timeline co ka¿dy cykl
	private void doStep(ActionEvent actionEvent) {
		
		frameCounter++;
		
		if(name == Items.PLAYER) {
			//Jeœli to PLAYER
			if(direction == KeyCode.LEFT)
				body.setX(body.getX() - dx);			
			else if(direction == KeyCode.RIGHT)
				body.setX(body.getX() + dx);						
			else if(direction == KeyCode.UP)
				body.setY(body.getY() - dy);
			else if(direction == KeyCode.DOWN)
				body.setY(body.getY() + dy);
			
			animate(frameCounter);
		}else {
			//Jeœli coœ INNEGO	
			System.out.println("nie jestem PLAYER");
		}
	}
	
	private void afterSteps(ActionEvent actionEvent) {
		skin = textures.get(0);
		body.setFill(new ImagePattern(skin));
	}
	
	private void animate(int frameCounter) {
		
		if(direction == KeyCode.LEFT)
			body.setScaleX(-1);
		else if(direction == KeyCode.RIGHT)
			body.setScaleX(1);
		
		frameCounter = (frameCounter == size ? 0 : frameCounter);
		
		if(frameCounter%22 == 0) {
			if(skin.equals(textures.get(0)))
				skin = textures.get(1);
			else
				if(skin.equals(textures.get(1)))
					skin = textures.get(2);
				else
					skin = textures.get(1);
			body.setFill(new ImagePattern(skin));
		}
	}
	
	public void move(KeyEvent e) {
		if(timeline.getStatus() == Animation.Status.STOPPED)
			direction = e.getCode();		
		if(e.getEventType() == KeyEvent.KEY_PRESSED) {
			timeline.play();
			timeline.setOnFinished(null);
		}
		if(e.getEventType() == KeyEvent.KEY_RELEASED)
			timeline.setOnFinished(this::afterSteps);
	}

	@Override
	public int compareTo(Item o) {
		if (name == o.getItem())
			return 0;
		else
			return 1;
	}

}// Item
