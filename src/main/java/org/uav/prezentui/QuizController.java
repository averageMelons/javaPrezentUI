package org.uav.prezentui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class QuizController {
    private QuizManager quizManager;
    private Stage quizStage;
    private Label questionLabel;
    private VBox optionsBox;
    private Label feedbackLabel;
    private Button submitButton;
    private ToggleGroup answerGroup;

    public void showQuiz() {
        quizManager = new QuizManager();
        quizManager.initialize();

        quizStage = new Stage();
        quizStage.setTitle("Quiz - Verifica cunostintele!");
        quizStage.setWidth(500);
        quizStage.setHeight(400);
        quizStage.initModality(Modality.APPLICATION_MODAL);

        // Create main layout
        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(15, 15, 15, 15));
        mainBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1;");

        // Question label
        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        // Options box
        optionsBox = new VBox(10);
        answerGroup = new ToggleGroup();

        // Feedback label
        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: #d32f2f;");
        feedbackLabel.setVisible(false);

        // Submit button
        submitButton = new Button("Verifica raspuns!");
        submitButton.setPrefWidth(150);
        submitButton.setOnAction(_ -> checkAnswer());

        // Button box
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(submitButton);

        // Add all elements
        mainBox.getChildren().addAll(
                questionLabel,
                new Separator(),
                optionsBox,
                feedbackLabel,
                buttonBox
        );

        Scene scene = new Scene(mainBox);
        quizStage.setScene(scene);

        displayQuestion();
        quizStage.show();
    }

    private void displayQuestion() {
        Question question = quizManager.getCurrentQuestion();
        if (question == null) {
            questionLabel.setText("Eroare: Nicio intrebare disponibila!");
            return;
        }

        questionLabel.setText(question.getText());

        // Clear previous options
        optionsBox.getChildren().clear();
        answerGroup = new ToggleGroup();
        feedbackLabel.setVisible(false);
        feedbackLabel.setText("");

        // Create radio buttons for options
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(answerGroup);
            optionsBox.getChildren().add(radioButton);
        }
    }

    private void checkAnswer() {
        Toggle selectedToggle = answerGroup.getSelectedToggle();
        
        if (selectedToggle == null) {
            feedbackLabel.setText("Alege o optiune mai intai!");
            feedbackLabel.setStyle("-fx-text-fill: #d32f2f;");
            feedbackLabel.setVisible(true);
            return;
        }

        String selectedAnswer = ((RadioButton) selectedToggle).getText();
        
        if (quizManager.checkAnswer(selectedAnswer)) {
            // Correct answer
            feedbackLabel.setText("Bravo! Raspunsul este corect! Poti inchide fereastra.");
            feedbackLabel.setStyle("-fx-text-fill: #2e7d32;");
            feedbackLabel.setVisible(true);
            submitButton.setDisable(true);
        } else {
            // Incorrect answer
            feedbackLabel.setText("Raspunsul este gresit. Incearca din nou!");
            feedbackLabel.setStyle("-fx-text-fill: #d32f2f;");
            feedbackLabel.setVisible(true);
            
            // Display next question after a short delay
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(1000));
            pause.setOnFinished(_ -> displayQuestion());
            pause.play();
        }
    }
}
