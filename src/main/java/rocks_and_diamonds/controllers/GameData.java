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
	private static int[] difficulty = {70,90,110};
	private static int diffIndex;
	private static String texture;
	private static boolean music;
	private static int score;
	private static boolean wasNotPaused;
	public static boolean gameIsGoing;
	private static List<Pair<String,Integer>> standings;
	private static File file;
	
	static {
		playerName = "unknown";
		diffIndex = 1;
		texture = "MALE";
		music = true;
		bgMusic = new AudioClip(Paths.get("src/main/resources/Music/background-music.mp3").toUri().toString());
		wasNotPaused = false;
		gameIsGoing = false;
		standings = new ArrayList<Pair<String,Integer>>();
		file = new File("src/main/resources/Scores/Scores.txt");
		readFromFile();
		bgMusic.setCycleCount(AudioClip.INDEFINITE);
		if(music) 
			bgMusic.play();
	}
	
	public static void setPlayerName(String name) {
		playerName = name;
	}
	
//	public static String getPlayerName() {
//		return playerName;
//	}
	
	public static void changeDifficulty() {
		diffIndex++;
		if(diffIndex == difficulty.length)
			diffIndex = 0;	
	}
	
	public static int getDifficulty() {
		return difficulty[diffIndex];
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
		Collections.sort(standings, new Comparator<Pair<String,Integer>>() {
		    @Override
		    public int compare(Pair<String,Integer> p1, Pair<String,Integer> p2) {
		        return p2.getValue().compareTo(p1.getValue());
		    }
		});
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
	
	public static void setWasNotPaused(boolean was) {
		wasNotPaused = was;
	}
	
	public static boolean wasNotPaused() {
		return wasNotPaused;
	}
}
