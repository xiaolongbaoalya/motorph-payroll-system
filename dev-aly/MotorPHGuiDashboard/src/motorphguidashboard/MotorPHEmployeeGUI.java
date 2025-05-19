/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorphguidashboard;
// MotorPHEmployeeGUI.java
// Full GUI for employee and admin login, attendance viewing, salary calculations, and logout

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class MotorPHEmployeeGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private static final String EMPLOYEE_PASSWORD = "arbitrary";
    private static final String ADMIN_PASSWORD = "admin";

    public LoginFrame() {
        setTitle("MotorPH Login");
        setSize(350, 230);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Login as:"));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Employee", "Admin"});
        panel.add(roleBox);

        panel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        panel.add(new JLabel());
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String id = userIdField.getText().trim();
            String pass = new String(passwordField.getPassword());

            if (role.equals("Employee")) {
                if (!MockData.employeeInfo.containsKey(id)) {
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!pass.equals(EMPLOYEE_PASSWORD)) {
                    JOptionPane.showMessageDialog(this, "Invalid password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    dispose();
                    new EmployeeDashboardFrame(id).setVisible(true);
                }
            } else {
                if (!id.equals("999999") || !pass.equals(ADMIN_PASSWORD)) {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    dispose();
                    new AdminDashboardFrame().setVisible(true);
                }
            }
        });

        add(panel);
    }
}

class EmployeeDashboardFrame extends JFrame {
    public EmployeeDashboardFrame(String empId) {
        setTitle("Employee Dashboard - " + empId);
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Map<String, String> info = MockData.employeeInfo.get(empId);
        java.util.List<Map<String, String>> records = MockData.attendanceRecords.get(empId);

        JLabel infoLabel = new JLabel("Employee #: " + empId +
                " | Name: " + info.get("First Name") + " " + info.get("Last Name") +
                " | Birthday: " + info.get("Birthday"));
        panel.add(infoLabel, BorderLayout.NORTH);

        String[] cols = {"Date", "Log In", "Log Out"};
        String[][] rowData = new String[records.size()][3];
        double totalHours = 0;
        for (int i = 0; i < records.size(); i++) {
            Map<String, String> r = records.get(i);
            rowData[i][0] = r.get("Date");
            rowData[i][1] = r.get("Log In");
            rowData[i][2] = r.get("Log Out");
            try {
                LocalTime in = LocalTime.parse(r.get("Log In"));
                LocalTime out = LocalTime.parse(r.get("Log Out"));
                totalHours += Duration.between(in, out).toMinutes() / 60.0;
            } catch (Exception ignored) {}
        }

        JTable table = new JTable(new DefaultTableModel(rowData, cols));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        double hourlyRate = 535.71;
        double grossPay = totalHours * hourlyRate;
        double netPay = grossPay - 500;

        JLabel salaryLabel = new JLabel(String.format("Hours Worked: %.2f hrs | Gross Weekly Salary: %.2f | Net Weekly Salary: %.2f",
                totalHours, grossPay, netPay));
        bottomPanel.add(salaryLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        bottomPanel.add(logoutBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }
}

class AdminDashboardFrame extends JFrame {
    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Welcome, Admin!", SwingConstants.CENTER));
        panel.add(new JButton("View All Employees"));
        panel.add(new JButton("Edit Employee Details"));
        panel.add(new JButton("Edit Deduction Settings"));
        panel.add(new JButton("Generate Salary Reports"));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        panel.add(logoutBtn);

        add(panel);
    }
}

class MockData {
    public static Map<String, Map<String, String>> employeeInfo = new HashMap<>();
    public static Map<String, java.util.List<Map<String, String>>> attendanceRecords = new HashMap<>();

    static {
        employeeInfo.put("10001", Map.of("First Name", "Manuel III", "Last Name", "Garcia", "Birthday", "10/11/1983", "Position", "Chief Executive Officer"));

        java.util.List<Map<String, String>> records = new ArrayList<>();
        records.add(createRecord("06/03/2024", "8:59", "18:31"));
        records.add(createRecord("06/04/2024", "9:47", "19:07"));
        records.add(createRecord("06/05/2024", "10:57", "21:32"));
        records.add(createRecord("06/06/2024", "9:32", "19:15"));
        records.add(createRecord("06/07/2024", "9:46", "19:15"));
        attendanceRecords.put("10001", records);
    }

    private static Map<String, String> createRecord(String date, String in, String out) {
        Map<String, String> r = new HashMap<>();
        r.put("Date", date);
        r.put("Log In", in);
        r.put("Log Out", out);
        return r;
    }
}