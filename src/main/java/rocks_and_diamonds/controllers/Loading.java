package rocks_and_diamonds.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import rocks_and_diamonds.GameState;
import rocks_and_diamonds.GameStates;

public class Loading extends StateController {
	
	private GameState parent;
	@FXML
	private StackPane Stackpane;
	
	@FXML
	private Label Label;
	
	@FXML
	private Circle Circle1;
	
	@FXML
	private Circle Circle2;
	
	@FXML
	private Circle Circle3;
	
	@FXML 
	private MediaView MediaView;	
	private static final String MEDIA_URL = "/Pictures/Diamond.mp4";
	private MediaPlayer mediaPlayer;
	
	@FXML
<<<<<<< HEAD
	public void initialize() {
		System.out.println(this.getClass().getResource(MEDIA_URL).toExternalForm());
	 	mediaPlayer = new MediaPlayer(new Media(this.getClass().getResource(MEDIA_URL).toExternalForm()));
	 	mediaPlayer.setAutoPlay(true);
	 	MediaView.setMediaPlayer(mediaPlayer);
//	 	 new Bounce(Circle1).setCycleCount(5).setDelay(Duration.valueOf("1000ms")).play();
//	     new Bounce(Circle2).setCycleCount(5).setDelay(Duration.valueOf("1250ms")).play();
//	     new Bounce(Circle3).setCycleCount(5).setDelay(Duration.valueOf("1500ms")).play();
	 	}
=======
	public void initialize() throws InterruptedException {
		label.setFocusTraversable(true);
	}
>>>>>>> 9864f3563245b50298305248bc3f16e0af11ad82
	
	public void labelKeyPressed(KeyEvent e) {
		System.out.println("LOADING");
		if(e.getCode() == KeyCode.SPACE )
			parent.mainWindow().changeState(GameStates.MENU);
	}
	
	@Override
	public void setParent(GameState parent) {
		this.parent = parent;
	}

	@Override
	public void handle(long now) {
		// TODO Auto-generated method stub
	}

}
