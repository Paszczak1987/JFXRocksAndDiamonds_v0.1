package rocks_and_diamonds.items;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
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
	protected String collision;
	private String direction;
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
		this.direction = "NONE";
		this.collision = "NONE";
		if (this.name == Items.WALL) {
			textures.add(new Image("Pictures/Textures/brick_grey.png", size, size, false, false));
		}else if(this.name == Items.GREY_WALL) {
			textures.add(new Image("Pictures/Textures/greystone_default.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/greystone_crushed_01.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/greystone_crushed_02.png", size, size, false, false));
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(74), this::doStep));
			timeline.setCycleCount(4);
		} else if (this.name == Items.DIRT) {
			textures.add(new Image("Pictures/Textures/dirt_default.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/dirt_crushed_01.png", size, size, false, false));
			textures.add(new Image("Pictures/Textures/dirt_crushed_02.png", size, size, false, false));
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(74), this::doStep));
			timeline.setCycleCount(4);
		} else if (this.name == Items.RED_DIAMOND) {
			textures.add(new Image("Pictures/Textures/redD.png", size, size, false, false));
		} else if (this.name == Items.GREEN_DIAMOND) {
			textures.add(new Image("Pictures/Textures/greenD.png", size, size, false, false));
		} else if (this.name == Items.BLUE_DIAMOND) {
			textures.add(new Image("Pictures/Textures/blueD.png", size, size, false, false));
		} else if (this.name == Items.YELLOW_DIAMOND) {
			textures.add(new Image("Pictures/Textures/yellowD.png", size, size, false, false));
		}else if (this.name == Items.STONE) {
			textures.add(new Image("Pictures/Textures/rock.png", size, size, false, false));
			timeline = new Timeline(new KeyFrame(Duration.millis(4), this::doStep));
			timeline.setCycleCount(size);
		}else if (this.name == Items.DOOR) {
			textures.add(new Image("Pictures/Textures/door.png", size, size, false, false));
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

	// Metoda wywo³ywana przez Timeline co ka¿dy cykl
	protected void doStep(ActionEvent actionEvent) {
		if (this.name == Items.DIRT || this.name == Items.GREY_WALL) {
			frameCounter++;
			timeline.setOnFinished(this::afterAnimation);
			animate();
		} else if (this.name == Items.STONE) {
			timeline.setOnFinished(this::afterAnimation);
			if(direction.equals("LEFT")) {
				body.setX(body.getX() - dx);
			}else if (direction.equals("RIGHT")) {
				body.setX(body.getX() + dx);
			}else if(direction.equals("DOWN")) {
				body.setY(body.getY() + dy);
			}
		}
	}

	public void move(String dir) {
		this.direction = dir;
		if(collision.equals("NONE")) {
			playAnimation();	
		}else if(collision.equals("DOWN")) {
			if(!direction.equals("DOWN"))
				playAnimation();				
		}else if(collision.equals("LEFT")) {
			if(!direction.equals("LEFT"))
				playAnimation();
		}else if(collision.equals("RIGHT")) {
			if(!direction.equals("RIGHT"))
				playAnimation();
		}else if(collision.equals("LEFT_RIGHT")) {
			if(!direction.equals("LEFT") && !direction.equals("RIGHT"))
				playAnimation();
		}
	}
	
	public void setCollision(String side) {
		collision = side;
	}
	
	public String getCollision() {
		return collision;
	}
	
	public String getDir() {
		return direction;
	}
	
	public void setDefaultSkin() {
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
		isAnimationStarted = false;
	}
	
	public void playAnimation() {
		if(isAnimationStarted == false) {
			timeline.play();
			isAnimationStarted = true;
		}
	}

}// Item
