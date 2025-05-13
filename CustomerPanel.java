package org.example;// CustomerPanel.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class CustomerPanel extends JPanel {
	private JTable table;
	private DefaultTableModel model;
	private JTextField idField, nameField, addressField, phoneField;
	
	public CustomerPanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);
		
		model = new DefaultTableModel(new String[]{"Mã KH", "Tên", "Địa chỉ", "SĐT"}, 0);
		table = new JTable(model);
		loadData();
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel form = new JPanel(new GridLayout(2, 5, 10, 10));
		idField = new JTextField();
		nameField = new JTextField();
		addressField = new JTextField();
		phoneField = new JTextField();
		
		JButton addBtn = new JButton("Thêm");
		addBtn.addActionListener(e -> insertCustomer());
		JButton upBtn = new JButton("Sửa");
		upBtn.addActionListener(e -> updateCustomer());
		
		JButton delBtn = new JButton("Xóa");
		delBtn.addActionListener(e -> deleteCustomer());
		
		form.add(new JLabel("Mã KH")); form.add(idField);
		form.add(new JLabel("Tên")); form.add(nameField); form.add(addBtn);
		form.add(new JLabel("Địa chỉ")); form.add(addressField);
		form.add(new JLabel("SĐT")); form.add(phoneField); form.add(delBtn);
		
		add(form, BorderLayout.SOUTH);
	}
	
	private void updateCustomer() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để sửa.");
			return;
		}
		
		String id = idField.getText();
		String name = nameField.getText();
		String address = addressField.getText();
		String phone = phoneField.getText();
		
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "UPDATE customers SET name = ?, address = ?, phone = ? WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, phone);
			stmt.setString(4, id);
			stmt.executeUpdate();
			loadData();
			JOptionPane.showMessageDialog(this, "Đã cập nhật khách hàng.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật.");
		}
	}
	
	
	private void loadData() {
		model.setRowCount(0);
		try (Connection conn = DBConnection.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
			while (rs.next()) {
				model.addRow(new Object[] {
						rs.getString("id"), rs.getString("name"),
						rs.getString("address"), rs.getString("phone")
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu");
		}
	}
	
	private void insertCustomer() {
		String id = idField.getText().trim();
		String name = nameField.getText().trim();
		String address = addressField.getText().trim();
		String phone = phoneField.getText().trim();
		
		// Kiểm tra các trường có bị để trống không
		if (id.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khách hàng!");
			return;
		}
		
		try (Connection conn = DBConnection.getConnection()) {
			// Kiểm tra trùng mã KH
			PreparedStatement check = conn.prepareStatement("SELECT id FROM customers WHERE id = ?");
			check.setString(1, id);
			ResultSet rs = check.executeQuery();
			if (rs.next()) {
				JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại!");
				return;
			}
			
			// Thêm khách hàng mới
			String sql = "INSERT INTO customers VALUES (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, name);
			stmt.setString(3, address);
			stmt.setString(4, phone);
			stmt.executeUpdate();
			loadData();
			
			// Xóa trắng form
			idField.setText(""); nameField.setText(""); addressField.setText(""); phoneField.setText("");
			
			JOptionPane.showMessageDialog(this, "Đã thêm khách hàng.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi thêm khách hàng");
		}
	}
	
	
	private void deleteCustomer() {
		int row = table.getSelectedRow();
		if (row == -1) return;
		String id = (String) model.getValueAt(row, 0);
		try (Connection conn = DBConnection.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM customers WHERE id = ?");
			stmt.setString(1, id);
			stmt.executeUpdate();
			loadData();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi xóa khách hàng");
		}
		
	}
	
}

