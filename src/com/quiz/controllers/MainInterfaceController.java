package com.quiz.controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.quiz.Main;
import com.quiz.models.Question;
import com.quiz.utils.QuestionsDB;
import com.quiz.utils.Store;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainInterfaceController extends QuizPreferencesController {

    @FXML
    Button answerA;
    @FXML
    Button answerB;
    @FXML
    Button answerC;
    @FXML
    Button answerD;
    @FXML
    Label currentQuestion;
    @FXML
    Label QuestionCounter;
    @FXML
    Label total;
    @FXML
    Text warningText;
    @FXML
    FontAwesomeIconView muteButton;
    @FXML
    ProgressBar timeProgressBar;
    @FXML
    Button nextSceneButton;
    @FXML
    Label hintButtonText;
    @FXML
    VBox hintButton;
    
    Store store = Store.getInstance();
    Button selectedButton;
    ArrayList<Button> correctAnswersButtons = new ArrayList<>();
    ArrayList<Button> answerButtons = new ArrayList<>();
    int helpused;
    
    private Timeline timeline;
    private float time = 1;
    private boolean musicOn = true;

    @FXML
    public void initialize() {
        answerButtons.add(answerA);
        answerButtons.add(answerB);
        answerButtons.add(answerC);
        answerButtons.add(answerD);

        loadQuestion();

        store.totalScore = 0;
        total.setText("/" + String.valueOf(this.store.gamePrefs.getNumQuestions()));

        timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(100), (e) -> {
            time = time > 0 ? time - 0.02F : 0;

            if (time == 0)
                onClick(null);
            else
                timeProgressBar.setProgress(time);
        });
        timeline.getKeyFrames().add(kf);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // initialize the button Help
        helpused = this.store.gamePrefs.getNumQuestions() / 5;

        if (helpused == 0) {
            hintButton.setDisable(true);
            hintButton.setOpacity(0.4);
        } else
            hintButtonText.setText("Hint " +"("+String.valueOf(helpused) +")");
    }

    void loadQuestion() {
        Question question = store.questions.get(store.currentQuestionNum);
        currentQuestion.setText(question.getText());
        correctAnswersButtons.clear();


        for (int i=0; i<answerButtons.size(); i++) {
            String answer = question.getPossibleAnswers().get(i);
            answerButtons.get(i).setText(answer);

            if (answer.equals(question.getCorrectAnswer()))
                correctAnswersButtons.add(answerButtons.get(i));
        }
    }

    @FXML
    void onClick(ActionEvent event) {
        if (selectedButton != null || time == 0) {
            
            nextSceneButton.setTooltip(null);

            // add counter
            int value = Integer.valueOf(QuestionCounter.getText());
            if( !(QuestionCounter.getText().equals(String.valueOf(this.store.gamePrefs.getNumQuestions()))) )
            QuestionCounter.setText(String.valueOf(value + 1));

            String rightAnswer = store.questions.get(store.currentQuestionNum).getCorrectAnswer();

            if (selectedButton != null && selectedButton.getText().equals(rightAnswer)) {
                store.totalScore++;
            }

            store.currentQuestionNum++;

            for (Button btn : correctAnswersButtons)
                btn.setStyle("-fx-background-color: #67D48B");

            timeProgressBar.setProgress(-1);
            
            nextSceneButton.setDisable(true);

            Timer loadQuestionTimer = new Timer();
            timeline.pause();
            loadQuestionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        parseAnswer();
                    });
                }
            }, 1500);
        } else {
            showTooltip(Main.pStage, nextSceneButton, "Pick at least one answer", null);
        }
    }

    void parseAnswer() {
        if (store.currentQuestionNum < store.gamePrefs.getNumQuestions()) {
            for (int i=0; i<answerButtons.size(); i++) {
                answerButtons.get(i).setStyle("-fx-background-color: #EAEAEA");
                answerButtons.get(i).setDisable(false);
            }

            loadQuestion();

            if (helpused > 0)
                hintButton.setDisable(false);

            time = 1;
            timeProgressBar.setProgress(time);

            timeline.play();
            nextSceneButton.setDisable(false);

        } else {
            try {
                // save game to history
                QuestionsDB.addHistoryEntry(store.username, store.gamePrefs.getTopicID(), store.currentQuestionNum, store.totalScore);

                timeline.stop();

                // load next time
                if ((double) store.totalScore / store.gamePrefs.getNumQuestions() >= 0.5) {

                    BorderPane root1;

                    root1 = FXMLLoader.load(getClass().getResource("../views/Winner.fxml"));

                    Scene scene1 = new Scene(root1, 1080, 720);
                    Stage mainWindow = Main.pStage;

                    mainWindow.setScene(scene1);
                    //primaryStage.initStyle(StageStyle.UNIFIED);
                    mainWindow.show();

                } else {

                    BorderPane root1 = FXMLLoader.load(getClass().getResource("../views/Loser.fxml"));
                    Scene scene1 = new Scene(root1, 1080, 720);
                    Stage mainWindow = Main.pStage;
                    mainWindow.setScene(scene1);
                    //primaryStage.initStyle(StageStyle.UNIFIED);
                    mainWindow.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void setButtonColorOnClick(ActionEvent e) {
        selectedButton = (Button) e.getSource();

        for (int i = 0; i < answerButtons.size(); i++) {
            if (answerButtons.get(i) == selectedButton)
                answerButtons.get(i).setStyle("-fx-background-color: #E6C997");
            else
                answerButtons.get(i).setStyle("-fx-background-color: #EAEAEA");
        }
    }

    public void hintButtonClicked() {
        for (Button btn : answerButtons)
            btn.setStyle("-fx-background-color: #EAEAEA");

        selectedButton = null;

        hintButton.setDisable(true);
        helpused--;

        hintButtonText.setText("Hint ("+String.valueOf(helpused) +")");

        if (helpused == 0) {
            hintButton.setDisable(true);
            hintButton.setOpacity(0.4);
        }

        int numDisabledButtons = 0;
        for (Button btn : answerButtons)
            if (numDisabledButtons < 2 && !correctAnswersButtons.contains(btn)) {
                btn.setDisable(true);
                numDisabledButtons++;
            }
    }



    public static void showTooltip(Stage owner, Control control, String tooltipText,
                                   ImageView tooltipGraphic) {
        javafx.geometry.Point2D p = control.localToScene(0.0, 0.0);

        final Tooltip customTooltip = new Tooltip();
        customTooltip.setText(tooltipText);

        control.setTooltip(customTooltip);
        customTooltip.setAutoHide(true);

        customTooltip.show(owner, p.getX()
                + control.getScene().getX() + control.getScene().getWindow().getX(), p.getY()
                + control.getScene().getY() + control.getScene().getWindow().getY());

    }

    @FXML
    public void muted(MouseEvent event) {
        if (musicOn) {
            store.mediaplayer.setVolume(0);
            muteButton.setGlyphName("VOLUME_OFF");
            musicOn = false;
        } else {
            store.mediaplayer.setVolume(0.8);
            muteButton.setGlyphName("VOLUME_UP");
            musicOn = true;
        }
    }

}
