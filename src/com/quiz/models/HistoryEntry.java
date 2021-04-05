package com.quiz.models;

public class HistoryEntry {
	
	private String username;
	private String topic;
	private int numQuestions;
	private int correctAnswers;

	public HistoryEntry(String username, String topic, int numQuestions, int correctAnswers) {
		super();
		this.username = username;
		this.topic = topic;
		this.numQuestions = numQuestions;
		this.correctAnswers = correctAnswers;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
}
