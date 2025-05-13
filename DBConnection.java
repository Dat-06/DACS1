package org.example;// DBConnection.java
import java.sql.*;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3307/internet_db";
	private static final String USER = "root";
	private static final String PASSWORD = "caovandat";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
