package com.quiz.controllers;

import java.io.IOException;

import com.quiz.utils.Store;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UsernameInputController {
	@FXML TextField username;
	
	@FXML
	public void openGamePreferencesScene(ActionEvent event) {
		Store.getInstance().username = username.getText();
		
		if (Store.getInstance().username == null || Store.getInstance().username.isEmpty())
			return;
		
		BorderPane root;
		try {
			root = FXMLLoader.load(getClass().getResource("../views/Quiz_Preferences.fxml"));
			Scene scene = new Scene(root,1080,720);
	        Stage mainWindow = (Stage) ((Node)event.getSource() ).getScene().getWindow();
	        mainWindow.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
