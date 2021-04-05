package com.quiz.utils;

import java.io.File;
import java.util.ArrayList;
import com.quiz.models.GamePreferences;
import com.quiz.models.Question;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Store {
	
	
	
	public MediaPlayer mediaplayer;
	public String path = "src/com/quiz/res/quiz.mp3";
	public Media musicFile = new Media(new File(path).toURI().toString());
	public GamePreferences gamePrefs;
	public int currentQuestionNum;
	public ArrayList<Question> questions;
	public int totalScore;
	public String username; 
	
	private static Store _instance;
	
	static {
		_instance = new Store();
	}
	
	private Store() {}

	public static Store getInstance() {
		if (_instance != null)
			return _instance;
		else
			return _instance = new Store();
	}
	
	public void musicOn() {
		mediaplayer = new MediaPlayer(musicFile);
		mediaplayer.setAutoPlay(true);
		mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaplayer.setVolume((float) 0.8);
	}
	
	public void musicOff() {
		mediaplayer.stop();
	}
	
	public void clear() {
		gamePrefs = null;
		mediaplayer.stop();
		mediaplayer = null;
		currentQuestionNum = 0;
		questions.clear();
		totalScore = 0;
	}
}
