package com.quiz.controllers;

import com.quiz.utils.Store;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class LoserController extends QuizPreferencesController {
	
    Store store = Store.getInstance();
    @FXML Label totalScoreLoser;
    @FXML FontAwesomeIconView muteButton;
    private boolean musicOn = true;
    

    @FXML
    public void initialize() {
        totalScoreLoser.setText("You scored: " + String.valueOf( store.totalScore ) );
        if(store.mediaplayer.getVolume() == 0) {
        	muteButton.setGlyphName("VOLUME_OFF");
        	musicOn = false;
        }
    }

    @FXML
    public void playAgain(ActionEvent event) throws IOException {
        Pane root1 = FXMLLoader.load(getClass().getResource("../views/Score_History.fxml"));
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
