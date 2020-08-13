package rocks_and_diamonds.controllers;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;

public class GameData {
	
	private static String playerName;
	private static int difficulty;
	private static String texture;
	private static boolean music;
	private static AudioClip bgMusic;
	private static int score;
	private static int levelNumber;
	private static boolean wasNotPaused;
	
	static {
		playerName = "unknown";
		difficulty = 90;
		texture = "MALE";
		music = true;
		bgMusic = new AudioClip(Paths.get("src/main/resources/Music/background-music.mp3").toUri().toString());
		score = 0;
		levelNumber = 0;
		wasNotPaused = false;
		
		if(music)
			bgMusic.play();
		
	}
	
	public static void setPlayerName(String name) {
		playerName = name;
	}
	
	public static String getPlayerName() {
		return playerName;
	}
	
	public static void changeDifficulty() {
		if(difficulty == 90)
			difficulty = 45;
		else
			difficulty = 90;
	}
	
	public static int getDifficulty() {
		return difficulty;
	}
	
	public static void changeTexture() {
		if(texture.equals("MALE"))
			texture = "FEMALE";
		else
			texture = "MALE";
	}
	
	public static String getTexture() {
		return texture;
	}
	
	public static void musicOnOff() {
		music = !music;
		if(!music)
			bgMusic.stop();
		else
			bgMusic.play();
	}
	
	public static void musicOn() {
		bgMusic.play();
	}
	
	public static void musicOff() {
		bgMusic.stop();
	}
	
	public static boolean isMusicOn() {
		return music;
	}
	
	public static void increaseScore(int scr) {
		score += scr;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void increaseLevel() {
		levelNumber++;
	}
	
	public static int getLvlNum() {
		return levelNumber;
	}
	
	public static void setWasNotPaused(boolean was) {
		wasNotPaused = was;
	}
	
	public static boolean wasNotPaused() {
		return wasNotPaused;
	}
}
