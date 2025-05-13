package org.example;

import org.example.Encryptor;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterForm extends JFrame {
	private JTextField tfUsername, tfEmail;
	private JPasswordField pfPassword;
	
	public RegisterForm() {
		setTitle("Đăng ký tài khoản");
		setSize(500, 370);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		init();
		setVisible(true);
	}
	
	private void init() {
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(500, 370));
		
		// Ảnh nền
		JLabel background = new JLabel(new ImageIcon("C:/Users/ASUS/Downloads/abc.jpg"));
		background.setBounds(0, 0, 500, 370);
		layeredPane.add(background, Integer.valueOf(0));
		
		
		// Nội dung chính
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setOpaque(false);
		content.setBounds(50, 40, 400, 270);
		
		JLabel title = new JLabel("Tạo tài khoản mới", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		content.add(title, BorderLayout.NORTH);
		
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
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
		
		inputPanel.add(lbUser);
		inputPanel.add(tfUsername);
		inputPanel.add(lbEmail);
		inputPanel.add(tfEmail);
		inputPanel.add(lbPass);
		inputPanel.add(pfPassword);
		
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
		
		if (!email.endsWith("@gmail.com")) {
			JOptionPane.showMessageDialog(this, "Email phải kết thúc bằng @gmail.com");
			return;
		}
		
		if (!isValidPassword(password)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự, 1 chữ hoa, 1 số và 1 ký tự đặc biệt");
			return;
		}
		
		try (Connection conn = Databaselog.getConnection()) {
			String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, Encryptor.encrypt(password));
			ps.executeUpdate();
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
