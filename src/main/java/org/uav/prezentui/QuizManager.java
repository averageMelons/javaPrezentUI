package org.uav.prezentui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuizManager {
    private List<Question> questions;
    private int currentQuestionIndex;
    private Random random;
    private boolean finished;

    public QuizManager() {
        this.questions = new ArrayList<>();
        this.random = new Random();
        this.currentQuestionIndex = -1;
        this.finished = false;
    }

    public void initialize() {
        loadQuestions();
        selectRandomQuestion();
    }

    private void loadQuestions() {
        // Q 1
        questions.add(new Question(
            "Care concept se referă la gruparea datelor și a metodelor într-o singură unitate?",
            Arrays.asList("Abstracțiune", "Încapsulare", "Polimorfism", "Moștenire"),
            "Încapsulare"
        ));

        // Q 2
        questions.add(new Question(
            "În Java, care cuvânt cheie este folosit pentru a moșteni o clasă?",
            Arrays.asList("implements", "extends", "inherits", "super"),
            "extends"
        ));

        // Q 3
        questions.add(new Question(
            "Un constructor are un tip de returnare.",
            Arrays.asList("Adevărat", "Fals"),
            "Fals"
        ));

        // Q 4
        questions.add(new Question(
            "Care dintre următoarele nu este un tip de date primitiv în Java?",
            Arrays.asList("int", "String", "double", "boolean"),
            "String"
        ));

        // Q 5
        questions.add(new Question(
            "Care este dimensiunea unui 'int' în Java?",
            Arrays.asList("8 biți", "16 biți", "32 biți", "64 biți"),
            "32 biți"
        ));

        // Q 6
        questions.add(new Question(
            "În Java, tablourile (arrays) sunt obiecte.",
            Arrays.asList("Adevărat", "Fals"),
            "Adevărat"
        ));

        // Q 7
        questions.add(new Question(
            "Care cuvânt cheie este utilizat pentru a crea un obiect în Java?",
            Arrays.asList("create", "new", "object", "instantiate"),
            "new"
        ));

        // Q 8
        questions.add(new Question(
            "Care este valoarea implicită a unei variabile de tip boolean în Java?",
            Arrays.asList("true", "false", "null", "undefined"),
            "false"
        ));
    }

    private void selectRandomQuestion() {
        int nextIndex;
        // Selectam o intrebare noua diferita de cea anterioara
        do {
            nextIndex = random.nextInt(questions.size());
        } while (nextIndex == currentQuestionIndex && questions.size() > 1);
        
        currentQuestionIndex = nextIndex;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean checkAnswer(String answer) {
        Question current = getCurrentQuestion();
        if (current == null) {
            return false;
        }
        
        if (current.isCorrect(answer)) {
            finished = true;
            return true;
        }
        
        selectRandomQuestion();
        return false;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public void reset() {
        finished = false;
        currentQuestionIndex = -1;
        selectRandomQuestion();
    }
}
