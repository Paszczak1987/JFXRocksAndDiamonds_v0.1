package rocks_and_diamonds;


import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import rocks_and_diamonds.controllers.Game;
import rocks_and_diamonds.controllers.GameData;

public class MainWindow extends Application {

	private Stage appWindow;
	private List<GameState> gameStates;
	private GameState currentState;
	
	@Override
	public void start(Stage window) throws Exception {
		this.appWindow = window;
		{// Listener przy minimalizacji lub przywróceniu okna apki
			window.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> ov, Boolean max, Boolean min) {
					musicAndPauseAtMinimize(max, min);
				}
			});
		} // Listener przy minimalizacji lub przywróceniu okna apki
		
		gameStates = new ArrayList<GameState>();
		gameStates.add(new GameState(this, GameStates.ENTER_PLAYER_NAME, this.getClass().getResource("/FXML/ENTER_PLAYER_NAME.fxml")));
		gameStates.add(new GameState(this, GameStates.LOADING, this.getClass().getResource("/FXML/LOADING.fxml")));
		gameStates.add(new GameState(this, GameStates.MENU, this.getClass().getResource("/FXML/MENU.fxml")));
		gameStates.add(new GameState(this, GameStates.OPTIONS, this.getClass().getResource("/FXML/OPTIONS.fxml")));
		gameStates.add(new GameState(this, GameStates.HALLOFFAME, this.getClass().getResource("/FXML/HALLOFFAME.fxml")));
		gameStates.add(new GameState(this, GameStates.GAME, this.getClass().getResource("/FXML/GAME.fxml")));
		gameStates.add(new GameState(this, GameStates.QUIT, this.getClass().getResource("/FXML/QUIT.fxml")));

		//changeState(GameStates.ENTER_PLAYER_NAME);
		changeState(GameStates.MENU);
		appWindow.getIcons().add(new Image("/Pictures/MainIcon.png"));
		appWindow.setTitle("Rocks & Diamonds by £ukasz & Krzysztof");
		appWindow.setResizable(false);
		appWindow.show();
		
	}
	
	private void musicAndPauseAtMinimize(boolean max, boolean min) {
		if (GameData.isMusicOn()) {
			if (min) GameData.musicOff();
			if (max) GameData.musicOn();
		}
		
		Game game = (Game)getGameState(GameStates.GAME).getController();
		
		if(game.isGameGoingOn()) {
			if(game.isGamePaused() == false)
				GameData.setWasNotPaused(true);
			
			if(GameData.wasNotPaused() && min)
				game.pauseOrPlay(false);
			else if (GameData.wasNotPaused() && max) {
				game.pauseOrPlay(true);
				GameData.setWasNotPaused(false);
			}		
		}
	}
	
	public void changeState(GameStates state) {
		for (GameState gameState : gameStates)
			if (gameState.getState() == state)
				currentState = gameState;
		appWindow.setScene(currentState.getScene());
	}

	public void check() {
		System.out.println("MainWindow!");
	}

	public void close() {
		appWindow.close();
	}
	
	public GameState getGameState() {
		return currentState;
	}

	public GameState getGameState(GameStates state) {
		for (GameState gameState : gameStates)
			if (gameState.getState() == state)
				return gameState;
		return null;
	}

	public static void main(String[] args) {
		launch(args);
	}

}// class MainWindow