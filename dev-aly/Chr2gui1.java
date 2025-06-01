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

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Personal & Employment Info ---
        panel.add(new JLabel("Full Name: " + emp.getFullName()));
        panel.add(new JLabel("Employee No.: " + emp.getEmployeeNumber()));
        panel.add(new JLabel("Birthday: " + emp.getBirthday()));
        panel.add(new JLabel("Position: " + emp.getPosition().getPosition()));
        panel.add(new JLabel("Position Type: " + emp.getPosition().getPosition()));
        panel.add(new JLabel("Status: " + emp.getStatus()));
        panel.add(new JLabel("Supervisor: "
                + (emp.getPosition().getSupervisor() != null
                ? emp.getPosition().getSupervisor().getFullName()
                : "None")));

        // --- Contact Info ---
        panel.add(new JLabel("Address: " + emp.getContact().getAddress()));
        panel.add(new JLabel("Phone: " + emp.getContact().getPhoneNumber()));

        // --- Government IDs ---
        panel.add(new JLabel("SSS #: " + emp.getGovernmentID().getSss()));
        panel.add(new JLabel("PhilHealth #: " + emp.getGovernmentID().getPhilhealth()));
        panel.add(new JLabel("TIN #: " + emp.getGovernmentID().getTin()));
        panel.add(new JLabel("Pag-IBIG #: " + emp.getGovernmentID().getPagibig()));

        // --- Compensation Details ---
        panel.add(new JLabel("Basic Salary: ₱" + emp.getCompensation().getBasicSalary()));
        panel.add(new JLabel("Hourly Rate: ₱" + emp.getHourlyRate()));
        panel.add(new JLabel("Rice Subsidy: ₱" + emp.getCompensation().getRiceSubsidy()));
        panel.add(new JLabel("Phone Allowance: ₱" + emp.getCompensation().getPhoneAllowance()));
        panel.add(new JLabel("Clothing Allowance: ₱" + emp.getCompensation().getClothingAllowance()));
        panel.add(new JLabel("Semi-monthly Gross: ₱" + emp.getCompensation().getSemiGross()));

        // --- Monthly Payroll Computation ---
        JTextField monthField = new JTextField();
        panel.add(new JLabel("Enter Month (yyyy-MM):"));
        panel.add(monthField);

        JButton calcBtn = new JButton("Calculate Payroll");
        calcBtn.addActionListener(e -> {
            try {
                YearMonth ym = YearMonth.parse(monthField.getText().trim());
                double gross = payrollService.calculateMonthlySalary(emp, ym);
                double net = payrollService.calculateNetSalary(emp, ym);
                JOptionPane.showMessageDialog(detailFrame, String.format("Payroll for %s:\nGross: ₱%.2f\nNet: ₱%.2f",
                        ym, gross, net));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(detailFrame, "Invalid date format. Use yyyy-MM.");
            }
        });

        panel.add(calcBtn);
        detailFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        detailFrame.setVisible(true);
    }


    public static void main(String[] args) {
        System.out.println("Launching GUI...");
        SwingUtilities.invokeLater(() -> new Chr2gui1().setVisible(true));
    }
}

