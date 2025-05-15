package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TransactionHistory extends JPanel {
	private JTable table;
	private DefaultTableModel tableModel;
	
	public TransactionHistory() {
		setLayout(new BorderLayout());
		initUI();
		loadTransactions();
	}
	
	private void initUI() {
		String[] columns = { "Mã giao dịch", "Tên người dùng", "Gói cước", "Số tiền", "Thời gian", "Trạng thái" };
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);
		table.setRowHeight(25);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		JLabel title = new JLabel("LỊCH SỬ GIAO DỊCH", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 22));
		title.setForeground(new Color(52, 73, 94));
		title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(title, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel();
		JButton btnAdd = new JButton("Thêm");
		JButton btnEdit = new JButton("Sửa");
		JButton btnDelete = new JButton("Xóa");
		
		btnAdd.addActionListener(e -> new TransactionForm(this, null));
		btnEdit.addActionListener(e -> editTransaction());
		btnDelete.addActionListener(e -> deleteTransaction());
		
		btnPanel.add(btnAdd);
		btnPanel.add(btnEdit);
		btnPanel.add(btnDelete);
		topPanel.add(btnPanel, BorderLayout.EAST);
		
		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void loadTransactions() {
		tableModel.setRowCount(0); // clear old data
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT t.transaction_id, t.username, p.package_name, t.amount, t.time, t.status " +
					"FROM transactions t JOIN packages p ON t.package_id = p.package_id ORDER BY t.time DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				tableModel.addRow(new Object[]{
						rs.getString("transaction_id"),
						rs.getString("username"),
						rs.getString("package_name"),
						rs.getString("amount") + " VNĐ",
						rs.getTimestamp("time").toString(),
						rs.getString("status")
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void editTransaction() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			String transactionId = (String) tableModel.getValueAt(selectedRow, 0);
			new TransactionForm(this, transactionId);
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch để sửa.");
		}
	}
	
	private void deleteTransaction() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			String transactionId = (String) tableModel.getValueAt(selectedRow, 0);
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa giao dịch này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try (Connection conn = DBConnection.getConnection()) {
					String sql = "DELETE FROM transactions WHERE transaction_id = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, transactionId);
					ps.executeUpdate();
					loadTransactions();
					JOptionPane.showMessageDialog(this, "Xóa thành công!");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch để xóa.");
		}
	}
}
