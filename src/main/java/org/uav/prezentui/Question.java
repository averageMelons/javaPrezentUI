package org.uav.prezentui;

import java.util.List;

public class Question {
	private String text;
	private String correctAnswer;
	private List<String> options;

	public Question(String text, List<String> options, String correctAnswer) {
		this.text = text;
		this.correctAnswer = correctAnswer;
		this.options = options;
	}

	public String getText() {
		return text;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public List<String> getOptions() {
		return options;
	}

	public boolean isCorrect(String userAnswer) {
		return correctAnswer.equals(userAnswer);
	}
}
