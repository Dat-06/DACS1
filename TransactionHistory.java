package admin;

import ketnoi.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TransactionHistory extends JPanel {
	private JTable table;
	private DefaultTableModel tableModel;
	
	public TransactionHistory() {
		setLayout(new BorderLayout());
		initUI();
		loadTransactions();
	}
	
	private void initUI() {
		// Thêm 2 cột SDT và Email, sửa tên cột tháng thành Thời gian
		String[] columns = { "Mã giao dịch", "Tên người dùng", "Số điện thoại", "Email", "Gói cước", "Số tiền", "Thời gian", "Số tháng" };
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
		JButton btnReload = new JButton("Tải lại");
		
		btnAdd.addActionListener(e -> new TransactionForm(this, null));
		btnEdit.addActionListener(e -> editTransaction());
		btnDelete.addActionListener(e -> deleteTransaction());
		btnReload.addActionListener(e -> loadTransactions());
		
		btnPanel.add(btnAdd);
		btnPanel.add(btnEdit);
		btnPanel.add(btnDelete);
		btnPanel.add(btnReload);
		
		topPanel.add(btnPanel, BorderLayout.EAST);
		
		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void loadTransactions() {
		tableModel.setRowCount(0);
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT t.transaction_id, t.username, u.phone, u.email, p.name AS package_name, t.amount, t.time, t.months " +
					"FROM transactions t " +
					"JOIN packages p ON t.package_id = p.id " +
					"JOIN customers u ON t.username = u.name " +
					"ORDER BY t.time DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			while (rs.next()) {
				String transactionId = rs.getString("transaction_id");
				String username = rs.getString("username");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String packageName = rs.getString("package_name");
				String amount = String.format("%,d VNĐ", rs.getInt("amount")); // format tiền có dấu phẩy
				Timestamp time = rs.getTimestamp("time");
				String formattedTime = (time != null) ? sdf.format(time) : "";
				String months = rs.getInt("months") + " tháng";
				
				tableModel.addRow(new Object[]{
						transactionId,
						username,
						phone,
						email,
						packageName,
						amount,
						formattedTime,
						months
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch để xóa.");
		}
	}
}
