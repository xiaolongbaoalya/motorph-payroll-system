package com.compprog1282025.gui;

import com.compprog1282025.model.Employee;
import com.compprog1282025.service.DataLoaderService;
import com.compprog1282025.service.EmployeeService;
import com.compprog1282025.util.CSVWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EmployeeDashboardGUI extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField tfFirstName, tfLastName, tfStatus;
    private JButton btnUpdate, btnDelete;

    private EmployeeService employeeService;
    private List<Employee> employees;

    public EmployeeDashboardGUI() {
        setTitle("Employee Dashboard - Feature 3");
        setSize(800, 600); // Increased height to show all components
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DataLoaderService loader = new DataLoaderService();
        loader.loadAllData();
        employees = loader.getEmployees();
        employeeService = new EmployeeService(employees);

        initComponents();
        loadTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Table
        String[] columnNames = {"Emp No", "First Name", "Last Name", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Text fields
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        tfFirstName = new JTextField();
        tfLastName = new JTextField();
        tfStatus = new JTextField();
        formPanel.add(new JLabel("First Name:")); formPanel.add(tfFirstName);
        formPanel.add(new JLabel("Last Name:")); formPanel.add(tfLastName);
        formPanel.add(new JLabel("Status:")); formPanel.add(tfStatus);

        // Buttons
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Wrap form and buttons in a container to make sure it's visible
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Row selection
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                tfFirstName.setText((String) tableModel.getValueAt(selectedRow, 1));
                tfLastName.setText((String) tableModel.getValueAt(selectedRow, 2));
                tfStatus.setText((String) tableModel.getValueAt(selectedRow, 3));
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });

        // Update action
        btnUpdate.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row >= 0) {
                Employee emp = employees.get(row);
                emp.setFirstName(tfFirstName.getText());
                emp.setLastName(tfLastName.getText());
                emp.setStatus(tfStatus.getText());
                CSVWriter.writeEmployees(employees); // save to CSV
                loadTable();
                JOptionPane.showMessageDialog(this, "Record updated.");
            }
        });

        // Delete action
        btnDelete.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row >= 0) {
                employees.remove(row);
                CSVWriter.writeEmployees(employees); // save to CSV
                loadTable();
                tfFirstName.setText(""); tfLastName.setText(""); tfStatus.setText("");
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Record deleted.");
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeNumber(), emp.getFirstName(), emp.getLastName(), emp.getStatus()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeDashboardGUI().setVisible(true));
    }
}
