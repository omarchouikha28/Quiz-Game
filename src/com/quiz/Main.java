package com.quiz;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Main extends Application {
	
	public static Stage pStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("views/UsernameInput.fxml"));
			Scene scene = new Scene(root,1080,720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			pStage = primaryStage;
			primaryStage.getIcons().add(new Image("file:src/com/quiz/views/IMGBIN_brain-drawing-thought-png_JhqZ8Kz6.png"));
			primaryStage.setScene(scene);
			primaryStage.setTitle("Quiz Me!");
			//primaryStage.initStyle(StageStyle.UNIFIED);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {	
		launch(args);	
	}
}
