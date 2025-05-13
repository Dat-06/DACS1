package org.example;// PackagePanel.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PackagePanel extends JPanel {
	private JTable table;
	private DefaultTableModel model;
	private JTextField idField, nameField, priceField;
	
	public PackagePanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("QUẢN LÝ GÓI CƯỚC", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);
		
		model = new DefaultTableModel(new String[]{"Mã gói", "Tên gói", "Giá (VNĐ)"}, 0);
		table = new JTable(model);
		loadData();
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
		idField = new JTextField();
		nameField = new JTextField();
		priceField = new JTextField();
		
		JButton addBtn = new JButton("Thêm");
		addBtn.addActionListener(e -> insertPackage());
		JButton upBtn = new JButton("Up");
		upBtn.addActionListener(e -> updatePackage());
		JButton delBtn = new JButton("Xóa");
		delBtn.addActionListener(e -> deletePackage());
		
		form.add(new JLabel("Mã gói")); form.add(idField);
		form.add(new JLabel("Tên gói")); form.add(nameField);
		form.add(new JLabel("Giá")); form.add(priceField);
		form.add(addBtn); form.add(delBtn);
		
		add(form, BorderLayout.SOUTH);
	}
	
	private void updatePackage() {
	}
	
	private void loadData() {
		model.setRowCount(0);
		try (Connection conn = DBConnection.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM packages");
			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("id"), rs.getString("name"),
						rs.getDouble("price")
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải gói cước");
		}
	}
	
	private void insertPackage() {
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "INSERT INTO packages VALUES (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, idField.getText());
			stmt.setString(2, nameField.getText());
			stmt.setDouble(3, Double.parseDouble(priceField.getText()));
			stmt.executeUpdate();
			loadData();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi thêm gói cước");
		}
	}
	
	private void deletePackage() {
		int row = table.getSelectedRow();
		if (row == -1) return;
		String id = (String) model.getValueAt(row, 0);
		try (Connection conn = DBConnection.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM packages WHERE id = ?");
			stmt.setString(1, id);
			stmt.executeUpdate();
			loadData();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi xóa gói cước");
		}
	}
}
