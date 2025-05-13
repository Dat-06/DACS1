package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {
	private JTextField tfUsername;
	private JPasswordField pfPassword;
	
	public LoginForm() {
		setTitle("Đăng nhập hệ thống");
		setSize(500, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		init();
		setVisible(true);
	}
	
	private void init() {
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(500, 350));
		
		JLabel background = new JLabel(new ImageIcon("C:/Users/ASUS/Downloads/abc.jpg"));
		background.setBounds(0, 0, 500, 350);
		layeredPane.add(background, Integer.valueOf(0));
		
		
		
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setOpaque(false);
		content.setBounds(50, 40, 400, 250);
		
		JLabel title = new JLabel("Vui lòng đăng nhập", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		content.add(title, BorderLayout.NORTH);
		
		JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		inputPanel.setOpaque(false);
		
		JLabel lbUser = new JLabel("Tên đăng nhập:");
		lbUser.setForeground(Color.WHITE);
		tfUsername = new JTextField();
		
		JLabel lbPass = new JLabel("Mật khẩu:");
		lbPass.setForeground(Color.WHITE);
		pfPassword = new JPasswordField();
		pfPassword.setEchoChar('•');
		
		JPanel passPanel = new JPanel(new BorderLayout());
		passPanel.setOpaque(false);
		passPanel.add(pfPassword, BorderLayout.CENTER);
		
		JButton eyeButton = new JButton("mo");
		eyeButton.setPreferredSize(new Dimension(40, 30));
		eyeButton.setFocusPainted(false);
		eyeButton.setContentAreaFilled(false);
		eyeButton.setFont(new Font("Arial", Font.PLAIN, 14));
		eyeButton.addActionListener(e -> {
			if (pfPassword.getEchoChar() != (char) 0) {
				pfPassword.setEchoChar((char) 0);
				eyeButton.setText("dong");
			} else {
				pfPassword.setEchoChar('•');
				eyeButton.setText("mo");
			}
		});
		passPanel.add(eyeButton, BorderLayout.EAST);
		
		inputPanel.add(lbUser);
		inputPanel.add(tfUsername);
		inputPanel.add(lbPass);
		inputPanel.add(passPanel);
		
		JButton btnLogin = new JButton("Đăng nhập");
		btnLogin.setBackground(new Color(30, 144, 255));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
		btnLogin.setFocusPainted(false);
		btnLogin.addActionListener(e -> login());
		
		content.add(inputPanel, BorderLayout.CENTER);
		content.add(btnLogin, BorderLayout.SOUTH);
		
		layeredPane.add(content, Integer.valueOf(2));
		
		setContentPane(layeredPane);
	}
	
	private void login() {
		String username = tfUsername.getText().trim();
		String password = new String(pfPassword.getPassword());
		
		try (Connection conn = Databaselog.getConnection()) {
			String sql = "SELECT * FROM users WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String hashedPass = rs.getString("password");
				if (hashedPass.equals(Encryptor.encrypt(password))) {
					JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					new AdminDashboard(username);
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Sai mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại. Chuyển sang trang đăng ký.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				new RegisterForm();
				dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi đăng nhập: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void main(String[] args) {
		new LoginForm();
	}
}
