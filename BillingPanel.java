package org.example;// BillingPanel.java
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;

public class BillingPanel extends JPanel {
	private JComboBox<String> customerBox, packageBox;
	private JTextField monthsField, totalField;
	
	public BillingPanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("TÍNH TIỀN INTERNET", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);
		
		JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
		customerBox = new JComboBox<>();
		packageBox = new JComboBox<>();
		monthsField = new JTextField();
		totalField = new JTextField();
		totalField.setEditable(false);
		
		loadComboBoxes();
		
		JButton calcBtn = new JButton("Tính tiền");
		calcBtn.addActionListener(e -> calculateTotal());
		
		JButton saveBtn = new JButton("Ghi hóa đơn");
		saveBtn.addActionListener(e -> saveInvoice());
		
		form.add(new JLabel("Khách hàng")); form.add(customerBox);
		form.add(new JLabel("Gói cước")); form.add(packageBox);
		form.add(new JLabel("Số tháng")); form.add(monthsField);
		form.add(new JLabel("Tổng tiền")); form.add(totalField);
		form.add(calcBtn); form.add(saveBtn);
		
		add(form, BorderLayout.CENTER);
	}
	
	private void loadComboBoxes() {
		try (Connection conn = DBConnection.getConnection()) {
			Statement s1 = conn.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT id FROM customers");
			while (rs1.next()) customerBox.addItem(rs1.getString("id"));
			
			Statement s2 = conn.createStatement();
			ResultSet rs2 = s2.executeQuery("SELECT id FROM packages");
			while (rs2.next()) packageBox.addItem(rs2.getString("id"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu combo box");
		}
	}
	
	private void calculateTotal() {
		try (Connection conn = DBConnection.getConnection()) {
			String pkgId = (String) packageBox.getSelectedItem();
			int months = Integer.parseInt(monthsField.getText());
			
			PreparedStatement stmt = conn.prepareStatement("SELECT price FROM packages WHERE id = ?");
			stmt.setString(1, pkgId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				double price = rs.getDouble("price");
				double total = price * months;
				totalField.setText(String.valueOf(total));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tính tiền");
		}
	}
	
	private void saveInvoice() {
		try (Connection conn = DBConnection.getConnection()) {
			String customerId = (String) customerBox.getSelectedItem();
			String packageId = (String) packageBox.getSelectedItem();
			int months = Integer.parseInt(monthsField.getText());
			double total = Double.parseDouble(totalField.getText());
			
			// Save to DB
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO invoices (customer_id, package_id, months, total) VALUES (?, ?, ?, ?)");
			stmt.setString(1, customerId);
			stmt.setString(2, packageId);
			stmt.setInt(3, months);
			stmt.setDouble(4, total);
			stmt.executeUpdate();
			
			// Save to file
			FileWriter writer = new FileWriter("hoadon.txt", true);
			writer.write("KH: " + customerId + ", Gói: " + packageId + ", Tháng: " + months + ", Tổng: " + total + "\n");
			writer.close();
			
			JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi lưu hóa đơn");
		}
	}
}
