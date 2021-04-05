package com.quiz.controllers;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.quiz.models.GamePreferences;
import com.quiz.utils.QuestionsDB;
import com.quiz.utils.Store;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QuizPreferencesController {
	
	@FXML ChoiceBox<String> quiz_pref_topic;
	@FXML Spinner<Integer> quiz_pref_num_questions;
	
	
	
	HashMap<Integer, String> topics;
	
	@FXML
    public void initialize() {
		Store s = Store.getInstance();
		s.musicOn();
		topics = QuestionsDB.getAvailableTopics();
		quiz_pref_topic.setItems(FXCollections.observableArrayList(topics.values()));
		quiz_pref_topic.setValue(topics.get(0));
		quiz_pref_num_questions.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3,15, 3));		
    }
	

	@FXML public void startGame(ActionEvent event) throws IOException{
		Store store = Store.getInstance();
		store.gamePrefs = new GamePreferences(quiz_pref_topic.getValue(),
				getKeyByValue(topics, quiz_pref_topic.getValue()),
				quiz_pref_num_questions.getValue());
		store.currentQuestionNum = 0;
		store.questions = QuestionsDB.getQuestions(store.gamePrefs.getTopic());

		AnchorPane root1 = FXMLLoader.load(getClass().getResource("../views/Main_Interface.fxml"));
		Scene scene1 = new Scene(root1,1080,720);
		Stage mainWindow = (Stage) ((Node)event.getSource() ).getScene().getWindow();

		mainWindow.setScene(scene1);
		//primaryStage.initStyle(StageStyle.UNIFIED);
		mainWindow.show();
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
