package com.quiz.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import com.quiz.models.HistoryEntry;
import com.quiz.utils.QuestionsDB;
import com.quiz.utils.Store;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScoreHistoryController {
	
	@FXML TableView<HistoryEntry> historyTable;
	@FXML TableColumn usernameColumn;
	@FXML TableColumn topicColumn;
	@FXML TableColumn numberQuestionsColumn;
	@FXML TableColumn scoreColumn;
	@FXML FontAwesomeIconView muteButton;
	private boolean musicOn = true;
	Store store = Store.getInstance();
	
	ArrayList<HistoryEntry> history;
	
	@FXML
	public void initialize() {
		history = QuestionsDB.getGameScoreHistory();
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<HistoryEntry, String>("username"));
		usernameColumn.setSortable(true);
		topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
		topicColumn.setSortable(true);
		numberQuestionsColumn.setCellValueFactory(new PropertyValueFactory<>("numQuestions"));
		numberQuestionsColumn.setSortable(true);
		scoreColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswers"));
		scoreColumn.setSortable(true);
		
		//historyTable.setSelectionModel(null);
		historyTable.getSortOrder().add(usernameColumn);
		historyTable.getSortOrder().add(topicColumn);
		historyTable.getSortOrder().add(numberQuestionsColumn);
		historyTable.getSortOrder().add(scoreColumn);
		historyTable.setItems(FXCollections.observableArrayList(history));
		
		if(store.mediaplayer.getVolume() == 0) {
        	muteButton.setGlyphName("VOLUME_OFF");
        	musicOn = false;
        }
	}
	
	@FXML
    public void playAgain(ActionEvent event) throws IOException {
		Store.getInstance().clear();
		
        Pane root1 = FXMLLoader.load(getClass().getResource("../views/Quiz_Preferences.fxml"));
        Scene scene1 = new Scene(root1,1080,720);
        Stage mainWindow = (Stage) ((Node)event.getSource() ).getScene().getWindow();
        mainWindow.setScene(scene1);
        //primaryStage.initStyle(StageStyle.UNIFIED);
        mainWindow.show();
    }
	
	@FXML
    public void muted(MouseEvent event){
    	if (musicOn) {
    		store.mediaplayer.setVolume(0);
    		muteButton.setGlyphName("VOLUME_OFF");
    		musicOn = false;
    	}
    	else {
    		store.mediaplayer.setVolume(0.8);
    		muteButton.setGlyphName("VOLUME_UP");
    		musicOn = true;
    	}
    }
}
