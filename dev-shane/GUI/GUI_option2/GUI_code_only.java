package com.compprog1282025;

import com.compprog1282025.model.Employee;
import com.compprog1282025.service.DataLoaderService;
import com.compprog1282025.service.EmployeeService;
import com.compprog1282025.service.PayrollService;
import com.compprog1282025.service.AttendanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.YearMonth;
import java.util.List;

public class Chr2gui1 extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private final String[] columns = {
            "Employee Number", "Last Name", "First Name",
            "SSS", "PhilHealth", "TIN", "Pag-IBIG"
    };

    private List<Employee> employees;
    private EmployeeService employeeService;
    private PayrollService payrollService;
    private AttendanceService attendanceService;

    public Chr2gui1() {
        setTitle("CHR2 GUI - Employee Viewer");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load Data
        DataLoaderService loader = new DataLoaderService();
        loader.loadAllData();
        employees = loader.getEmployees();

        attendanceService = new AttendanceService(loader.getAttendanceRecords());
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
                emp.getGovernmentID().getSss(),
                emp.getGovernmentID().getPhilhealth(),
                emp.getGovernmentID().getTin(),
                emp.getGovernmentID().getPagibig()
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

        JFrame detailFrame = new JFrame("Employee Details");
        detailFrame.setSize(500, 700);
        detailFrame.setLocationRelativeTo(this);
        detailFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Personal & Employment Info ---
        JLabel label;
        label = new JLabel("Full Name: " + emp.getFullName());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Employee No.: " + emp.getEmployeeNumber());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Birthday: " + emp.getBirthday());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Position: " + emp.getPosition().getPosition());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Position Type: " + emp.getPosition().getPosition());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Status: " + emp.getStatus());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Supervisor: "
                + (emp.getPosition().getSupervisor() != null
                ? emp.getPosition().getSupervisor().getFullName()
                : "None"));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        // --- Contact Info ---
        label = new JLabel("Address: " + emp.getContact().getAddress());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Phone: " + emp.getContact().getPhoneNumber());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        // --- Government IDs ---
        label = new JLabel("SSS #: " + emp.getGovernmentID().getSss());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("PhilHealth #: " + emp.getGovernmentID().getPhilhealth());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("TIN #: " + emp.getGovernmentID().getTin());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Pag-IBIG #: " + emp.getGovernmentID().getPagibig());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        // --- Compensation Details ---
        label = new JLabel("Basic Salary: ₱" + emp.getCompensation().getBasicSalary());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Hourly Rate: ₱" + emp.getHourlyRate());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Rice Subsidy: ₱" + emp.getCompensation().getRiceSubsidy());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Phone Allowance: ₱" + emp.getCompensation().getPhoneAllowance());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Clothing Allowance: ₱" + emp.getCompensation().getClothingAllowance());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        label = new JLabel("Semi-monthly Gross: ₱" + emp.getCompensation().getSemiGross());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        // --- Monthly Payroll Computation ---
        label = new JLabel("Enter Month (yyyy-MM):");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        JTextField monthField = new JTextField(10);
        monthField.setMaximumSize(new Dimension(120, 25));
        monthField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(monthField);
        panel.add(Box.createVerticalStrut(5));
        JButton calcBtn = new JButton("Calculate Payroll");
        JTextArea payrollResultArea = new JTextArea(3, 30);
        payrollResultArea.setEditable(false);
        payrollResultArea.setLineWrap(true);
        payrollResultArea.setWrapStyleWord(true);
        payrollResultArea.setOpaque(false);
        payrollResultArea.setBorder(null);
        payrollResultArea.setFocusable(false);
        panel.add(calcBtn);
        panel.add(Box.createVerticalStrut(5));
        payrollResultArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(payrollResultArea);
        
        calcBtn.addActionListener(e -> {
            try {
                YearMonth ym = YearMonth.parse(monthField.getText().trim());
                if (!attendanceService.hasAttendanceForMonth(emp, ym)) {
                    payrollResultArea.setText(""); // Clear result if no attendance
                    return;
                }
                double gross = payrollService.calculateMonthlySalary(emp, ym);
                double net = payrollService.calculateNetSalary(emp, ym);
                payrollResultArea.setForeground(Color.BLACK);
                payrollResultArea.setText(String.format("Payroll for %s:\nGross: ₱%.2f\nNet: ₱%.2f", ym, gross, net));
            } catch (Exception ex) {
                payrollResultArea.setForeground(Color.RED);
                payrollResultArea.setText("Invalid date selection. Use yyyy-MM.");
            }
        });
        
        detailFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        detailFrame.setVisible(true);
    }


    public static void main(String[] args) {
        System.out.println("Launching GUI...");
        SwingUtilities.invokeLater(() -> new Chr2gui1().setVisible(true));
    }
}
