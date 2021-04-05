package com.quiz.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import com.quiz.utils.DBManager.DBState;

public class DBManager {

	// JDBC driver name and database URL
	private static final String DB_URL = "jdbc:sqlite:src/com/quiz/res/app.db";
	
	private Connection conn = null;
	
	private static DBManager _instance;
	
	private DBState state;
	
	static {
		_instance = new DBManager();
	}
	
	private DBManager() {
		state = DBState.oppening;
		init();
	}
	
	public static DBManager getInstance() {
		return _instance;
	}
	
	public Connection getConnection() {
		return conn;
	}

	public void init() {
		try {
			conn = DriverManager.getConnection(DB_URL);
			
			state = DBState.ready;
		} catch (Exception e) {
			e.printStackTrace();
			state = DBState.error;
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			this.state = DBState.error;
		}
	}
	
	public DBState getState() {
		return state;
	}
	
	public CloseableResultset executeQuery(String query) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			
			ResultSet res = stmt.executeQuery(query);
			
			return new CloseableResultset(res, stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static enum DBState {
		error, ready, busy, oppening
	}
	
	public static class CloseableResultset {
		private ResultSet res;
		private Statement stmt;
		
		public CloseableResultset(ResultSet result, Statement statement) {
			res = result;
			stmt = statement;
		}
		
		public ResultSet res() {
			return res;
		}
		
		public void close() {
			try {
				stmt.close();
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void loadQuestionsFromCSV(String filename, int themeId, String questionPrefix) {
		// read csv into an arraylist
		ArrayList<Entry<String, String>> data = new ArrayList<>();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String row;
			while ((row = in.readLine()) != null) {
				String[] pair = row.split(",");
				data.add(new AbstractMap.SimpleEntry<String, String>(pair[0],pair[1]));
			}
			
			String SQL = "INSERT INTO Questions ('topic_id','question_text') VALUES (?, ?);";
			String SQL2 = "INSERT INTO Answers ('question_id','answer_text', 'correct') VALUES (?,?,?);";
			pstmt = getInstance().conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
			pstmt2 = getInstance().conn.prepareStatement(SQL2);
			
			
			for (Entry<String, String> entry : data) {				
				pstmt.setInt(1, themeId);
				pstmt.setString(2, questionPrefix + entry.getKey());
				int affectedRows = pstmt.executeUpdate();
				
				if (affectedRows != 0) {
					ResultSet generatedKeys = pstmt.getGeneratedKeys();
					if (generatedKeys.next()) {
						int question_id = generatedKeys.getInt(1);
						
						Random rand = new Random();
						int correctQuestionNum = rand.nextInt(4);
						for (int i=0; i<4; i++) {
							pstmt2.setInt(1, question_id);
							String res = correctQuestionNum == i ? entry.getValue() :
								data.get(rand.nextInt(data.size())).getValue();
							pstmt2.setString(2, res);
							pstmt2.setInt(3, correctQuestionNum == i ? 1 : 0);
							pstmt2.executeUpdate();
						}
					}
				}
			}
						
		} catch (Exception e) {
			System.out.println("Couldn't open \"" + filename + "\"");
		} finally {
			   try {
				pstmt.close();
				pstmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
