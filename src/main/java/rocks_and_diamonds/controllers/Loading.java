package rocks_and_diamonds.controllers;

import animatefx.animation.Bounce;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;


public class Loading extends StateController {
	
	private int 	timeToCount;
	private long 	lastTime;
	private long 	frameTime;
	private int 	msSum;
	
	private GameState parent;
	@FXML
	private StackPane stackPane;	
	@FXML
	private Label label;
	@FXML
	private Circle circle1;
	@FXML
	private Circle circle2;
	@FXML
	private Circle circle3;
	@FXML 
	private MediaView mediaView;	
	
	private MediaPlayer mediaPlayer;
	private String MEDIA_URL = "/Pictures/Diamond.mp4";
	
	
	@FXML

	public void initialize() {
		
		this.timeToCount = 4;
		
		{//dodanie Eventhandlera tymczasowo	
			label.setFocusTraversable(true);
			EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					labelKeyPressed(e);
				}
				
			};
			label.addEventHandler(KeyEvent.KEY_PRESSED, handler);
		}//dodanie Eventhandlera tymczasowo		
		
	 	mediaPlayer = new MediaPlayer(new Media(this.getClass().getResource(MEDIA_URL).toExternalForm()));
	 	mediaPlayer.setAutoPlay(true);
	 	mediaView.setMediaPlayer(mediaPlayer);
	 	
	 	new Bounce(circle1).setCycleCount(5).setDelay(Duration.valueOf("3000ms")).play();
	    new Bounce(circle2).setCycleCount(5).setDelay(Duration.valueOf("3250ms")).play();
	    new Bounce(circle3).setCycleCount(5).setDelay(Duration.valueOf("3500ms")).play();
	 	
	}

	
	public void labelKeyPressed(KeyEvent e) {
		System.out.println("LOADING");
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
		
			--timeToCount;
			
			if(timeToCount == -1) {
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
