package org.example;// ReportPanel.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportPanel extends JPanel {
	private JTextArea reportArea;
	
	public ReportPanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("THỐNG KÊ DOANH THU THEO THÁNG", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);
		
		reportArea = new JTextArea();
		reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		reportArea.setEditable(false);
		add(new JScrollPane(reportArea), BorderLayout.CENTER);
		
		JButton loadBtn = new JButton("Tải thống kê");
		loadBtn.addActionListener(e -> loadReport());
		add(loadBtn, BorderLayout.SOUTH);
	}
	
	private void loadReport() {
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT MONTH(created_at) AS thang, SUM(total) AS doanhthu FROM invoices GROUP BY MONTH(created_at)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%-10s %-15s\n", "Tháng", "Doanh thu (VNĐ)"));
			sb.append("===========================\n");
			
			while (rs.next()) {
				sb.append(String.format("%-10d %-15.0f\n", rs.getInt(1), rs.getDouble(2)));
			}
			
			reportArea.setText(sb.toString());
		} catch (Exception e) {
			reportArea.setText("Lỗi truy vấn dữ liệu!");
		}
	}
}
