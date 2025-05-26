package login;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterForm extends JFrame {
	private JTextField tfUsername, tfEmail, tfPhone, tfAddress;
	private JPasswordField pfPassword;
	
	public RegisterForm() {
		setTitle("Đăng ký tài khoản");
		setSize(500, 420);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		init();
		setVisible(true);
	}
	
	private void init() {
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(500, 420));
		
		// Nội dung chính
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setOpaque(false);
		content.setBounds(50, 40, 400, 330);
		
		JLabel title = new JLabel("Tạo tài khoản mới", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		content.add(title, BorderLayout.NORTH);
		
		// Thêm các trường: Username, Email, Mật khẩu, SĐT, Địa chỉ
		JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		inputPanel.setOpaque(false);
		
		JLabel lbUser = new JLabel("Tên đăng nhập:");
		lbUser.setForeground(Color.WHITE);
		tfUsername = new JTextField();
		
		JLabel lbEmail = new JLabel("Email:");
		lbEmail.setForeground(Color.WHITE);
		tfEmail = new JTextField();
		
		JLabel lbPass = new JLabel("Mật khẩu:");
		lbPass.setForeground(Color.WHITE);
		pfPassword = new JPasswordField();
		
		JLabel lbPhone = new JLabel("Số điện thoại:");
		lbPhone.setForeground(Color.WHITE);
		tfPhone = new JTextField();
		
		JLabel lbAddress = new JLabel("Địa chỉ:");
		lbAddress.setForeground(Color.WHITE);
		tfAddress = new JTextField();
		
		inputPanel.add(lbUser);
		inputPanel.add(tfUsername);
		inputPanel.add(lbEmail);
		inputPanel.add(tfEmail);
		inputPanel.add(lbPass);
		inputPanel.add(pfPassword);
		inputPanel.add(lbPhone);
		inputPanel.add(tfPhone);
		inputPanel.add(lbAddress);
		inputPanel.add(tfAddress);
		
		JButton btnRegister = new JButton("Đăng ký");
		btnRegister.setBackground(new Color(46, 204, 113));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
		btnRegister.setFocusPainted(false);
		btnRegister.addActionListener(e -> register());
		
		content.add(inputPanel, BorderLayout.CENTER);
		content.add(btnRegister, BorderLayout.SOUTH);
		
		layeredPane.add(content, Integer.valueOf(2));
		setContentPane(layeredPane);
	}
	
	private void register() {
		String username = tfUsername.getText().trim();
		String email = tfEmail.getText().trim();
		String password = new String(pfPassword.getPassword());
		String phone = tfPhone.getText().trim();
		String address = tfAddress.getText().trim();
		
		if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
			return;
		}
		if (!email.endsWith("@gmail.com")) {
			JOptionPane.showMessageDialog(this, "Email phải kết thúc bằng @gmail.com");
			return;
		}
		if (!isValidPassword(password)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự, 1 chữ hoa, 1 số và 1 ký tự đặc biệt");
			return;
		}
		
		try (Connection conn = Databaselog.getConnection()) {
			conn.setAutoCommit(false);
			// 1. Insert vào bảng users
			String sqlUser = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
			try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
				psUser.setString(1, username);
				psUser.setString(2, email);
				psUser.setString(3, Encryptor.encrypt(password));
				psUser.executeUpdate();
			}
			// 2. Insert vào bảng customers
			String sqlCust = "INSERT INTO customers(id, name, address, phone, email) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement psCust = conn.prepareStatement(sqlCust)) {
				psCust.setString(1, username);  // dùng username làm customer_id
				psCust.setString(2, username);  // tên
				psCust.setString(3, address);
				psCust.setString(4, phone);
				psCust.setString(5, email);
				psCust.executeUpdate();
			}
			conn.commit();
			JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
			dispose();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi đăng ký: " + ex.getMessage());
		}
	}
	
	private boolean isValidPassword(String password) {
		return password.length() >= 6 &&
				password.matches(".*[A-Z].*") &&
				password.matches(".*[0-9].*") &&
				password.matches(".*[!@#$%^&*()].*");
	}
}
