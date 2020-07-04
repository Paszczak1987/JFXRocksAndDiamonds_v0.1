package rocks_and_diamonds.controllers;

import javafx.animation.AnimationTimer;
import rocks_and_diamonds.GameState;

public abstract class StateController extends AnimationTimer {
	static final int RECT_SIZE = 44;
	public abstract void setParent(GameState parent);
	public abstract void handle(long now);
	public abstract void play(); //Start animacji np. w Loading.java
}
