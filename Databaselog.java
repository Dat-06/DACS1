package login;// File: Database.java
import java.sql.*;

public class Databaselog {
	private static final String URL = "jdbc:mysql://localhost:3307/registry_db";
	private static final String USER = "root"; // Thay bằng tài khoản MySQL của bạn
	private static final String PASSWORD = "caovandat"; // Thay bằng mật khẩu MySQL của bạn
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void createUserTable() {
		String sql = "CREATE TABLE IF NOT EXISTS users (" +
				"id INT AUTO_INCREMENT PRIMARY KEY," +
				"username VARCHAR(100)," +
				"email VARCHAR(100)," +
				"password VARCHAR(256))";
		try (Connection conn = getConnection();
		     Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
