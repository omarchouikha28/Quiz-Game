package com.quiz.models;

public class GamePreferences {
	private String topic;
	private int topicID;
	private int numQuestions;
	
	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public GamePreferences(String topic, int topicID, int numQuestions) {
		super();
		this.topic = topic;
		this.numQuestions = numQuestions;
		this.topicID = topicID;
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
}
