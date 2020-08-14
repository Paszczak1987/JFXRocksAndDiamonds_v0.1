package rocks_and_diamonds.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javafx.scene.media.AudioClip;
import javafx.util.Pair;

public class GameData {

	private static AudioClip bgMusic;
	
	private static String playerName;
	private static int difficulty;
	private static String texture;
	private static boolean music;
	private static int score;
	private static int levelNumber;
	private static boolean wasNotPaused;
	public static boolean gameIsGoing;
	private static List<Pair<String,Integer>> standings;
	private static File file;
	
	static {
		playerName = "unknown";
		difficulty = 90;
		texture = "MALE";
		music = true;
		bgMusic = new AudioClip(Paths.get("src/main/resources/Music/background-music.mp3").toUri().toString());
		levelNumber = 0;
		wasNotPaused = false;
		gameIsGoing = false;
		standings = new ArrayList<Pair<String,Integer>>();
		file = new File("src/main/resources/Scores/Scores.txt");
		readFromFile();
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
	
	public static void setScore(int scr) {
		score = scr;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void increaseLevel() {
		levelNumber++;
	}
	
	public static void readFromFile() {
		Scanner inputFileReader = null;
		try {
			inputFileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		while (inputFileReader.hasNextLine()) {
			String line = inputFileReader.nextLine();

			String key = line.split("#")[0];
			int value = Integer.valueOf(line.split("#")[1]);

			standings.add(new Pair<String, Integer>(key, value));
		}

		inputFileReader.close();
	}
	
	public static void writeToFile() {
		PrintWriter fileWriter = null;
		try {
			fileWriter = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		for(Pair<String, Integer> standing : standings)
			fileWriter.println(standing.getKey()+"#"+standing.getValue());
		fileWriter.close();
	}
	
	public static void standingAddAndSort() {
		standings.add(new Pair<String, Integer>(playerName, score));
		Collections.sort(standings, new Comparator<Pair<String,Integer>>() {
		    @Override
		    public int compare(Pair<String,Integer> p1, Pair<String,Integer> p2) {
		        return p2.getValue().compareTo(p1.getValue());
		    }
		});
	}
	
	public static List<Pair<String,Integer>> getStandings(){
		return standings;
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
