package com.compprog1282025;

import com.compprog1282025.model.Employee;
import com.compprog1282025.service.DataLoaderService;
import com.compprog1282025.service.EmployeeService;
import com.compprog1282025.service.PayrollService;
import com.compprog1282025.service.AttendanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.YearMonth;
import java.util.List;

public class Chr2gui extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private final String[] columns = {"Employee Number", "Last Name", "First Name", "Position"};

    private List<Employee> employees;
    private EmployeeService employeeService;
    private PayrollService payrollService;

    public Chr2gui() {
        setTitle("CHR2 GUI - Employee Viewer");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load Data
        DataLoaderService loader = new DataLoaderService();
        loader.loadAllData();
        employees = loader.getEmployees();

        AttendanceService attendanceService = new AttendanceService(loader.getAttendanceRecords());
        payrollService = new PayrollService(attendanceService);
        employeeService = new EmployeeService(employees);

        // Table
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        populateTable();

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton viewBtn = new JButton("View Employee");
        viewBtn.addActionListener(e -> viewEmployeeDetails());
        buttonPanel.add(viewBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getPosition().getPosition()
            });
        }
    }

    private void viewEmployeeDetails() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee from the table.");
            return;
        }

        int empNum = (int) tableModel.getValueAt(row, 0);
        Employee emp = employeeService.findEmployeeByNumber(empNum);

        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        JTextField monthField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Employee: " + emp.getFullName()));
        panel.add(new JLabel("Position: " + emp.getPosition().getPosition()));
        panel.add(new JLabel("Hourly Rate: " + emp.getHourlyRate()));
        panel.add(new JLabel("Enter Month (yyyy-MM):"));
        panel.add(monthField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Employee Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                YearMonth ym = YearMonth.parse(monthField.getText().trim());
                double gross = payrollService.calculateMonthlySalary(emp, ym);
                double net = payrollService.calculateNetSalary(emp, ym);

                JOptionPane.showMessageDialog(this, String.format("Payroll for %s:\nGross: %.2f\nNet: %.2f",
                        ym, gross, net));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM.");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching GUI...");
        SwingUtilities.invokeLater(() -> new Chr2gui().setVisible(true));
    }
}
