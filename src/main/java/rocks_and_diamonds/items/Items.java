package rocks_and_diamonds.items;

import javafx.scene.paint.Color;

public enum Items {
	PLAYER(255, 255, 255),
	VOID(0, 0, 0),
	DIRT(130, 65, 0),
	WALL(150, 150, 150),
	GREY_WALL(200,200,200),
	RED_DIAMOND(255,0,0),
	GREEN_DIAMOND(0,255,0),
	BLUE_DIAMOND(0,0,255),
	YELLOW_DIAMOND(255,255,0),
	DOOR(255,10,255),
	STONE(0,200,255);
	
	private Color color;
	
	private Items(int r, int g, int b) {
		this.color = Color.rgb(r, g, b);
	}
	
	public Color getColor() {
		return color;
	}
}
