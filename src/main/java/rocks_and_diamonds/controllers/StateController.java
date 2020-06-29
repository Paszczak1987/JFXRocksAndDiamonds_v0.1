package rocks_and_diamonds.controllers;

import javafx.animation.AnimationTimer;
import rocks_and_diamonds.GameState;

public abstract class StateController extends AnimationTimer {
	public abstract void setParent(GameState parent);
	public abstract void handle(long now);
}
