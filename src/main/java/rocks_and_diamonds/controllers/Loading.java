package rocks_and_diamonds.controllers;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Loading extends StateController {

	private GameState parent;

	private int timeToCount;
	private long lastTime;
	private long frameTime;
	private int msSum;

	@FXML
	private Circle circle1;
	@FXML
	private Circle circle2;
	@FXML
	private Circle circle3;
	@FXML
	private MediaView mediaView;
	private MediaPlayer mediaPlayer;

	@FXML
	public void initialize() {

		this.timeToCount = 3;

		mediaPlayer = new MediaPlayer(new Media(this.getClass().getResource("/Pictures/Diamond.mp4").toExternalForm()));
		mediaView.setMediaPlayer(mediaPlayer);

	}
	
	@Override
	public void play() {
		new Bounce(circle1).setCycleCount(5).play();
		new Bounce(circle2).setCycleCount(5).setDelay(Duration.valueOf("250ms")).play();
		new Bounce(circle3).setCycleCount(5).setDelay(Duration.valueOf("500ms")).play();
		mediaPlayer.play();
	}

	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void handle(long now) {
		if (lastTime == 0) {
			lastTime = System.currentTimeMillis();
			return;
		}

		msSum += frameTime;

		if (msSum > 1000) {

			timeToCount--;

			if (timeToCount == -1) {
				parent.mainWindow().changeState(GameStates.MENU);
				stop();
				return;
			}

			msSum = 0;
		}

		frameTime = System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
	}

}
