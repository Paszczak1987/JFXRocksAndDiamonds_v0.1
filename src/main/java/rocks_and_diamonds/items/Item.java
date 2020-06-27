package rocks_and_diamonds.items;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Item implements Comparable<Item> {

	private Items name;
	private int size;
	private Rectangle body;
	private Image skin;	
	
	public Item(int size) {
		name = Items.PLAYER;
		this.size = size;
		skin = new Image("Pictures/Player/adventurer_stand.png", size, size, false, false);
		body = new Rectangle(size, size);
		body.setFill(new ImagePattern(skin));
	}
	
	public Item(Items name, int size, int x, int y) {
		this.name = name;
		this.size = size;
		body = new Rectangle(size, size);
		body.setX(x*size);
		body.setY(y*size);
		if (this.name == Items.PLAYER) {
			skin = new Image("Pictures/Player/adventurer_stand.png", size, size, false, false);
		}else if(this.name == Items.WALL) {
			skin = new Image("Pictures/Textures/brick_grey.png", size, size, false, false);
		}else if(this.name == Items.DIRT) {
			skin = new Image("Pictures/Textures/dirt.png", size, size, false, false);
		}
		body.setFill(new ImagePattern(skin));
	}

	public Items getName() {
		return name;
	}

	public Rectangle getBody() {
		return body;
	}
	
	public void setXY(int x, int y) {
		body.setX(x*size);
		body.setY(y*size);
	}

	@Override
	public int compareTo(Item o) {
		if (name == o.getName())
			return 0;
		else
			return 1;
	}
}
