package com.quiz.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Question {
	
	private int id;
	private String Topic;
	private int TopicId;
	private String text;
	private ArrayList<String> possibleAnswers;
	private String correctAnswer;
	private int userAnswerString;
	private String userAnswerId;
	
	
	public Question(int id, String topic, int topicId, String text, String[] possibleAnswers, String correctAnswer) {
		super();
		this.id = id;
		Topic = topic;
		TopicId = topicId;
		this.text = text;
		this.possibleAnswers = new ArrayList<>(Arrays.asList(possibleAnswers));
		this.correctAnswer = correctAnswer;
	}


	public int getUserAnswerString() {
		return userAnswerString;
	}


	public void setUserAnswerString(int userAnswerString) {
		this.userAnswerString = userAnswerString;
	}


	public String getUserAnswerId() {
		return userAnswerId;
	}


	public void setUserAnswerId(String userAnswerId) {
		this.userAnswerId = userAnswerId;
	}


	public int getId() {
		return id;
	}


	public String getTopic() {
		return Topic;
	}


	public int getTopicId() {
		return TopicId;
	}


	public String getText() {
		return text;
	}


	public ArrayList<String> getPossibleAnswers() {
		return possibleAnswers;
	}
	
	public void addPossibleAnswer (String answer) {
		if (!answer.isEmpty())
			possibleAnswers.add(answer);
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	@Override
	public String toString() {
		StringBuffer res = new StringBuffer("\"" + text + "\" :\n");
		int i = 0;
		for (String answer : possibleAnswers)
			res.append(++i).append(" - \"" + answer + "\"\n");
		res.append("==> \"" + correctAnswer + "\"\n");
		return res.toString();
	}
	
}
