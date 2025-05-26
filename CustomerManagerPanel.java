package User;

import ketnoi.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerManagerPanel extends JPanel {
	private JTable table;
	private DefaultTableModel model;
	private String user_id;  // Biến user_id
	
	public CustomerManagerPanel(String user_id) throws SQLException {
		this.user_id = user_id;
		
		setLayout(new BorderLayout());
		
		String[] columns = {"Mã KH", "Họ tên", "Địa chỉ", "SĐT", "Email"};
		model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		
		loadCustomerData();
		
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	private void loadCustomerData() throws SQLException {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "SELECT * FROM customers WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, user_id);
			try (ResultSet rs = stmt.executeQuery()) {
				model.setRowCount(0); // Xóa dữ liệu cũ
				if (rs.next()) {
					String id = rs.getString("id");
					String name = rs.getString("name");
					String address = rs.getString("address");
					String phone = rs.getString("phone");
					String email = rs.getString("email");
					
					model.addRow(new Object[]{id, name, address, phone, email});
				} else {
					// Nếu không có thông tin, hiện thông báo
					JOptionPane.showMessageDialog(this, "Bạn chưa đăng ký thuê bao. Vui lòng đăng ký.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}
