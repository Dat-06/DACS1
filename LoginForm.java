package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.example.AdminDashboard;

public class LoginForm extends JFrame {
	private JTextField tfUsername;
	private JPasswordField pfPassword;
	private JLabel lbError;
	private JCheckBox cbRemember;
	private String role; // <-- thêm biến này
	
	
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
		
		
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setOpaque(false);
		content.setBounds(50, 40, 400, 250);
		
		JLabel title = new JLabel("Vui lòng đăng nhập", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		content.add(title, BorderLayout.NORTH);
		
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		JLabel lbUser = new JLabel("Tên đăng nhập:");
		title.setForeground(new Color(173, 216, 230)); // màu vàng (hoặc chọn màu khác)
		lbUser.setForeground(Color.WHITE);
		inputPanel.add(lbUser, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		tfUsername = new JTextField();
		tfUsername.setPreferredSize(new Dimension(200, 30));
		inputPanel.add(tfUsername, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		JLabel lbPass = new JLabel("Mật khẩu:");
		lbPass.setForeground(Color.WHITE);
		inputPanel.add(lbPass, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		
		JPanel passPanel = new JPanel(null);
		passPanel.setOpaque(false);
		passPanel.setPreferredSize(new Dimension(280, 30));
		
		pfPassword = new JPasswordField();
		pfPassword.setEchoChar('•');
		pfPassword.setBounds(0, 0, 250, 30);
		
		ImageIcon iconShow = new ImageIcon(new ImageIcon("G:/icons8-eye-100.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
		ImageIcon iconHide = new ImageIcon(new ImageIcon("G:/icons8-invisible-80.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
		
		JButton eyeButton = new JButton(iconHide);
		eyeButton.setBounds(255, 6, 18, 18);
		eyeButton.setFocusPainted(false);
		eyeButton.setContentAreaFilled(false);
		eyeButton.setBorderPainted(false);
		
		eyeButton.addActionListener(e -> {
			if (pfPassword.getEchoChar() != (char) 0) {
				pfPassword.setEchoChar((char) 0);
				eyeButton.setIcon(iconShow);
			} else {
				pfPassword.setEchoChar('•');
				eyeButton.setIcon(iconHide);
			}
		});
		
		passPanel.add(pfPassword);
		passPanel.add(eyeButton);
		
		inputPanel.add(passPanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		cbRemember = new JCheckBox("Ghi nhớ mật khẩu");
		cbRemember.setOpaque(false);
		cbRemember.setForeground(Color.WHITE);
		inputPanel.add(cbRemember, gbc);
		
		gbc.gridy = 3;
		lbError = new JLabel(" ");
		lbError.setForeground(Color.RED);
		lbError.setFont(new Font("Arial", Font.ITALIC, 12));
		inputPanel.add(lbError, gbc);
		
		JButton btnLogin = new JButton("Đăng nhập");
		btnLogin.setBackground(new Color(30, 144, 255));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
		btnLogin.setFocusPainted(false);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.addActionListener(e -> login());
		
		btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnLogin.setBackground(new Color(0, 120, 215));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnLogin.setBackground(new Color(30, 144, 255));
			}
		});
		
		content.add(inputPanel, BorderLayout.CENTER);
		content.add(btnLogin, BorderLayout.SOUTH);
		
		layeredPane.add(content, Integer.valueOf(2));
		
		setContentPane(layeredPane);
	}
	
	private void login() {
		String username = tfUsername.getText().trim();
		String password = new String(pfPassword.getPassword());
		
		if (username.isEmpty() || password.isEmpty()) {
			lbError.setText("Vui lòng nhập đầy đủ thông tin!");
			return;
		}
		
		try (Connection conn = Databaselog.getConnection()) {
			String sql = "SELECT * FROM users WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String hashedPass = rs.getString("password");
				if (hashedPass.equals(Encryptor.encrypt(password))) {
					JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Th\u00f4ng b\u00e1o", JOptionPane.INFORMATION_MESSAGE);
					if (cbRemember.isSelected()) {
						// Luu thong tin neu can
					}
					if ("Admin".equalsIgnoreCase(role)) {
						new AdminDashboard(username);}
					 else {
						new AdminDashboard(username);
					}
					
					dispose();
				} else {
					lbError.setText("Sai mật khẩu.");
				}
			} else {
				lbError.setText("Tài khoản không tồn tại.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lbError.setText("Lỗi hệ thống: " + e.getMessage());
		}
	}
}
