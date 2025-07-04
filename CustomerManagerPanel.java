package User;



import ketnoi.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class CustomerManagerPanel extends JPanel {
	private JLabel avatarLabel;
	private JLabel nameLabel;
	private JLabel phoneLabel;
	private JLabel addressLabel;
	private JLabel emailLabel;
	
	private String customerId = "";
	
	public CustomerManagerPanel() throws SQLException {
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
		centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.setOpaque(false);
		
		// Ảnh đại diện
		avatarLabel = new JLabel();
		avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon avatar = new ImageIcon("G:\\ac.jpg");
		
		Image scaledImage = avatar.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		avatarLabel.setIcon(new ImageIcon(scaledImage));
		avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		Font font = new Font("Arial", Font.BOLD, 18);
		nameLabel = new JLabel("Họ và tên: ");
		phoneLabel = new JLabel("SĐT: ");
		addressLabel = new JLabel("Địa chỉ: ");
		emailLabel = new JLabel("Email: ");
		JLabel[] labels = {nameLabel, phoneLabel, addressLabel, emailLabel};
		for (JLabel label : labels) {
			label.setFont(font);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		
		JButton editButton = new JButton("Chỉnh sửa thông tin");
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		editButton.setFont(new Font("Arial", Font.BOLD, 16));
		editButton.addActionListener(e -> {
			try {
				editCustomerInfo(e);
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		
		centerPanel.add(avatarLabel);
		centerPanel.add(Box.createVerticalStrut(20));
		centerPanel.add(nameLabel);
		centerPanel.add(phoneLabel);
		centerPanel.add(addressLabel);
		centerPanel.add(emailLabel);
		centerPanel.add(Box.createVerticalStrut(20));
		centerPanel.add(editButton);
		
		add(centerPanel, BorderLayout.CENTER);
		
		loadFirstCustomerData();
	}
	
	private void loadFirstCustomerData() throws SQLException {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "SELECT * FROM customers LIMIT 1";
		try (PreparedStatement stmt = conn.prepareStatement(query);
		     ResultSet rs = stmt.executeQuery()) {
			
			if (rs.next()) {
				customerId = rs.getString("id");
				nameLabel.setText("Họ và tên: " + rs.getString("name"));
				phoneLabel.setText("SĐT: " + rs.getString("phone"));
				addressLabel.setText("Địa chỉ: " + rs.getString("address"));
				emailLabel.setText("Email: " + rs.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void editCustomerInfo(ActionEvent e) throws SQLException {
		String newName = JOptionPane.showInputDialog(this, "Nhập tên mới:", nameLabel.getText().replace("Họ và tên: ", ""));
		String newPhone = JOptionPane.showInputDialog(this, "Nhập số điện thoại mới:", phoneLabel.getText().replace("SĐT: ", ""));
		String newAddress = JOptionPane.showInputDialog(this, "Nhập địa chỉ mới:", addressLabel.getText().replace("Địa chỉ: ", ""));
		String newEmail = JOptionPane.showInputDialog(this, "Nhập email mới:", emailLabel.getText().replace("Email: ", ""));
		
		if (newName != null && newPhone != null && newAddress != null && newEmail != null) {
			nameLabel.setText("Họ và tên: " + newName);
			phoneLabel.setText("SĐT: " + newPhone);
			addressLabel.setText("Địa chỉ: " + newAddress);
			emailLabel.setText("Email: " + newEmail);
			
			updateCustomerInDatabase(newName, newPhone, newAddress, newEmail);
		}
	}
	
	private void updateCustomerInDatabase(String name, String phone, String address, String email) throws SQLException {
		Connection conn = DBConnection.getConnection();
		if (conn == null || customerId.isEmpty()) return;
		
		String query = "UPDATE customers SET name=?, phone=?, address=?, email=? WHERE id=?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.setString(2, phone);
			stmt.setString(3, address);
			stmt.setString(4, email);
			stmt.setString(5, customerId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Không thể cập nhật dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}
