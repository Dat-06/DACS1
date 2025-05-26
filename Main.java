package User;

import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				String loggedInUserId = ""; // Giả sử user đã đăng nhập, lấy userId thực tế từ login
				new InternetCustomerManager(loggedInUserId).setVisible(true);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
