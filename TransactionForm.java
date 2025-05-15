package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class TransactionForm extends JDialog {
	private JTextField txtTransactionId, txtUsername, txtAmount, txtTime, txtStatus;
	private JComboBox<String> packageCombo;
	private Map<String, Integer> packageMap = new HashMap<>();
	private TransactionHistory parent;
	private String transactionId;
	
	public TransactionForm(TransactionHistory parent, String transactionId) {
		this.parent = parent;
		this.transactionId = transactionId;
		setTitle(transactionId == null ? "Thêm giao dịch" : "Sửa giao dịch");
		setModal(true);
		setSize(400, 400);
		setLocationRelativeTo(parent);
		setLayout(new GridLayout(7, 2, 10, 10));
		
		initUI();
		loadPackages();
		
		if (transactionId != null) {
			loadTransactionData();
		}
		
		setVisible(true);
	}
	
	private void initUI() {
		add(new JLabel("Mã giao dịch:"));
		txtTransactionId = new JTextField();
		add(txtTransactionId);
		
		add(new JLabel("Tên người dùng:"));
		txtUsername = new JTextField();
		add(txtUsername);
		
		add(new JLabel("Gói cước:"));
		packageCombo = new JComboBox<>();
		add(packageCombo);
		
		add(new JLabel("Số tiền (VNĐ):"));
		txtAmount = new JTextField();
		add(txtAmount);
		
		add(new JLabel("Thời gian (YYYY-MM-DD HH:MM:SS):"));
		txtTime = new JTextField();
		add(txtTime);
		
		add(new JLabel("Trạng thái:"));
		txtStatus = new JTextField();
		add(txtStatus);
		
		JButton btnSave = new JButton("Lưu");
		btnSave.addActionListener(e -> saveTransaction());
		add(btnSave);
		
		JButton btnCancel = new JButton("Hủy");
		btnCancel.addActionListener(e -> dispose());
		add(btnCancel);
	}
	
	private void loadPackages() {
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT package_id, package_name FROM packages";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("package_name");
				int id = rs.getInt("package_id");
				packageCombo.addItem(name);
				packageMap.put(name, id);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải gói cước: " + e.getMessage());
		}
	}
	
	private void loadTransactionData() {
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, transactionId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				txtTransactionId.setText(rs.getString("transaction_id"));
				txtTransactionId.setEnabled(false);
				txtUsername.setText(rs.getString("username"));
				txtAmount.setText(rs.getString("amount"));
				txtTime.setText(rs.getTimestamp("time").toString());
				txtStatus.setText(rs.getString("status"));
				
				int packageId = rs.getInt("package_id");
				for (Map.Entry<String, Integer> entry : packageMap.entrySet()) {
					if (entry.getValue() == packageId) {
						packageCombo.setSelectedItem(entry.getKey());
						break;
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
		}
	}
	
	private void saveTransaction() {
		String id = txtTransactionId.getText().trim();
		String username = txtUsername.getText().trim();
		String amountStr = txtAmount.getText().trim();
		String timeStr = txtTime.getText().trim();
		String status = txtStatus.getText().trim();
		String packageName = (String) packageCombo.getSelectedItem();
		
		if (id.isEmpty() || username.isEmpty() || amountStr.isEmpty() || timeStr.isEmpty() || status.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
			return;
		}
		
		try (Connection conn = DBConnection.getConnection()) {
			int packageId = packageMap.get(packageName);
			double amount = Double.parseDouble(amountStr);
			
			if (transactionId == null) {
				// Add
				String sql = "INSERT INTO transactions (transaction_id, username, package_id, amount, time, status) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				ps.setString(2, username);
				ps.setInt(3, packageId);
				ps.setDouble(4, amount);
				ps.setTimestamp(5, Timestamp.valueOf(timeStr));
				ps.setString(6, status);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Thêm giao dịch thành công!");
			} else {
				// Update
				String sql = "UPDATE transactions SET username=?, package_id=?, amount=?, time=?, status=? WHERE transaction_id=?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setInt(2, packageId);
				ps.setDouble(3, amount);
				ps.setTimestamp(4, Timestamp.valueOf(timeStr));
				ps.setString(5, status);
				ps.setString(6, transactionId);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Cập nhật giao dịch thành công!");
			}
			
			parent.loadTransactions();
			dispose();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi lưu giao dịch: " + e.getMessage());
		}
	}
}

