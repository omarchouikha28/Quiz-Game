package com.quiz.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

import com.quiz.models.HistoryEntry;
import com.quiz.models.Question;
import com.quiz.utils.DBManager.CloseableResultset;
import com.quiz.utils.DBManager.DBState;

public class QuestionsDB {
	
	private QuestionsDB() {}
	
	private static HashMap<String, ArrayList<Question>> _queryCache = new HashMap<>();
	
	private static DBManager _db = DBManager.getInstance();
	
	public static ArrayList<Question> getQuestions(String topic) {
		
		CloseableResultset resultSet = null;
		if (_queryCache.containsKey(topic))
			return _queryCache.get(topic);
		else if (_db.getState() == DBState.ready) {
			resultSet = _db.executeQuery("SELECT Questions.id, Topics.name, question_text, answer_text, Answers.correct FROM Answers\n"
					+ "INNER JOIN Questions ON Answers.question_id = Questions.id\n"
					+ "INNER JOIN Topics ON topic_id = Topics.id\n"
					+ "WHERE Topics.name = \"" + topic + "\";");
			HashMap<Integer, Question> questions = new HashMap<>();
			try {
				while (resultSet.res().next()) {
					int questionId = resultSet.res().getInt("id");
					String answer = resultSet.res().getString("answer_text");
					boolean correctAnswer = resultSet.res().getInt("correct") == 1;
					if (questions.containsKey(questionId)) {
						questions.get(questionId).addPossibleAnswer(answer);
						if (correctAnswer)
							questions.get(questionId).setCorrectAnswer(answer);
					} else {
						String question_text = resultSet.res().getString("question_text");
						questions.put(questionId, new Question(questionId,
								topic, 0, question_text, new String[]{answer}, correctAnswer ? answer : "") );
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resultSet.close();
			}
			return new ArrayList<>(questions.values());
		} else
			return null;		
	}
	
	public static HashMap<Integer, String> getAvailableTopics() {

		CloseableResultset resultSet = null;
		if (_db.getState() == DBState.ready) {
			resultSet = _db.executeQuery("SELECT * FROM Topics;");
			HashMap<Integer, String> topics = new HashMap<>();
			try {
				while (resultSet.res().next())
					topics.put(resultSet.res().getInt("id"),
							resultSet.res().getString("name"));
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resultSet.close();
			}
			return topics;
		} else
			return null;
	}
	
	public static ArrayList<HistoryEntry> getGameScoreHistory() {

		CloseableResultset resultSet = null;
		if (_db.getState() == DBState.ready) {
			resultSet = _db.executeQuery("SELECT * FROM History INNER JOIN Topics ON topic_id = Topics.id;");
			ArrayList<HistoryEntry> topics = new ArrayList<>();
			try {
				while (resultSet.res().next()) {
					HistoryEntry entry = new HistoryEntry(
							resultSet.res().getString("username"),
							resultSet.res().getString("name"),
							resultSet.res().getInt("num_questions"),
							resultSet.res().getInt("num_correct_responses")
							);
					topics.add(entry);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resultSet.close();
			}
			return topics;
		} else
			return null;
	}
	
	public static void addHistoryEntry(String username, int topicId, int numQuestions, int numCorrectAnswers) {
		PreparedStatement pstmt = null;
		
		try{	
			String SQL = "INSERT INTO History ('username', 'topic_id', 'num_questions', 'num_correct_responses') VALUES (?, ?, ?, ?);";
			pstmt = _db.getConnection().prepareStatement(SQL);
			
			pstmt.setString(1, username);
			pstmt.setInt(2, topicId);
			pstmt.setInt(3, numQuestions);
			pstmt.setInt(4, numCorrectAnswers);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
		} finally {
			   try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
