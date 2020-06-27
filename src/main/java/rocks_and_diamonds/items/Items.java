package rocks_and_diamonds.items;

import javafx.scene.paint.Color;

public enum Items {
	PLAYER(255, 255, 255),
	VOID(0, 0, 0),
	DIRT(130, 65, 0),
	WALL(150, 150, 150);
	
	private Color color;
	
	private Items(int r, int g, int b) {
		this.color = Color.rgb(r, g, b);
	}
	
	public Color getColor() {
		return color;
	}
}
