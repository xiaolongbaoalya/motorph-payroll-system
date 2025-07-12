package com.compprog1282025.gui;

import com.compprog1282025.auth.UserRole;
import com.compprog1282025.model.Employee;
import com.compprog1282025.model.Attendance;
import com.compprog1282025.model.Compensation;
import com.compprog1282025.model.ContactInfo;
import com.compprog1282025.model.GovernmentID;
import com.compprog1282025.model.Position;
import com.compprog1282025.service.*;
import com.compprog1282025.util.CSVWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

//add this
import java.awt.*;
import java.io.File;
import java.time.YearMonth;
import java.util.List;
import java.awt.Dialog.ModalityType; //add this
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.geom.RoundRectangle2D;
import java.awt.FontFormatException;
import java.io.InputStream;
import javax.swing.border.EmptyBorder;


public class MainGUI extends JFrame {

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final PayrollService payrollService;
    private final List<Employee> employees;
    
    // Add these new fields here
    private JTextField tfEmpNum;
    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfBirthday;
    private JTextField tfAddress;
    private JTextField tfPhone;
    private JTextField tfSSS;
    private JTextField tfPhilHealth;
    private JTextField tfTIN;
    private JTextField tfPagibig;
    private JTextField tfStatus;
    private JTextField tfPosition;
    private JTextField tfSupervisor;
    private JTextField tfBasicSalary;
    private JTextField tfRice;
    private JTextField tfPhoneAllow;
    private JTextField tfClothing;
    private JTextField tfSemiGross;
    private JTextField tfHourly;
    private List<Attendance> attendanceRecords;

    // Static reference to Update/Delete Employee table model
    private static javax.swing.table.DefaultTableModel updateDeleteTableModel = null;

    public MainGUI() {
        // Load data
        DataLoaderService loader = new DataLoaderService();
        loader.loadAllData();

        employees = loader.getEmployees();
        List<Attendance> attendanceRecords = loader.getAttendanceRecords();

        attendanceService = new AttendanceService(attendanceRecords);
        payrollService = new PayrollService(attendanceService);
        employeeService = new EmployeeService(employees);

        initUI();
    }
    
    private void initUI() {
        setTitle("Payroll System");
        setSize(1224, 870); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Use Segoe UI font for all UI elements
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 18);
        Color mainTextColor = new Color(0x1F2937);

        BackgroundPanel bgPanel = new BackgroundPanel();
        setContentPane(bgPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Main rounded login panel
        RoundedPanel loginPanel = new RoundedPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(622, 644));

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.gridwidth = 2;
        innerGbc.insets = new Insets(40, 0, 10, 0);
        innerGbc.anchor = GridBagConstraints.CENTER;

        // Logo
        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/motorph_logo.png"));
            Image img = logoIcon.getImage().getScaledInstance(148, 83, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logo.setText("");
        }
        innerGbc.insets = new Insets(-20, 0, 8, 0); // Top, left, bottom, right (logo)
        loginPanel.add(logo, innerGbc);

        // Title
        innerGbc.gridy++;
        innerGbc.insets = new Insets(0, 0, 16, 0); // Small space below heading
        JLabel titleLabel = new JLabel("Welcome to MotorPH Payroll");
        titleLabel.setFont(mainFont.deriveFont(Font.BOLD, 32f));
        titleLabel.setForeground(mainTextColor);
        loginPanel.add(titleLabel, innerGbc);

        // Role buttons
        innerGbc.gridy++;
        innerGbc.insets = new Insets(0, 0, 28, 0); // More space below role buttons
        JPanel rolePanel = new JPanel(new GridLayout(1,2,0,0));
        rolePanel.setOpaque(false);
        JButton employeeBtn = new JButton("Employee");
        JButton adminBtn = new JButton("Admin");
        styleToggleButton(employeeBtn, true); // Employee selected by default
        styleToggleButton(adminBtn, false);
        employeeBtn.setPreferredSize(new Dimension(200, 50));
        adminBtn.setPreferredSize(new Dimension(200, 50));
        employeeBtn.setFont(mainFont.deriveFont(Font.PLAIN, 20f));
        adminBtn.setFont(mainFont.deriveFont(Font.PLAIN, 20f));
        employeeBtn.setForeground(mainTextColor);
        adminBtn.setForeground(mainTextColor);
        rolePanel.add(employeeBtn);
        rolePanel.add(adminBtn);
        loginPanel.add(rolePanel, innerGbc);

        // --- CardLayout for login forms ---
        innerGbc.gridy++;
        innerGbc.insets = new Insets(0, 0, 0, 0);
        JPanel formCardPanel = new JPanel(new CardLayout());
        formCardPanel.setOpaque(false);

        // --- Employee Login Form ---
        JPanel employeeForm = new JPanel(new GridBagLayout());
        employeeForm.setOpaque(false);
        GridBagConstraints efGbc = new GridBagConstraints();
        efGbc.insets = new Insets(10, 32, 0, 8);
        efGbc.anchor = GridBagConstraints.EAST;
        efGbc.gridx = 0;
        efGbc.gridy = 0;
        // Employee Number
        JLabel empNumLabel = new JLabel("Employee Number:");
        empNumLabel.setFont(mainFont.deriveFont(15f));
        empNumLabel.setForeground(mainTextColor);
        JTextField empNumField = new JTextField();
        empNumField.setFont(mainFont);
        empNumField.setText(""); // Remove placeholder
        empNumField.setToolTipText(null); // Remove tooltip
        Color placeholderColor = new Color(180, 180, 180);
        Color normalColor = empNumField.getForeground();
        empNumField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (empNumField.getText().equals("")) {
                    empNumField.setText("");
                    empNumField.setForeground(normalColor);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (empNumField.getText().isEmpty()) {
                    empNumField.setText("");
                    empNumField.setForeground(placeholderColor);
                }
            }
        });
        SharpTextFieldPanel empNumPanel = new SharpTextFieldPanel(empNumField, 220);
        // Row 0: Employee Number
        efGbc.gridx = 0;
        efGbc.gridy = 0;
        efGbc.anchor = GridBagConstraints.EAST;
        employeeForm.add(empNumLabel, efGbc);
        efGbc.gridx = 1;
        efGbc.anchor = GridBagConstraints.WEST;
        employeeForm.add(empNumPanel, efGbc);
        // Username
        JLabel usernameLabelE = new JLabel("Username:");
        usernameLabelE.setFont(mainFont.deriveFont(15f));
        usernameLabelE.setForeground(mainTextColor);
        JTextField usernameFieldE = new JTextField();
        usernameFieldE.setFont(mainFont);
        SharpTextFieldPanel usernamePanelE = new SharpTextFieldPanel(usernameFieldE, 220);
        // Row 1: Username
        efGbc.gridx = 0;
        efGbc.gridy = 1;
        efGbc.anchor = GridBagConstraints.EAST;
        employeeForm.add(usernameLabelE, efGbc);
        efGbc.gridx = 1;
        efGbc.anchor = GridBagConstraints.WEST;
        employeeForm.add(usernamePanelE, efGbc);
        // Password
        JLabel passwordLabelE = new JLabel("Password:");
        passwordLabelE.setFont(mainFont.deriveFont(15f));
        passwordLabelE.setForeground(mainTextColor);
        JPasswordField passwordFieldE = new JPasswordField();
        passwordFieldE.setFont(mainFont);
        SharpTextFieldPanel passwordPanelE = new SharpTextFieldPanel(passwordFieldE, 220);
        // Row 2: Password
        efGbc.gridx = 0;
        efGbc.gridy = 2;
        efGbc.anchor = GridBagConstraints.EAST;
        employeeForm.add(passwordLabelE, efGbc);
        efGbc.gridx = 1;
        efGbc.anchor = GridBagConstraints.WEST;
        employeeForm.add(passwordPanelE, efGbc);
        // Login Button (centered below fields)
        JButton loginBtnE = new JButton("Login as Employee");
        styleButton(loginBtnE);
        loginBtnE.setPreferredSize(new Dimension(400, 50));
        loginBtnE.setFont(mainFont.deriveFont(Font.PLAIN, 20f));
        loginBtnE.setForeground(Color.WHITE);
        efGbc.gridx = 0;
        efGbc.gridy = 3;
        efGbc.gridwidth = 2;
        efGbc.anchor = GridBagConstraints.CENTER;
        efGbc.insets = new Insets(35, 0, 24, 0);
        employeeForm.add(loginBtnE, efGbc);

        // --- Admin Login Form ---
        JPanel adminForm = new JPanel(new GridBagLayout());
        adminForm.setOpaque(false);
        GridBagConstraints afGbc = new GridBagConstraints();
        afGbc.insets = new Insets(10, 32, 0, 8);
        afGbc.anchor = GridBagConstraints.EAST;
        afGbc.gridx = 0;
        afGbc.gridy = 0;
        // Username
        JLabel usernameLabelA = new JLabel("Username:");
        usernameLabelA.setFont(mainFont.deriveFont(15f));
        usernameLabelA.setForeground(mainTextColor);
        JTextField usernameFieldA = new JTextField();
        usernameFieldA.setFont(mainFont);
        SharpTextFieldPanel usernamePanelA = new SharpTextFieldPanel(usernameFieldA, 220);
        // Row 0: Username
        afGbc.gridx = 0;
        afGbc.gridy = 0;
        afGbc.anchor = GridBagConstraints.EAST;
        adminForm.add(usernameLabelA, afGbc);
        afGbc.gridx = 1;
        afGbc.anchor = GridBagConstraints.WEST;
        adminForm.add(usernamePanelA, afGbc);
        // Password
        JLabel passwordLabelA = new JLabel("Password:");
        passwordLabelA.setFont(mainFont.deriveFont(15f));
        passwordLabelA.setForeground(mainTextColor);
        JPasswordField passwordFieldA = new JPasswordField();
        passwordFieldA.setFont(mainFont);
        SharpTextFieldPanel passwordPanelA = new SharpTextFieldPanel(passwordFieldA, 220);
        // Row 1: Password
        afGbc.gridx = 0;
        afGbc.gridy = 1;
        afGbc.anchor = GridBagConstraints.EAST;
        adminForm.add(passwordLabelA, afGbc);
        afGbc.gridx = 1;
        afGbc.anchor = GridBagConstraints.WEST;
        adminForm.add(passwordPanelA, afGbc);
        // Login Button (centered below fields)
        JButton loginBtnA = new JButton("Login as Admin");
        styleButton(loginBtnA);
        loginBtnA.setPreferredSize(new Dimension(400, 50));
        loginBtnA.setFont(mainFont.deriveFont(Font.PLAIN, 20f));
        loginBtnA.setForeground(Color.WHITE);
        afGbc.gridx = 0;
        afGbc.gridy = 2;
        afGbc.gridwidth = 2;
        afGbc.anchor = GridBagConstraints.CENTER;
        afGbc.insets = new Insets(35, 0, 24, 0);
        adminForm.add(loginBtnA, afGbc);

        // Add forms to card panel
        formCardPanel.add(employeeForm, "EMPLOYEE");
        formCardPanel.add(adminForm, "ADMIN");
        loginPanel.add(formCardPanel, innerGbc);

        CardLayout cardLayout = (CardLayout) formCardPanel.getLayout();
        cardLayout.show(formCardPanel, "EMPLOYEE");

        // Toggle logic
        employeeBtn.addActionListener(e -> {
            styleToggleButton(employeeBtn, true);
            styleToggleButton(adminBtn, false);
            cardLayout.show(formCardPanel, "EMPLOYEE");
        });
        adminBtn.addActionListener(e -> {
            styleToggleButton(employeeBtn, false);
            styleToggleButton(adminBtn, true);
            cardLayout.show(formCardPanel, "ADMIN");
        });

        // Login logic for Employee
        loginBtnE.addActionListener(e -> {
                String empNumStr = empNumField.getText().trim();
            String username = usernameFieldE.getText().trim();
            String password = new String(passwordFieldE.getPassword());
            if (empNumStr.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    StyledErrorDialog(this, "Error", "Please fill in all fields.");
                    return;
                }
                AuthService authService = new AuthService();
                try {
                    authService.loadAccounts();
                } catch (IOException ex) {
                    StyledErrorDialog(this, "Error", "Failed to load account data: " + ex.getMessage());
                    return;
                }
                if (!authService.authenticate(username, password)) {
                    StyledErrorDialog(this, "Login Failed", "Invalid username or password.");
                    return;
                }
                String role = authService.getRole(username);
                if (role == null || !role.equalsIgnoreCase("employee")) {
                    StyledErrorDialog(this, "Access Denied", "You do not have employee access.");
                    return;
                }
                try {
                    int empNum = Integer.parseInt(empNumStr);
                    Employee employee = employeeService.findEmployeeByNumber(empNum);
                    if (employee != null) {
                        showEmployeeDashboard(employee);
                    } else {
                        StyledErrorDialog(this, "Error", "Employee number not found.");
                    }
                } catch (NumberFormatException ex) {
                    StyledErrorDialog(this, "Error", "Invalid employee number.");
                }
        });
        // Login logic for Admin
        loginBtnA.addActionListener(e -> {
            String username = usernameFieldA.getText().trim();
            String password = new String(passwordFieldA.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                StyledErrorDialog(this, "Error", "Please fill in all fields.");
                return;
            }
            AuthService authService = new AuthService();
            try {
                authService.loadAccounts();
            } catch (IOException ex) {
                StyledErrorDialog(this, "Error", "Failed to load account data: " + ex.getMessage());
                return;
            }
            if (!authService.authenticate(username, password)) {
                StyledErrorDialog(this, "Login Failed", "Invalid username or password.");
                return;
            }
            String role = authService.getRole(username);
            if (role == null || !role.equalsIgnoreCase("admin")) {
                StyledErrorDialog(this, "Access Denied", "You do not have admin access.");
                return;
            }
            showAdminMenu();
        });

        bgPanel.add(loginPanel, gbc);
    }
    
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0x4F5EC0)); // Updated color
        button.setForeground(Color.WHITE); // White text
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, true),
            BorderFactory.createEmptyBorder(12, 20, 12, 20) // More padding for pill look
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 32, 32); // Rounded corners
                super.paint(g, c);
                g2.dispose();
            }
        });
    }

    private void styleButtonSmall(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(0x1c3680));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x1c3680), 1, true),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
   
   private void employeeLogin() { // This is the code for employee login
    JDialog loginDialog = new JDialog(this, "Employee Login", true);
    loginDialog.setSize(400, 250);
    loginDialog.setLocationRelativeTo(this);
    loginDialog.setLayout(new BorderLayout(10, 10));

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    formPanel.add(usernameLabel);
    formPanel.add(Box.createVerticalStrut(5));
    formPanel.add(usernameField);
    formPanel.add(Box.createVerticalStrut(15));
    formPanel.add(passwordLabel);
    formPanel.add(Box.createVerticalStrut(5));
    formPanel.add(passwordField);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton loginBtn = new JButton("Login");
    JButton cancelBtn = new JButton("Cancel");
    styleButtonSmall(loginBtn);
    styleButtonSmall(cancelBtn);

    buttonPanel.add(cancelBtn);
    buttonPanel.add(loginBtn);

    loginDialog.add(formPanel, BorderLayout.CENTER);
    loginDialog.add(buttonPanel, BorderLayout.SOUTH);

    loginBtn.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); // this is to NOT allow to trim password

        AuthService authService = new AuthService();

        try {
            authService.loadAccounts();
        } catch (IOException ex) {
            StyledErrorDialog(loginDialog, "Error", "Failed to load account data: " + ex.getMessage());
            return;
        }

        if (!authService.authenticate(username, password)) {
            StyledErrorDialog(this, "Login Failed", "Invalid username or password.");
            return;
        }

        String role = authService.getRole(username);
        if (role == null || !role.equalsIgnoreCase("employee")) {
            StyledErrorDialog(this, "Access Denied", "You do not have employee access.");
            return;
        }

        loginDialog.dispose();

        String input = StyledInputDialog(this, "Verify Identity", "Enter your Employee Number:");
        if (input == null) return;

        try {
            int empNum = Integer.parseInt(input.trim());
            Employee employee = employeeService.findEmployeeByNumber(empNum);

            if (employee != null) {
                showEmployeeDashboard(employee);
            } else {
                StyledErrorDialog(this, "Error", "Employee number not found.");
            }
        } catch (NumberFormatException ex) {
            StyledErrorDialog(this, "Error", "Invalid employee number.");
        }
    });

    cancelBtn.addActionListener(e -> loginDialog.dispose());

    loginDialog.setVisible(true);
}


   private void adminLogin() { //this is the code for admin login
    AttendanceService attendanceService = new AttendanceService(attendanceRecords);
    PayrollService payrollService = new PayrollService(attendanceService);
    EmployeeService employeeService = new EmployeeService(employees);
    ReportService reportService = new ReportService(payrollService, attendanceService);

    JDialog loginDialog = new JDialog(this, "Admin Login", true);
    loginDialog.setSize(400, 250);
    loginDialog.setLocationRelativeTo(this);
    loginDialog.setLayout(new BorderLayout(10, 10));

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    formPanel.add(usernameLabel);
    formPanel.add(Box.createVerticalStrut(5));
    formPanel.add(usernameField);
    formPanel.add(Box.createVerticalStrut(15));
    formPanel.add(passwordLabel);
    formPanel.add(Box.createVerticalStrut(5));
    formPanel.add(passwordField);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton loginBtn = new JButton("Login");
    JButton cancelBtn = new JButton("Cancel");
    styleButtonSmall(loginBtn);
    styleButtonSmall(cancelBtn);

    buttonPanel.add(cancelBtn);
    buttonPanel.add(loginBtn);

    loginDialog.add(formPanel, BorderLayout.CENTER);
    loginDialog.add(buttonPanel, BorderLayout.SOUTH);

    cancelBtn.addActionListener(e -> loginDialog.dispose());

    loginBtn.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); // do NOT trim password

        AuthService authService = new AuthService();

        try {
            authService.loadAccounts();
        } catch (IOException ex) {
            StyledErrorDialog(loginDialog, "Error", "Failed to load account data: " + ex.getMessage());
            return;
        }
        
        if (!authService.authenticate(username, password)) {
            StyledErrorDialog(this, "Login Failed", "Invalid username or password.");
            return;
        }

        if (!authService.authenticate(username, password)) {
            StyledErrorDialog(loginDialog, "Error", "Invalid login credentials. Access denied.");
            return;
        }

        String role = authService.getRole(username);

        if (role == null || !role.trim().equalsIgnoreCase("admin")) {
            StyledErrorDialog(loginDialog, "Access Denied", "You do not have admin privileges.");
            return;
        }

        loginDialog.dispose();
        showAdminMenu();
    });

    loginDialog.setVisible(true);
}

    private void showEmployeeDashboard(Employee employee) {
        getContentPane().removeAll();
        // Outer white rounded panel (same as login size)
        RoundedPanel dashboardPanel = new RoundedPanel();
        dashboardPanel.setPreferredSize(new Dimension(622, 644));
        dashboardPanel.setLayout(new GridBagLayout());
        dashboardPanel.setOpaque(false);

        // Card panel with BorderLayout
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(16, 36, 32, 36)); // less top padding
        cardPanel.setMaximumSize(new Dimension(480, 520));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Center panel for all content below header row ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Move the top row (logo, header, logout) 100px upward
        JPanel topRow = new JPanel();
        topRow.setOpaque(false);
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));
        // Logo (left)
        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/motorph_logo.png"));
            Image img = logoIcon.getImage().getScaledInstance(60, 32, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logo.setText("");
        }
        logo.setAlignmentY(Component.CENTER_ALIGNMENT);
        topRow.add(logo);
        topRow.add(Box.createHorizontalStrut(12)); // space between logo and header
        // Header (right of logo)
        JLabel dashboardHeader = new JLabel("Employee Dashboard");
        dashboardHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        dashboardHeader.setForeground(new Color(0x1F2937));
        dashboardHeader.setAlignmentY(Component.CENTER_ALIGNMENT);
        topRow.add(dashboardHeader);
        topRow.add(Box.createHorizontalGlue());
        // Logout button (right, smaller)
        JLabel logoutIcon = new JLabel();
        try {
            ImageIcon logoutImg = new ImageIcon(getClass().getResource("/assets/logoutBtn.png"));
            Image img = logoutImg.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            logoutIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoutIcon.setText("");
        }
        logoutIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutIcon.setToolTipText("Logout");
        logoutIcon.setAlignmentY(Component.CENTER_ALIGNMENT);
        logoutIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                getContentPane().removeAll();
                initUI();
                revalidate();
                repaint();
            }
        });
        topRow.add(logoutIcon);
        centerPanel.add(Box.createVerticalStrut(16));
        centerPanel.add(topRow);
        centerPanel.add(Box.createVerticalStrut(68));

        // --- Remove name heading and section headers ---
        // Name/info fields (no heading)
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        basicInfoPanel.setOpaque(false);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 15);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLbl = createInfoLabel("Name:", labelFont);
        nameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(nameLbl, gbc);
        gbc.gridy++;
        JLabel posLbl = createInfoLabel("Position:", labelFont);
        posLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(posLbl, gbc);
        gbc.gridy++;
        JLabel hrLbl = createInfoLabel("Hourly Rate:", labelFont);
        hrLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(hrLbl, gbc);
        gbc.gridy++;
        JLabel riceLbl = createInfoLabel("Rice Subsidy:", labelFont);
        riceLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(riceLbl, gbc);
        gbc.gridy++;
        JLabel phoneLbl = createInfoLabel("Phone Allowance:", labelFont);
        phoneLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(phoneLbl, gbc);
        gbc.gridy++;
        JLabel clothLbl = createInfoLabel("Clothing Allowance:", labelFont);
        clothLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        basicInfoPanel.add(clothLbl, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        basicInfoPanel.add(createInfoField(employee.getFullName(), valueFont), gbc);
        gbc.gridy++;
        basicInfoPanel.add(createInfoField(employee.getPosition().getPosition(), valueFont), gbc);
        gbc.gridy++;
        basicInfoPanel.add(createInfoField(String.format("%.2f", employee.getHourlyRate()), valueFont), gbc);
        gbc.gridy++;
        basicInfoPanel.add(createInfoField(String.format("%.2f", employee.getCompensation().getRiceSubsidy()), valueFont), gbc);
        gbc.gridy++;
        basicInfoPanel.add(createInfoField(String.format("%.2f", employee.getCompensation().getPhoneAllowance()), valueFont), gbc);
        gbc.gridy++;
        basicInfoPanel.add(createInfoField(String.format("%.2f", employee.getCompensation().getClothingAllowance()), valueFont), gbc);
        centerPanel.add(basicInfoPanel);
        centerPanel.add(Box.createVerticalStrut(24));

        // Move the 'View Monthly Salary' button 100px downward
        centerPanel.add(Box.createVerticalStrut(100));
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(Box.createHorizontalGlue());
        JButton viewSalaryBtn = new JButton("View Monthly Salary");
        styleButton(viewSalaryBtn);
        viewSalaryBtn.setPreferredSize(new Dimension(260, 48));
        viewSalaryBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        // Restore the action listener for the button
        viewSalaryBtn.addActionListener(e -> {
            // Dialog for month/year input and salary display
            JDialog dialog = new JDialog(this, "Monthly Salary", true);
            dialog.setSize(440, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));
            panel.setBackground(new Color(247, 247, 250));

            JLabel prompt = new JLabel("Choose Year and Month");
            prompt.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(prompt);
            panel.add(Box.createVerticalStrut(28));

            // Row: Year, Month, View button
            JPanel rowPanel = new JPanel();
            rowPanel.setOpaque(false);
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            rowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            int currentYear = java.time.Year.now().getValue();
            int yearRange = 5;
            Integer[] years = new Integer[yearRange * 2 + 1];
            for (int i = 0; i < years.length; i++) {
                years[i] = currentYear - yearRange + i;
            }
            JComboBox<Integer> yearCombo = new JComboBox<>(years);
            yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            yearCombo.setMaximumSize(new Dimension(110, 40));
            yearCombo.setPreferredSize(new Dimension(110, 40));
            yearCombo.setBackground(Color.WHITE);
            yearCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
            yearCombo.setSelectedItem(currentYear);

            String[] months = java.util.Arrays.stream(java.time.Month.values())
                .map(m -> m.toString().substring(0,1) + m.toString().substring(1).toLowerCase().substring(0,2)).toArray(String[]::new);
            JComboBox<String> monthCombo = new JComboBox<>(months);
            monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            monthCombo.setMaximumSize(new Dimension(150, 40));
            monthCombo.setPreferredSize(new Dimension(150, 40));
            monthCombo.setBackground(Color.WHITE);
            monthCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
            monthCombo.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);

            JButton okBtn = new JButton("View");
            styleButton(okBtn);
            okBtn.setPreferredSize(new Dimension(100, 40));
            okBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

            rowPanel.add(yearCombo);
            rowPanel.add(Box.createHorizontalStrut(16));
            rowPanel.add(monthCombo);
            rowPanel.add(Box.createHorizontalStrut(16));
            rowPanel.add(okBtn);

            panel.add(rowPanel);
            panel.add(Box.createVerticalStrut(32));

            // White rounded panel for results
            JPanel resultPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
        };
            resultPanel.setOpaque(false);
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
            resultPanel.setMaximumSize(new Dimension(340, 90));
            resultPanel.setMinimumSize(new Dimension(340, 70));
            resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel grossLabel = new JLabel();
            JLabel netLabel = new JLabel();
            grossLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
            netLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
            grossLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            netLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            grossLabel.setForeground(new Color(30, 30, 30));
            netLabel.setForeground(new Color(30, 30, 30));
            resultPanel.add(grossLabel);
            resultPanel.add(Box.createVerticalStrut(6));
            resultPanel.add(netLabel);

            okBtn.addActionListener(ev -> {
                try {
                    int month = monthCombo.getSelectedIndex() + 1;
                    int year = (Integer) yearCombo.getSelectedItem();
                    java.time.YearMonth ym = java.time.YearMonth.of(year, month);
                double gross = payrollService.calculateMonthlySalary(employee, ym);
                double net = payrollService.calculateNetSalary(employee, ym);
                    grossLabel.setText("Gross Salary: ₱" + String.format("%.2f", gross));
                    netLabel.setText("Net Salary: ₱" + String.format("%.2f", net));
            } catch (Exception ex) {
                    grossLabel.setText("");
                    netLabel.setText("");
                    StyledErrorDialog(dialog, "Error", "Invalid month or year selection.");
                }
            });
            panel.add(resultPanel);
            panel.add(Box.createVerticalStrut(18));
            dialog.add(panel, BorderLayout.CENTER);
            dialog.setVisible(true);
        });
        btnPanel.add(viewSalaryBtn);
        btnPanel.add(Box.createHorizontalGlue());
        centerPanel.add(btnPanel);
        centerPanel.add(Box.createVerticalGlue());

        cardPanel.add(centerPanel, BorderLayout.CENTER);

        // Add cardPanel to dashboardPanel (centered)
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.gridx = 0;
        dgbc.gridy = 0;
        dgbc.anchor = GridBagConstraints.CENTER;
        dashboardPanel.add(cardPanel, dgbc);

        // Set background and add to frame
        BackgroundPanel bgPanel = new BackgroundPanel();
        setContentPane(bgPanel);
        bgPanel.setLayout(new GridBagLayout());
        bgPanel.add(dashboardPanel, new GridBagConstraints());
        revalidate();
        repaint();
    }

    // Helper for info fields with fixed width and font
    private JTextField createInfoField(String value, Font font) {
        JTextField v = new JTextField(value);
        v.setFont(font);
            v.setEditable(false);
            v.setBackground(Color.WHITE);
        v.setPreferredSize(new Dimension(220, 28));
        v.setMaximumSize(new Dimension(220, 28));
            v.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return v;
    }
    // Helper for info labels
    private JLabel createInfoLabel(String text, Font font) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(new Color(0x374151));
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private void showAdminMenu() {
        // Remove all content and show admin menu in main window
        getContentPane().removeAll();
        // Outer white rounded panel (smaller size)
        RoundedPanel adminPanel = new RoundedPanel();
        adminPanel.setPreferredSize(new Dimension(420, 420));
        adminPanel.setLayout(new GridBagLayout());
        adminPanel.setOpaque(false);

        // Card panel with BorderLayout (smaller max size and less padding)
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(8, 18, 18, 18));
        cardPanel.setMaximumSize(new Dimension(320, 320));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Center panel for all content below header row ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Top: Centered logo and Admin Main Menu label
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/motorph_logo.png"));
            Image img = logoIcon.getImage().getScaledInstance((int)(48*1.25), (int)(26*1.25), Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logo.setText("");
        }
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel adminHeader = new JLabel("Admin Main Menu");
        adminHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        adminHeader.setForeground(new Color(0x1F2937));
        adminHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(18));
        headerPanel.add(logo);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(adminHeader);
        headerPanel.add(Box.createVerticalStrut(28));
        centerPanel.add(headerPanel);

        // --- Admin menu buttons ---
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.add(Box.createVerticalGlue());

        int btnW = (int)(240 * 1.08);
        int btnH = (int)(40 * 1.08);
        int borderPadV = (int)(12 * 1.08);
        int borderPadH = (int)(20 * 1.08);
        JButton viewAddBtn = new JButton("View/Add Employee");
        styleButton(viewAddBtn);
        viewAddBtn.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        viewAddBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAddBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x4F5EC0), 2, true),
            BorderFactory.createEmptyBorder(borderPadV, borderPadH, borderPadV, borderPadH)
        ));
        viewAddBtn.addActionListener(e -> showViewAddEmployeeList(this));
        btnPanel.add(viewAddBtn);
        btnPanel.add(Box.createVerticalStrut(24));
        JButton updateDeleteBtn = new JButton("Update/Delete Employee");
        styleButton(updateDeleteBtn);
        updateDeleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        updateDeleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateDeleteBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x4F5EC0), 2, true),
            BorderFactory.createEmptyBorder(borderPadV, borderPadH, borderPadV, borderPadH)
        ));
        updateDeleteBtn.addActionListener(e -> showUpdateDeleteEmployeeList(this));
        // Ensure both buttons are the same size
        int maxBtnW = Math.max(viewAddBtn.getPreferredSize().width, updateDeleteBtn.getPreferredSize().width);
        int maxBtnH = Math.max(viewAddBtn.getPreferredSize().height, updateDeleteBtn.getPreferredSize().height);
        Dimension btnSize = new Dimension(maxBtnW, maxBtnH);
        viewAddBtn.setPreferredSize(btnSize);
        viewAddBtn.setMaximumSize(btnSize);
        updateDeleteBtn.setPreferredSize(btnSize);
        updateDeleteBtn.setMaximumSize(btnSize);
        btnPanel.add(updateDeleteBtn);
        btnPanel.add(Box.createVerticalGlue());
        centerPanel.add(btnPanel);
        centerPanel.add(Box.createVerticalGlue());

        // --- Logout button at bottom center ---
        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.X_AXIS));
        logoutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutPanel.add(Box.createHorizontalGlue());
        JLabel logoutIcon = new JLabel();
        try {
            ImageIcon logoutImg = new ImageIcon(getClass().getResource("/assets/logoutBtn.png"));
            Image img = logoutImg.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            logoutIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoutIcon.setText("");
        }
        logoutIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutIcon.setToolTipText("Logout");
        logoutIcon.setAlignmentY(Component.CENTER_ALIGNMENT);
        logoutIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                getContentPane().removeAll();
                initUI();
                revalidate();
                repaint();
            }
        });
        logoutPanel.add(logoutIcon);
        logoutPanel.add(Box.createHorizontalGlue());
        centerPanel.add(Box.createVerticalStrut(18));
        centerPanel.add(logoutPanel);

        cardPanel.add(centerPanel, BorderLayout.CENTER);

        // Add cardPanel to adminPanel (centered)
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.gridx = 0;
        dgbc.gridy = 0;
        dgbc.anchor = GridBagConstraints.CENTER;
        adminPanel.add(cardPanel, dgbc);

        // Set background and add to frame
        BackgroundPanel bgPanel = new BackgroundPanel();
        setContentPane(bgPanel);
        bgPanel.setLayout(new GridBagLayout());
        bgPanel.add(adminPanel, new GridBagConstraints());
        revalidate();
        repaint();
    }
    
    private void showWeeklySalaryMenu(Component parent) {
    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Weekly Payroll Reports", true);
    dialog.setSize(400, 300);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());

    String[] options = {
        "Gross Salary for specific employee",
        "Net Salary for specific employee",
        "Close"
    };

    JPanel menuPanel = new JPanel();
    menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
    menuPanel.setBackground(Color.WHITE);
    menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

    menuPanel.add(Box.createVerticalGlue());

    for (String option : options) {
        JButton button = new JButton(option);
        styleButton(button);
        button.setPreferredSize(new Dimension(300, 40));
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(button);

        button.addActionListener(e -> {
            switch (option) {
                case "Gross Salary for specific employee" -> {
                    Employee emp = promptEmployeeNumber(dialog);
                    if (emp == null) return;
                    String input = StyledInputDialog(dialog, "Enter Reference Date", "Enter a reference date (yyyy-MM-dd):");
                    if (input == null) return;
                    try {
                        LocalDate date = LocalDate.parse(input.trim());
                        double gross = payrollService.calculateWeeklySalary(emp, date);
                        StyledInfoDialog(dialog, "Gross Weekly Salary",
                                String.format("Gross Salary for %s on week of %s: %.2f", emp.getFullName(), date, gross));
                    } catch (Exception ex) {
                        StyledErrorDialog(dialog, "Error", "Invalid date format. Use yyyy-MM-dd.");
                    }
                }
                case "Net Salary for specific employee" -> {
                    Employee emp = promptEmployeeNumber(dialog);
                    if (emp == null) return;
                    String input = StyledInputDialog(dialog, "Enter Reference Date", "Enter a reference date (yyyy-MM-dd):");
                    if (input == null) return;
                    try {
                        LocalDate date = LocalDate.parse(input.trim());
                        double net = payrollService.calculateNetWeeklySalary(emp, date);
                        StyledInfoDialog(dialog, "Net Weekly Salary",
                                String.format("Net Salary for %s on week of %s: %.2f", emp.getFullName(), date, net));
                    } catch (Exception ex) {
                        StyledErrorDialog(dialog, "Error", "Invalid date format. Use yyyy-MM-dd.");
                    }
                }
                case "Close" -> dialog.dispose();
            }
        });
    }

    menuPanel.add(Box.createVerticalGlue());
    dialog.add(menuPanel, BorderLayout.CENTER);
    dialog.setVisible(true);
}
    
    private void showDetailedEmployeeDialog(Component parent, Employee emp) {
    JDialog detailDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), "View Employee", Dialog.ModalityType.APPLICATION_MODAL);
    detailDialog.setSize(600, 850);
    detailDialog.setLocationRelativeTo(parent);
    detailDialog.setLayout(new BorderLayout(10, 10));

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 0, 8, 0);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    Font valueFont = new Font("Segoe UI", Font.PLAIN, 16);

    int row = 0;
    Object[][] labelValuePairs = {
        {"Full Name", emp.getFullName()},
        {"Employee No.", String.valueOf(emp.getEmployeeNumber())},
        {"Birthday", emp.getBirthday().toString()},
        {"Position", emp.getPosition().getPosition()},
        {"Position Type", emp.getPosition().getPosition()},
        {"Status", emp.getStatus()},
        {"Supervisor", emp.getPosition().getSupervisor() != null ? emp.getPosition().getSupervisor().getFullName() : "None"},
        {"Address", emp.getContact().getAddress()},
        {"Phone", emp.getContact().getPhoneNumber()},
        {"SSS #", emp.getGovernmentID().getSss()},
        {"PhilHealth #", emp.getGovernmentID().getPhilhealth()},
        {"TIN #", emp.getGovernmentID().getTin()},
        {"Pag-IBIG #", emp.getGovernmentID().getPagibig()},
        {"Basic Salary", "₱" + emp.getCompensation().getBasicSalary()},
        {"Hourly Rate", "₱" + emp.getHourlyRate()},
        {"Rice Subsidy", "₱" + emp.getCompensation().getRiceSubsidy()},
        {"Phone Allowance", "₱" + emp.getCompensation().getPhoneAllowance()},
        {"Clothing Allowance", "₱" + emp.getCompensation().getClothingAllowance()},
        {"Semi-monthly Gross", "₱" + emp.getCompensation().getSemiGross()}
    };
    for (Object[] pair : labelValuePairs) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 8, 16); // Add horizontal space between columns
        JLabel label = new JLabel((String)pair[0]);
        label.setFont(labelFont);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        if ("Address".equals(pair[0])) {
            JTextArea addressArea = new JTextArea((String)pair[1]);
            addressArea.setFont(valueFont);
            addressArea.setLineWrap(true);
            addressArea.setWrapStyleWord(true);
            addressArea.setRows(2);
            addressArea.setEditable(false);
            addressArea.setOpaque(false);
            addressArea.setBorder(null);
            addressArea.setFocusable(false);
            formPanel.add(addressArea, gbc);
        } else {
            JLabel value = new JLabel((String)pair[1]);
            value.setFont(valueFont);
            value.setHorizontalAlignment(SwingConstants.LEFT);
            formPanel.add(value, gbc);
        }
        row++;
    }

    // Payroll calculation section (keep dropdowns)
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    JLabel calcLabel = new JLabel("Calculate Payroll:");
    calcLabel.setFont(labelFont);
    formPanel.add(calcLabel, gbc);
    row++;

    // Add a separator line
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
    formPanel.add(separator, gbc);
    row++;

    JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
    dropdownPanel.setOpaque(false);
    int currentYear = java.time.Year.now().getValue();
    int yearRange = 5;
    Integer[] years = new Integer[yearRange * 2 + 1];
    for (int i = 0; i < years.length; i++) {
        years[i] = currentYear - yearRange + i;
    }
    JComboBox<Integer> yearCombo = new JComboBox<>(years);
    yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    yearCombo.setSelectedItem(currentYear);
    String[] months = java.util.Arrays.stream(java.time.Month.values())
        .map(m -> m.toString().substring(0,1) + m.toString().substring(1).toLowerCase().substring(0,2)).toArray(String[]::new);
    JComboBox<String> monthCombo = new JComboBox<>(months);
    monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    monthCombo.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);
    dropdownPanel.add(new JLabel("Year:"));
    dropdownPanel.add(yearCombo);
    dropdownPanel.add(new JLabel("Month:"));
    dropdownPanel.add(monthCombo);
    JButton calcBtn = new JButton("Calculate Payroll");
    styleButtonSmall(calcBtn);
    calcBtn.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
    calcBtn.setForeground(java.awt.Color.WHITE);
    calcBtn.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0x4f, 0x5e, 0xc0), 1, false));
    calcBtn.setFocusPainted(false);
    calcBtn.setPreferredSize(new Dimension(120, 36));
    calcBtn.setMaximumSize(new Dimension(120, 36));
    calcBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    dropdownPanel.add(calcBtn);
    gbc.gridy = row;
    formPanel.add(dropdownPanel, gbc);
    row++;

    // Payroll report box
    RoundedPanel reportPanel = new RoundedPanel();
    reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
    reportPanel.setBackground(Color.WHITE);
    reportPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
        BorderFactory.createEmptyBorder(16, 18, 16, 18)));
    JLabel payrollForLabel = new JLabel();
    JLabel grossLabel = new JLabel();
    JLabel netLabel = new JLabel();
    payrollForLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    grossLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    netLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    reportPanel.add(payrollForLabel);
    reportPanel.add(grossLabel);
    reportPanel.add(netLabel);
    gbc.gridy = row;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(reportPanel, gbc);
    row++;

    calcBtn.addActionListener(e -> {
        try {
            int year = (Integer) yearCombo.getSelectedItem();
            int month = monthCombo.getSelectedIndex() + 1;
            YearMonth ym = YearMonth.of(year, month);
            double gross = payrollService.calculateMonthlySalary(emp, ym);
            double net = payrollService.calculateNetSalary(emp, ym);
            payrollForLabel.setText("Payroll for: " + ym);
            grossLabel.setText(String.format("Gross Salary: ₱%.2f", gross));
            netLabel.setText(String.format("Net Salary: ₱%.2f", net));
        } catch (Exception ex) {
            StyledErrorDialog(detailDialog, "Error", "Invalid year or month selection.");
        }
    });

    JScrollPane scrollPane = new JScrollPane(formPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);
    detailDialog.add(scrollPane, BorderLayout.CENTER);
    detailDialog.setVisible(true);
}

    private void showEmployeeList(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "All Employees", true);
        dialog.setSize(1100, 700);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table setup
        String[] columns = {
            "Employee Number", "Last Name", "First Name", "Status",
            "SSS", "PhilHealth", "TIN", "Pag-IBIG"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable employeeTable = new JTable(tableModel);

        // Populate table
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getStatus(),
                emp.getGovernmentID().getSss(),
                emp.getGovernmentID().getPhilhealth(),
                emp.getGovernmentID().getTin(),
                emp.getGovernmentID().getPagibig()
            });
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Form panel setup
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder("Employee Data");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        formPanel.setBorder(border);

        tfFirstName = new JTextField();
        tfLastName = new JTextField();
        tfStatus = new JTextField();
        tfBirthday = new JTextField();

        JTextField[] fields = {tfFirstName, tfLastName, tfStatus, tfBirthday};
        for (JTextField field : fields) {
            field.setPreferredSize(new Dimension(220, 28));
            field.setMaximumSize(new Dimension(220, 28));
            field.setEditable(false);
        }

        formPanel.add(new JLabel("First Name:")); formPanel.add(tfFirstName);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Last Name:")); formPanel.add(tfLastName);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Status:")); formPanel.add(tfStatus);
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(new JLabel("Birthdate:")); formPanel.add(tfBirthday);
        formPanel.add(Box.createVerticalGlue());

        // Add form panel to the right side (no scroll pane)
        mainPanel.add(formPanel, BorderLayout.EAST);

        // Add selection listener for first name, last name, and status
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = employeeTable.getSelectedRow();
                if (row != -1) {
                    tfFirstName.setText((String) tableModel.getValueAt(row, 2)); // First Name column
                    tfLastName.setText((String) tableModel.getValueAt(row, 1));  // Last Name column
                    tfStatus.setText((String) tableModel.getValueAt(row, 3));    // Status column
                }
            }
        });

        // Buttons panel
        JButton viewBtn = new JButton("View Employee");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        styleButtonSmall(viewBtn);
        styleButtonSmall(updateBtn);
        styleButtonSmall(deleteBtn);

        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        // Add all panels to dialog
        dialog.add(mainPanel, BorderLayout.CENTER);
        JPanel paddedButtonPanel = new JPanel(new BorderLayout());
        paddedButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // top, left, bottom, right
        paddedButtonPanel.add(buttonPanel, BorderLayout.CENTER);

        dialog.add(paddedButtonPanel, BorderLayout.SOUTH);


        // Add selection listener for button enabling
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = employeeTable.getSelectedRow();
            updateBtn.setEnabled(row >= 0);
            deleteBtn.setEnabled(row >= 0);
        });

        viewBtn.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row == -1) {
                StyledErrorDialog(dialog, "Error", "Select an employee from the table.");
                return;
            }
            int empNum = (int) tableModel.getValueAt(row, 0);
            Employee emp = employeeService.findEmployeeByNumber(empNum);
            if (emp == null) {
                StyledErrorDialog(dialog, "Error", "Employee not found.");
                return;
            }
            showDetailedEmployeeDialog(dialog, emp);
        });
               
        // this is the update button, red's converted code
        updateBtn.addActionListener(e -> {
            // No action: Update Employee window is removed as requested
        });

        // this is the delete button code
        deleteBtn.addActionListener(e -> {
        int row = employeeTable.getSelectedRow();
        if (row < 0) return;

        int empNum = (int) tableModel.getValueAt(row, 0);
        Employee emp = employeeService.findEmployeeByNumber(empNum);
        if (emp == null) {
            StyledErrorDialog(dialog, "Error", "Employee not found.");
            return;
        }

        // custom confirmation dialog
        JDialog confirmDialog = new JDialog(dialog, "Confirm Deletion", Dialog.ModalityType.APPLICATION_MODAL);
        confirmDialog.setLayout(new BorderLayout(10, 10));

        JLabel messageLabel = new JLabel("Are you sure you want to delete " + emp.getFullName() + "?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

        JButton yesBtn = new JButton("Yes");
        JButton noBtn = new JButton("No");

        // Set plain font, size, and color directly (not bold)
        Font dialogBtnFont = new Font("Segoe UI", Font.PLAIN, 16);
        Dimension dialogBtnSize = new Dimension(100, 38);
        yesBtn.setFont(dialogBtnFont);
        noBtn.setFont(dialogBtnFont);
        yesBtn.setPreferredSize(dialogBtnSize);
        noBtn.setPreferredSize(dialogBtnSize);
        yesBtn.setBackground(new Color(0x4F5EC0));
        noBtn.setBackground(new Color(0x4F5EC0));
        yesBtn.setForeground(Color.WHITE);
        noBtn.setForeground(Color.WHITE);
        yesBtn.setFocusPainted(false);
        noBtn.setFocusPainted(false);
        yesBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
        noBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
        yesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        yesBtn.addActionListener(e2 -> {
            try {
                CSVWriter.deleteEmployeeFromCSV(empNum);
                employees.remove(emp);
                tableModel.removeRow(row);
                // custom "Employee deleted" dialog
                JDialog deletedDialog = new JDialog(dialog, "Success", Dialog.ModalityType.APPLICATION_MODAL);
                deletedDialog.setLayout(new BorderLayout(10, 10));
                JLabel successLabel = new JLabel("Employee deleted!");
                successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                successLabel.setBorder(BorderFactory.createEmptyBorder(10, 75, 5, 75));
                JButton okBtn = new JButton("OK");
                okBtn.setFont(dialogBtnFont);
                okBtn.setPreferredSize(dialogBtnSize);
                okBtn.setBackground(new Color(0x4F5EC0));
                okBtn.setForeground(Color.WHITE);
                okBtn.setFocusPainted(false);
                okBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
                okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                okBtn.addActionListener(e3 -> deletedDialog.dispose());
                JPanel btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(okBtn);
                btnPanel.add(Box.createHorizontalGlue());
                deletedDialog.add(successLabel, BorderLayout.CENTER);
                deletedDialog.add(btnPanel, BorderLayout.SOUTH);
                deletedDialog.pack();
                deletedDialog.setLocationRelativeTo(dialog);
                deletedDialog.setVisible(true);
            } catch (Exception ex) {
                StyledErrorDialog(dialog, "Error", "Failed to delete employee: " + ex.getMessage());
            }
            confirmDialog.dispose();
        });
        noBtn.addActionListener(e2 -> confirmDialog.dispose());

        // Use BoxLayout for button panel to respect button sizes
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(yesBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(16, 0)));
        btnPanel.add(noBtn);
        btnPanel.add(Box.createHorizontalGlue());

        confirmDialog.add(messageLabel, BorderLayout.CENTER);
        confirmDialog.add(btnPanel, BorderLayout.SOUTH);
        confirmDialog.pack();
        confirmDialog.setLocationRelativeTo(dialog);
        confirmDialog.setVisible(true);
    });


        dialog.setVisible(true);
    }
    
    //the output for the dialog when the update button is clicked on should be edited here
    private void showUpdateDialog(Window parent, Employee emp) throws IOException {
    JDialog dialog = new JDialog(parent, "Update Employee", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLayout(new BorderLayout());

    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    JTextField tfFirstName = new JTextField();
    JTextField tfLastName = new JTextField();
    JTextField tfBirthday = new JTextField();
    JTextField tfAddress = new JTextField();
    JTextField tfPhone = new JTextField();
    JTextField tfSSS = new JTextField();
    JTextField tfPhil = new JTextField();
    JTextField tfTIN = new JTextField();
    JTextField tfPagibig = new JTextField();
    JTextField tfStatus = new JTextField();
    JTextField tfPosition = new JTextField();
    JTextField tfSupervisor = new JTextField(emp.getPosition().getSupervisor() != null ? emp.getPosition().getSupervisor().getFullName() : "");
    JTextField tfBasic = new JTextField();
    JTextField tfRice = new JTextField();
    JTextField tfPhoneAllow = new JTextField();
    JTextField tfClothing = new JTextField();
    JTextField tfSemi = new JTextField();
    JTextField tfHourly = new JTextField();

    panel.add(new JLabel("First Name [" + emp.getFirstName() + "]:")); panel.add(tfFirstName);
    panel.add(new JLabel("Last Name [" + emp.getLastName() + "]:")); panel.add(tfLastName);
    panel.add(new JLabel("Birthday (MM/dd/yyyy) [" + emp.getBirthday().format(fmt) + "]:")); panel.add(tfBirthday);
    panel.add(new JLabel("Address [" + emp.getContact().getAddress() + "]:")); panel.add(tfAddress);
    panel.add(new JLabel("Phone [" + emp.getContact().getPhoneNumber() + "]:")); panel.add(tfPhone);
    panel.add(new JLabel("SSS [" + emp.getGovernmentID().getSss() + "]:")); panel.add(tfSSS);
    panel.add(new JLabel("PhilHealth [" + emp.getGovernmentID().getPhilhealth() + "]:")); panel.add(tfPhil);
    panel.add(new JLabel("TIN [" + emp.getGovernmentID().getTin() + "]:")); panel.add(tfTIN);
    panel.add(new JLabel("Pag-ibig [" + emp.getGovernmentID().getPagibig() + "]:")); panel.add(tfPagibig);
    panel.add(new JLabel("Status [" + emp.getStatus() + "]:")); panel.add(tfStatus);
    panel.add(new JLabel("Position [" + emp.getPosition().getPosition() + "]:")); panel.add(tfPosition);
    panel.add(new JLabel("Supervisor [" + tfSupervisor.getText() + "]:")); panel.add(tfSupervisor);
    panel.add(new JLabel("Basic Salary [" + emp.getCompensation().getBasicSalary() + "]:")); panel.add(tfBasic);
    panel.add(new JLabel("Rice Subsidy [" + emp.getCompensation().getRiceSubsidy() + "]:")); panel.add(tfRice);
    panel.add(new JLabel("Phone Allowance [" + emp.getCompensation().getPhoneAllowance() + "]:")); panel.add(tfPhoneAllow);
    panel.add(new JLabel("Clothing Allowance [" + emp.getCompensation().getClothingAllowance() + "]:")); panel.add(tfClothing);
    panel.add(new JLabel("Semi-Monthly [" + emp.getCompensation().getSemiGross() + "]:")); panel.add(tfSemi);
    panel.add(new JLabel("Hourly Rate [" + emp.getHourlyRate() + "]:")); panel.add(tfHourly);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnOk = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    styleButtonSmall(btnOk);       // Use your custom styling method
    styleButtonSmall(btnCancel);   // Use your custom styling method

    buttonPanel.add(btnOk);
    buttonPanel.add(btnCancel);

    dialog.setPreferredSize(new Dimension(650, 575)); // width, height — adjust as needed

    JPanel paddedPanel = new JPanel(new BorderLayout());
    paddedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right
    paddedPanel.add(panel, BorderLayout.CENTER);
    dialog.add(paddedPanel, BorderLayout.CENTER);

    JPanel paddedButtonPanel = new JPanel(new BorderLayout());
    paddedButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 7, 7)); // top, left, bottom, right
    paddedButtonPanel.add(buttonPanel, BorderLayout.CENTER);
    dialog.add(paddedButtonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(parent);

    btnCancel.addActionListener(e -> dialog.dispose());

    btnOk.addActionListener(e -> {
        try {
            if (!tfFirstName.getText().trim().isEmpty()) emp.setFirstName(tfFirstName.getText().trim());
            if (!tfLastName.getText().trim().isEmpty()) emp.setLastName(tfLastName.getText().trim());
            if (!tfBirthday.getText().trim().isEmpty()) {
                try {
                    emp.setBirthday(LocalDate.parse(tfBirthday.getText().trim(), fmt));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Invalid birthday format. Birthday not updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (!tfAddress.getText().trim().isEmpty()) emp.getContact().setAddress(tfAddress.getText().trim());
            if (!tfPhone.getText().trim().isEmpty()) emp.getContact().setPhoneNumber(tfPhone.getText().trim());

            GovernmentID govID = emp.getGovernmentID();
            if (!tfSSS.getText().trim().isEmpty()) govID.setSss(tfSSS.getText().trim());
            if (!tfPhil.getText().trim().isEmpty()) govID.setPhilhealth(tfPhil.getText().trim());
            if (!tfTIN.getText().trim().isEmpty()) govID.setTin(tfTIN.getText().trim());
            if (!tfPagibig.getText().trim().isEmpty()) govID.setPagibig(tfPagibig.getText().trim());

            if (!tfStatus.getText().trim().isEmpty()) emp.setStatus(tfStatus.getText().trim());
            if (!tfPosition.getText().trim().isEmpty()) emp.getPosition().setPosition(tfPosition.getText().trim());

            if (!tfSupervisor.getText().trim().isEmpty()) {
                Employee supervisor = employees.stream()
                    .filter(e2 -> e2.getFullName().equalsIgnoreCase(tfSupervisor.getText().trim()))
                    .findFirst().orElse(null);
                emp.getPosition().setSupervisor(supervisor);
            }

            Compensation comp = emp.getCompensation();
            comp.setBasicSalary(parseDoubleOrKeep(tfBasic.getText(), comp.getBasicSalary()));
            comp.setRiceSubsidy(parseDoubleOrKeep(tfRice.getText(), comp.getRiceSubsidy()));
            comp.setPhoneAllowance(parseDoubleOrKeep(tfPhoneAllow.getText(), comp.getPhoneAllowance()));
            comp.setClothingAllowance(parseDoubleOrKeep(tfClothing.getText(), comp.getClothingAllowance()));
            comp.setSemiGross(parseDoubleOrKeep(tfSemi.getText(), comp.getSemiGross()));

            if (!tfHourly.getText().trim().isEmpty()) {
                emp.setHourlyRate(Double.parseDouble(tfHourly.getText().trim()));
            }

            CSVWriter.updateEmployeeInCSV(emp);
            
            // custom "Sucess" prompt
            JDialog successDialog = new JDialog(dialog, "Success", Dialog.ModalityType.APPLICATION_MODAL);
            successDialog.setLayout(new BorderLayout(10, 5));

            JLabel successLabel = new JLabel("Employee record updated successfully!");
            successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            successLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

            JButton okBtn = new JButton("OK");
            styleButtonSmall(okBtn);
            okBtn.setBackground(new Color(0x4F5EC0));
            okBtn.setForeground(Color.WHITE);
            okBtn.addActionListener(e2 -> successDialog.dispose());

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // "ok" button padding
            btnPanel.add(okBtn);

            successDialog.add(successLabel, BorderLayout.CENTER);
            successDialog.add(btnPanel, BorderLayout.SOUTH);
            successDialog.pack();
            successDialog.setLocationRelativeTo(dialog);
            successDialog.setVisible(true);

            dialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Invalid number entered. Some fields were not updated.", "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Failed to update employee record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    dialog.setVisible(true);
}     

    // this is a helper method for parsing doubles or returning old value if empty
    private double parseDoubleOrKeep(String input, double oldValue) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            return oldValue;
        }
        return Double.parseDouble(input.trim());
    }


        private void salaryCalculationMenu(Component parent) { // THIS IS DONE
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Salary Calculation", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        String[] options = {
            "Gross Salary for ALL employees",
            "Net Salary for ALL employees",
            "Gross Salary for specific employee",
            "Net Salary for specific employee",
            "Close"
        };

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        menuPanel.add(Box.createVerticalGlue());

        for (String option : options) {
            JButton button = new JButton(option);
            styleButton(button);
            button.setPreferredSize(new Dimension(300, 40));
            button.setMaximumSize(new Dimension(300, 40));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(button);

            button.addActionListener(e -> {
                switch (option) {
                    case "Gross Salary for ALL employees" -> {
                        YearMonth ym = promptYearMonth(dialog);
                        if (ym == null) return;

                        String[] columnNames = {"Employee Number", "Full Name", "Gross Salary"};
                        Object[][] data = new Object[employees.size()][3];
                        int i = 0;
                        for (Employee emp : employees) {
                            double gross = payrollService.calculateMonthlySalary(emp, ym);
                            data[i][0] = emp.getEmployeeNumber();
                            data[i][1] = emp.getFullName();
                            data[i][2] = String.format("%.2f", gross);
                            i++;
                        }

                        JTable table = new JTable(data, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        table.setFillsViewportHeight(true);

                        JDialog tableDialog = new JDialog(dialog, "Gross Salaries for " + ym, true);
                        tableDialog.setSize(600, 400);
                        tableDialog.setLocationRelativeTo(dialog);
                        tableDialog.add(scrollPane);
                        tableDialog.setVisible(true);
                    }

                    case "Net Salary for ALL employees" -> {
                        YearMonth ym = promptYearMonth(dialog);
                        if (ym == null) return;

                        String[] columnNames = {"Employee Number", "Full Name", "Net Salary"};
                        Object[][] data = new Object[employees.size()][3];
                        int i = 0;
                        for (Employee emp : employees) {
                            double net = payrollService.calculateNetSalary(emp, ym);
                            data[i][0] = emp.getEmployeeNumber();
                            data[i][1] = emp.getFullName();
                            data[i][2] = String.format("%.2f", net);
                            i++;
                        }

                        JTable table = new JTable(data, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        table.setFillsViewportHeight(true);

                        JDialog tableDialog = new JDialog(dialog, "Net Salaries for " + ym, true);
                        tableDialog.setSize(600, 400);
                        tableDialog.setLocationRelativeTo(dialog);
                        tableDialog.add(scrollPane);
                        tableDialog.setVisible(true);
                    }

                    case "Gross Salary for specific employee" -> {
                        Employee emp = promptEmployeeNumber(dialog);
                        if (emp == null) return;
                        YearMonth ym = promptYearMonth(dialog);
                        if (ym == null) return;
                        double gross = payrollService.calculateMonthlySalary(emp, ym);
                        StyledInfoDialog(dialog, "Gross Salary", String.format("Gross Salary for %s in %s: %.2f", emp.getFullName(), ym, gross));
                    }

                    case "Net Salary for specific employee" -> {
                        Employee emp = promptEmployeeNumber(dialog);
                        if (emp == null) return;
                        YearMonth ym = promptYearMonth(dialog);
                        if (ym == null) return;
                        double net = payrollService.calculateNetSalary(emp, ym);
                        StyledInfoDialog(dialog, "Net Salary", String.format("Net Salary for %s in %s: %.2f", emp.getFullName(), ym, net));
                    }

                    case "Close" -> dialog.dispose();
                }
            });
        }

        menuPanel.add(Box.createVerticalGlue());
        dialog.add(menuPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void exportPayrollReport(Component parent) { // Find way to edit view or design
        YearMonth ym = promptYearMonth(parent);
        if (ym == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File to Save Payroll Report");
        fileChooser.setSelectedFile(new File("payroll_report_" + ym + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                payrollService.exportPayrollReportToCSV(employees, ym, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Payroll report exported to:\n" + fileToSave.getAbsolutePath());
            } catch (Exception e) {
                StyledErrorDialog(this, "Error", "Error exporting CSV: " + e.getMessage());
            }
        }
    }
    
    private void addNewEmp(Component parent) { // code for adding new employee data christine's project
    try {
        int empNum = Integer.parseInt(StyledInputDialog(this, "Add Employee", "Enter Employee Number:"));
        String firstName = StyledInputDialog(this, "Add Employee", "Enter First Name:");
        String lastName = StyledInputDialog(this, "Add Employee", "Enter Last Name:");

        String birthdayStr = StyledInputDialog(this, "Add Employee", "Enter Birthday (MM/dd/yyyy):");
        LocalDate birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        String address = StyledInputDialog(this, "Add Employee", "Enter Address:");
        String phone = StyledInputDialog(this, "Add Employee", "Enter Phone Number:");

        String sss = StyledInputDialog(this, "Add Employee", "Enter SSS #: ");
        String philhealth = StyledInputDialog(this, "Add Employee", "Enter PhilHealth #: ");
        String tin = StyledInputDialog(this, "Add Employee", "Enter TIN #: ");
        String pagibig = StyledInputDialog(this, "Add Employee", "Enter Pag-ibig #: ");

        String status = StyledInputDialog(this, "Add Employee", "Enter Status (Regular, Contractual, etc.): ");
        String posTitle = StyledInputDialog(this, "Add Employee", "Enter Position Title: ");

        String supervisorName = StyledInputDialog(this, "Add Employee", "Immediate Supervisor (full name or leave blank):");
        Employee supervisor = employees.stream()
                .filter(e -> e.getFullName().equalsIgnoreCase(supervisorName))
                .findFirst()
                .orElse(null);

        double basic = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Basic Salary: "));
        double rice = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Rice Subsidy: "));
        double phoneAllow = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Phone Allowance: "));
        double clothing = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Clothing Allowance: "));
        double semiGross = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Gross Semi-Monthly Rate: "));
        double hourly = Double.parseDouble(StyledInputDialog(this, "Add Employee", "Enter Hourly Rate: "));

        Employee emp = new Employee(
                empNum,
                firstName,
                lastName,
                birthday,
                new ContactInfo(address, phone),
                new GovernmentID(sss, philhealth, tin, pagibig),
                new Position(posTitle, supervisor),
                new Compensation(basic, rice, phoneAllow, clothing, semiGross, hourly),
                status,
                "" // department or additional data if needed
        );

        // FIXED: Add to list first, then write to CSV
        employees.add(emp);
        CSVWriter.writeAllEmployeesToCSV(employees);

        JDialog successDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Success", Dialog.ModalityType.APPLICATION_MODAL);
        successDialog.setLayout(new BorderLayout(10, 10));

        JLabel successLabel = new JLabel("New employee added successfully!");
        successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        successLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

        JButton okBtn = new JButton("OK");
        styleButtonSmall(okBtn);
        okBtn.setBackground(new Color(0x4F5EC0));
        okBtn.setForeground(Color.WHITE);
        okBtn.addActionListener(e2 -> successDialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        btnPanel.add(okBtn);

        successDialog.add(successLabel, BorderLayout.CENTER);
        successDialog.add(btnPanel, BorderLayout.SOUTH);
        successDialog.pack();
        successDialog.setLocationRelativeTo(parent);
        successDialog.setVisible(true);


    } catch (Exception e) {
        StyledErrorDialog(this, "Error", "Failed to add employee: " + e.getMessage());
    }
}

    private YearMonth promptYearMonth(Component parent) {
    String input = StyledInputDialog(parent, "Enter Month and Year", "Enter month and year (yyyy-MM):");
    if (input == null) return null;
    try {
        return YearMonth.parse(input.trim());
    } catch (Exception e) {
        StyledErrorDialog(parent, "Error", "Invalid format. Use yyyy-MM.");
        return null;
    }
}
    private Employee promptEmployeeNumber(Component parent) {
        String input = StyledInputDialog(parent, "Enter Employee Number", "Enter employee number:");
        if (input == null) return null; // User cancelled

        try {
            int empNum = Integer.parseInt(input.trim());
            Employee emp = employeeService.findEmployeeByNumber(empNum);
            if (emp == null) {
                StyledErrorDialog(parent, "Error", "Employee not found.");
            }
            return emp;
        } catch (NumberFormatException e) {
            StyledErrorDialog(parent, "Error", "Invalid employee number.");
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
    
    private String StyledInputDialog(Component parent, String title, String message) {
        JDialog inputDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), title, ModalityType.APPLICATION_MODAL);
        inputDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inputDialog.setSize(380, 170);
        inputDialog.setResizable(false);
        inputDialog.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(new Color(250, 252, 255));

        JLabel promptLabel = new JLabel(message);
        promptLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        promptLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(325, 28));
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        styleButtonSmall(okButton);
        styleButtonSmall(cancelButton);
        okButton.setPreferredSize(new Dimension(100, 36));
        cancelButton.setPreferredSize(new Dimension(100, 36));
        okButton.setMaximumSize(new Dimension(100, 36));
        cancelButton.setMaximumSize(new Dimension(100, 36));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(16, 0)));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());

        final String[] result = new String[1];
        result[0] = null;

        okButton.addActionListener(e -> {
            result[0] = inputField.getText().trim();
            inputDialog.dispose();
        });
        cancelButton.addActionListener(e -> {
            result[0] = null;
            inputDialog.dispose();
        });

        mainPanel.add(promptLabel);
        mainPanel.add(inputField);
        mainPanel.add(buttonPanel);

        inputDialog.setContentPane(mainPanel);
        inputDialog.getRootPane().setDefaultButton(okButton);
        inputDialog.setVisible(true);

        return result[0];
    }
    
    private void StyledErrorDialog(Component parent, String title, String message) {
        JDialog errorDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
        errorDialog.setLayout(new BorderLayout(10, 10));
        errorDialog.setSize(340, 150);
        errorDialog.setLocationRelativeTo(parent);
        errorDialog.getContentPane().setBackground(new Color(245, 248, 250));

        // Icon and message panel
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 18));
        contentPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        contentPanel.add(iconLabel);
        contentPanel.add(messageLabel);

        // OK button
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 32));
        okButton.setMaximumSize(new Dimension(80, 32));
        okButton.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
        okButton.setForeground(java.awt.Color.WHITE);
        okButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0x4f, 0x5e, 0xc0), 1, false));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> errorDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        errorDialog.add(contentPanel, BorderLayout.CENTER);
        errorDialog.add(buttonPanel, BorderLayout.SOUTH);
        errorDialog.setVisible(true);
    }
    
    private void StyledPayrollPromptDialog(Component parent, String title, String[] lines) { // NEW HELPER FOR PAYROLL SUMMARY PROMPT
        JDialog payrollDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
        payrollDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        payrollDialog.setSize(500, 600);
        payrollDialog.setResizable(false);
        payrollDialog.setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        contentPanel.setBackground(new Color(245, 248, 250));

        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        Font plainFont = new Font("Segoe UI", Font.PLAIN, 14);

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                contentPanel.add(Box.createVerticalStrut(10));
                continue;
            }

            JLabel label;
            if (line.startsWith("---")) {
                label = new JLabel(line.replace("---", "").trim());
                label.setFont(boldFont);
                label.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));
            } else {
                label = new JLabel(line);
                label.setFont(plainFont);
            }
            contentPanel.add(label);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 248, 250));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(14, 18, 12, 18));

        JButton okButton = new JButton("OK");
        styleButtonSmall(okButton);
        okButton.setPreferredSize(new Dimension(100, 36));
        okButton.setMaximumSize(new Dimension(100, 36));
        okButton.addActionListener(e -> payrollDialog.dispose());

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalGlue());

        payrollDialog.setLayout(new BorderLayout());
        payrollDialog.add(scrollPane, BorderLayout.CENTER);
        payrollDialog.add(buttonPanel, BorderLayout.SOUTH);
        payrollDialog.getRootPane().setDefaultButton(okButton);
        payrollDialog.setVisible(true);
}

    private void StyledInfoDialog(Component parent, String title, String message) {
    JDialog dialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setLayout(new BorderLayout(10, 10));
    dialog.setSize(350, 180);
    dialog.setLocationRelativeTo(parent);

    JTextArea messageArea = new JTextArea(message);
    messageArea.setEditable(false);
    messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    messageArea.setWrapStyleWord(true);
    messageArea.setLineWrap(true);
    messageArea.setBackground(dialog.getBackground());
    messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton okButton = new JButton("OK");
    okButton.setPreferredSize(new Dimension(60, 30));
    okButton.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
    okButton.setForeground(java.awt.Color.WHITE);
    okButton.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0x4f, 0x5e, 0xc0), 1, false));
    okButton.setFocusPainted(false);
    okButton.addActionListener(e -> dialog.dispose());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(okButton);

    dialog.add(new JScrollPane(messageArea), BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}

    private class BackgroundPanel extends JPanel {
        private BufferedImage bgImage;
        public BackgroundPanel() {
            try {
                bgImage = ImageIO.read(getClass().getResource("/assets/bg_color.png"));
            } catch (Exception e) {
                System.err.println("Background image not found: " + e.getMessage());
            }
            setLayout(new GridBagLayout()); // To center the login panel
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private class RoundedPanel extends JPanel {
        private int cornerRadius = 40;
        public RoundedPanel() {
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Draw drop shadow
            g2.setColor(new Color(0,0,0,40));
            g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, cornerRadius, cornerRadius);
            // Draw white panel
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Sharp-edged text field panel for login UI
    private class SharpTextFieldPanel extends JPanel {
        public SharpTextFieldPanel(JTextField field) {
            this(field, 260);
        }
        public SharpTextFieldPanel(JTextField field, int width) {
            setOpaque(false);
            setLayout(new BorderLayout());
            field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            field.setOpaque(false);
            add(field, BorderLayout.CENTER);
            setPreferredSize(new Dimension(width, 40));
            setBorder(BorderFactory.createLineBorder(new Color(0x1F2937), 1, false));
        }
    }

    // Add this helper for toggle button styling
    private void styleToggleButton(JButton button, boolean selected) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        if (selected) {
            button.setBackground(new Color(0xF7F7FA)); // Very light gray
            button.setFont(button.getFont().deriveFont(Font.BOLD));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 2, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
            ));
        } else {
            button.setBackground(new Color(240, 242, 245));
            button.setFont(button.getFont().deriveFont(Font.PLAIN));
            button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        }
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
}

    // Helper for info row with rounded text field
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Inter", Font.PLAIN, 14));
        l.setForeground(new Color(0x1F2937));
        JTextField v = new JTextField(value);
        v.setFont(new Font("Inter", Font.PLAIN, 14));
        v.setEditable(false);
        v.setBackground(Color.WHITE);
        v.setPreferredSize(new Dimension(238, 26));
        v.setMaximumSize(new Dimension(238, 26));
        v.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        return row;
}

    private void showViewAddEmployeeList(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "View/Add Employee", true);
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Table setup (no Status column)
        String[] columns = {
            "Employee Number", "Last Name", "First Name",
            "SSS", "PhilHealth", "TIN", "Pag-IBIG"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable employeeTable = new JTable(tableModel);
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
        // Modern table design (copied from View/Add Employee)
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        employeeTable.setRowHeight(32);
        employeeTable.setShowGrid(false);
        employeeTable.setIntercellSpacing(new Dimension(0, 0));
        employeeTable.setSelectionBackground(new Color(0x4F5EC0));
        employeeTable.setSelectionForeground(Color.WHITE);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        employeeTable.getTableHeader().setBackground(new Color(0x4F5EC0));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel at the bottom
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 18, 10));

        // View Employee button
        JButton viewBtn = new JButton("View Employee");
        styleButtonSmall(viewBtn);
        viewBtn.setPreferredSize(new Dimension(160, 38));
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        viewBtn.setEnabled(false);
        viewBtn.setBackground(new java.awt.Color(0x4F, 0x5E, 0xC0));
        viewBtn.setForeground(java.awt.Color.WHITE);
        btnPanel.add(viewBtn);

        // New Employee button
        JButton addBtn = new JButton("New Employee");
        styleButtonSmall(addBtn);
        addBtn.setPreferredSize(new Dimension(180, 38));
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addBtn.setBackground(new java.awt.Color(0x4F, 0x5E, 0xC0));
        addBtn.setForeground(java.awt.Color.WHITE);
        btnPanel.add(addBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Enable view button only when a row is selected
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = employeeTable.getSelectedRow();
            viewBtn.setEnabled(row >= 0);
        });

        // View Employee logic
        viewBtn.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row == -1) {
                StyledErrorDialog(dialog, "Error", "Select an employee from the table.");
                return;
            }
            int empNum = (int) tableModel.getValueAt(row, 0);
            Employee emp = employeeService.findEmployeeByNumber(empNum);
            if (emp == null) {
                StyledErrorDialog(dialog, "Error", "Employee not found.");
                return;
            }
            showDetailedEmployeeDialog(dialog, emp);
        });

        // New Employee logic: open a single form dialog
        addBtn.addActionListener(e -> {
            JDialog addDialog = new JDialog(dialog, "New Employee", true);
            addDialog.setSize(600, 700);
            addDialog.setLocationRelativeTo(dialog);
            addDialog.setLayout(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 8, 0); // vertical gap between rows
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.NONE;

            Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
            // Fields
            JLabel lblEmpNum = new JLabel("Employee Number"); lblEmpNum.setFont(labelFont); lblEmpNum.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfEmpNum = new JTextField();
            JLabel lblFirstName = new JLabel("First Name"); lblFirstName.setFont(labelFont); lblFirstName.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfFirstName = new JTextField();
            JLabel lblLastName = new JLabel("Last Name"); lblLastName.setFont(labelFont); lblLastName.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfLastName = new JTextField();
            JLabel lblBirthday = new JLabel("Birthday (MM/dd/yyyy)"); lblBirthday.setFont(labelFont); lblBirthday.setHorizontalAlignment(SwingConstants.LEFT);
            String[] months = java.util.Arrays.stream(java.time.Month.values())
                .map(m -> m.toString().substring(0,1) + m.toString().substring(1).toLowerCase().substring(0,2)).toArray(String[]::new);
            JComboBox<String> cbMonth = new JComboBox<>(months);
            Integer[] days = java.util.stream.IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);
            JComboBox<Integer> cbDay = new JComboBox<>(days);
            int currentYear = java.time.LocalDate.now().getYear();
            Integer[] years = java.util.stream.IntStream.rangeClosed(currentYear-70, currentYear-16).boxed().sorted((a,b)->b-a).toArray(Integer[]::new);
            JComboBox<Integer> cbYear = new JComboBox<>(years);
            JPanel birthdayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            birthdayPanel.add(cbMonth);
            birthdayPanel.add(cbDay);
            birthdayPanel.add(cbYear);
            JLabel lblAddress = new JLabel("Address"); lblAddress.setFont(labelFont); lblAddress.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfAddress = new JTextField();
            JLabel lblPhone = new JLabel("Phone"); lblPhone.setFont(labelFont); lblPhone.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfPhone = new JTextField();
            JLabel lblSSS = new JLabel("SSS"); lblSSS.setFont(labelFont); lblSSS.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfSSS = new JTextField();
            JLabel lblPhil = new JLabel("PhilHealth"); lblPhil.setFont(labelFont); lblPhil.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfPhil = new JTextField();
            JLabel lblTIN = new JLabel("TIN"); lblTIN.setFont(labelFont); lblTIN.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfTIN = new JTextField();
            JLabel lblPagibig = new JLabel("Pag-ibig"); lblPagibig.setFont(labelFont); lblPagibig.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfPagibig = new JTextField();
            JLabel lblStatus = new JLabel("Status"); lblStatus.setFont(labelFont); lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfStatus = new JTextField();
            JLabel lblPosition = new JLabel("Position"); lblPosition.setFont(labelFont); lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfPosition = new JTextField();
            JLabel lblSupervisor = new JLabel("Supervisor"); lblSupervisor.setFont(labelFont); lblSupervisor.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfSupervisor = new JTextField();
            JLabel lblBasic = new JLabel("Basic Salary"); lblBasic.setFont(labelFont); lblBasic.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfBasic = new JTextField();
            JLabel lblRice = new JLabel("Rice Subsidy"); lblRice.setFont(labelFont); lblRice.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfRice = new JTextField();
            JLabel lblPhoneAllow = new JLabel("Phone Allowance"); lblPhoneAllow.setFont(labelFont); lblPhoneAllow.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfPhoneAllow = new JTextField();
            JLabel lblClothing = new JLabel("Clothing Allowance"); lblClothing.setFont(labelFont); lblClothing.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfClothing = new JTextField();
            JLabel lblSemi = new JLabel("Semi-Monthly"); lblSemi.setFont(labelFont); lblSemi.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfSemi = new JTextField();
            JLabel lblHourly = new JLabel("Hourly Rate"); lblHourly.setFont(labelFont); lblHourly.setHorizontalAlignment(SwingConstants.LEFT);
            JTextField tfHourly = new JTextField();

            // Add fields to form using GridBagLayout
            int row = 0;
            Object[][] labelFieldPairs = {
                {lblEmpNum, tfEmpNum},
                {lblFirstName, tfFirstName},
                {lblLastName, tfLastName},
                {lblBirthday, birthdayPanel},
                {lblAddress, tfAddress},
                {lblPhone, tfPhone},
                {lblSSS, tfSSS},
                {lblPhil, tfPhil},
                {lblTIN, tfTIN},
                {lblPagibig, tfPagibig},
                {lblStatus, tfStatus},
                {lblPosition, tfPosition},
                {lblSupervisor, tfSupervisor},
                {lblBasic, tfBasic},
                {lblRice, tfRice},
                {lblPhoneAllow, tfPhoneAllow},
                {lblClothing, tfClothing},
                {lblSemi, tfSemi},
                {lblHourly, tfHourly}
            };
            for (Object[] pair : labelFieldPairs) {
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.NONE;
                formPanel.add((JLabel)pair[0], gbc);
                gbc.gridx = 1;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                formPanel.add((Component)pair[1], gbc);
                row++;
            }

            // Make textfield border thicker and font larger, not bold
            Font textFieldFont = new Font("Segoe UI", Font.PLAIN, 16);
            javax.swing.border.Border thinGrayBorder = BorderFactory.createLineBorder(new java.awt.Color(176, 176, 176), 1);
            JTextField[] allFields = {tfEmpNum, tfFirstName, tfLastName, tfAddress, tfPhone, tfSSS, tfPhil, tfTIN, tfPagibig, tfStatus, tfPosition, tfSupervisor, tfBasic, tfRice, tfPhoneAllow, tfClothing, tfSemi, tfHourly};
            for (JTextField field : allFields) {
                field.setFont(textFieldFont);
                field.setBorder(thinGrayBorder);
            }
            // Fix text clipping by increasing text field size
            Dimension fieldSize = new Dimension(0, 40);
            for (JTextField field : allFields) {
                field.setPreferredSize(fieldSize);
            }
            JScrollPane addEmpScrollPane = new JScrollPane(formPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            addEmpScrollPane.setBorder(null); // Remove default border for a cleaner look
            addDialog.add(addEmpScrollPane, BorderLayout.CENTER);
            // Save/Cancel buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 23, 0)); // Add extra bottom margin (15px more)
            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");
            saveBtn.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
            saveBtn.setForeground(java.awt.Color.WHITE);
            cancelBtn.setBackground(new java.awt.Color(0xC0, 0x4F, 0x51));
            cancelBtn.setForeground(java.awt.Color.WHITE);
            saveBtn.setFocusPainted(false);
            cancelBtn.setFocusPainted(false);
            saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            saveBtn.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));
            cancelBtn.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            addDialog.add(buttonPanel, BorderLayout.SOUTH);
            // Save logic
            saveBtn.addActionListener(ev -> {
                // Check all required fields before proceeding
                if (tfEmpNum.getText().trim().isEmpty() ||
                    tfFirstName.getText().trim().isEmpty() ||
                    tfLastName.getText().trim().isEmpty() ||
                    tfAddress.getText().trim().isEmpty() ||
                    tfPhone.getText().trim().isEmpty() ||
                    tfSSS.getText().trim().isEmpty() ||
                    tfPhil.getText().trim().isEmpty() ||
                    tfTIN.getText().trim().isEmpty() ||
                    tfPagibig.getText().trim().isEmpty() ||
                    tfStatus.getText().trim().isEmpty() ||
                    tfPosition.getText().trim().isEmpty() ||
                    tfBasic.getText().trim().isEmpty() ||
                    tfRice.getText().trim().isEmpty() ||
                    tfPhoneAllow.getText().trim().isEmpty() ||
                    tfClothing.getText().trim().isEmpty() ||
                    tfSemi.getText().trim().isEmpty() ||
                    tfHourly.getText().trim().isEmpty()) {
                    StyledErrorDialog(addDialog, "Error", "Please fill in all fields.");
                    return;
                }
                try {
                    int empNum = Integer.parseInt(tfEmpNum.getText().trim());
                    String firstName = tfFirstName.getText().trim();
                    String lastName = tfLastName.getText().trim();
                    int month = cbMonth.getSelectedIndex() + 1;
                    int day = (Integer) cbDay.getSelectedItem();
                    int year = (Integer) cbYear.getSelectedItem();
                    LocalDate birthday = LocalDate.of(year, month, day);
                    String address = tfAddress.getText().trim();
                    String phone = tfPhone.getText().trim();
                    String sss = tfSSS.getText().trim();
                    String philhealth = tfPhil.getText().trim();
                    String tin = tfTIN.getText().trim();
                    String pagibig = tfPagibig.getText().trim();
                    String status = tfStatus.getText().trim();
                    String posTitle = tfPosition.getText().trim();
                    String supervisorName = tfSupervisor.getText().trim();
                    Employee supervisor = employees.stream()
                            .filter(e2 -> e2.getFullName().equalsIgnoreCase(supervisorName))
                            .findFirst().orElse(null);
                    
                    // FIXED: Correct field mapping
                    double basic = Double.parseDouble(tfBasic.getText().trim());
                    double rice = Double.parseDouble(tfRice.getText().trim());
                    double phoneAllow = Double.parseDouble(tfPhoneAllow.getText().trim());
                    double clothing = Double.parseDouble(tfClothing.getText().trim());
                    double semiGross = Double.parseDouble(tfSemi.getText().trim());
                    double hourly = Double.parseDouble(tfHourly.getText().trim());
                    
                    Employee emp = new Employee(
                            empNum, firstName, lastName, birthday,
                            new ContactInfo(address, phone),
                            new GovernmentID(sss, philhealth, tin, pagibig),
                            new Position(posTitle, supervisor),
                            new Compensation(basic, rice, phoneAllow, clothing, semiGross, hourly),
                            status, "");
                    
                    // FIXED: Add to list first, then write to CSV
                    employees.add(emp);
                    com.compprog1282025.util.CSVWriter.writeAllEmployeesToCSV(employees);
                    
                    tableModel.addRow(new Object[]{
                        emp.getEmployeeNumber(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getGovernmentID().getSss(),
                        emp.getGovernmentID().getPhilhealth(),
                        emp.getGovernmentID().getTin(),
                        emp.getGovernmentID().getPagibig()
                    });
                    // Also update Update/Delete Employee table if open
                    if (updateDeleteTableModel != null) {
                        updateDeleteTableModel.addRow(new Object[]{
                            emp.getEmployeeNumber(),
                            emp.getLastName(),
                            emp.getFirstName(),
                            emp.getGovernmentID().getSss(),
                            emp.getGovernmentID().getPhilhealth(),
                            emp.getGovernmentID().getTin(),
                            emp.getGovernmentID().getPagibig()
                        });
                    }
                    addDialog.dispose();
                    JDialog successDialog = new JDialog(dialog, "Success", true);
                    successDialog.setLayout(new BorderLayout(10, 10));
                    JLabel successLabel = new JLabel("New employee added successfully!");
                    successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    successLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
                    JButton okBtn = new JButton("OK");
                    styleButtonSmall(okBtn);
                    okBtn.setBackground(new Color(0x4F5EC0));
                    okBtn.setForeground(Color.WHITE);
                    okBtn.addActionListener(e2 -> successDialog.dispose());
                    JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    btnPanel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    btnPanel2.add(okBtn);
                    successDialog.add(successLabel, BorderLayout.CENTER);
                    successDialog.add(btnPanel2, BorderLayout.SOUTH);
                    successDialog.pack();
                    successDialog.setLocationRelativeTo(dialog);
                    successDialog.setVisible(true);
                } catch (Exception ex) {
                    String msg = ex.getMessage();
                    if (msg == null || msg.trim().isEmpty()) {
                        msg = "An unexpected error occurred. Please check your input.";
                    }
                    StyledErrorDialog(addDialog, "Error", "Failed to add employee: " + msg);
                }
            });
            cancelBtn.addActionListener(ev -> addDialog.dispose());
            addDialog.setVisible(true);
        });

        dialog.setVisible(true);
    }

    private void showUpdateDeleteEmployeeList(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Update/Delete Employee", true);
        dialog.setSize(1400, 900);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Table setup (no Status column)
        String[] columns = {
            "Employee Number", "Last Name", "First Name",
            "SSS", "PhilHealth", "TIN", "Pag-IBIG"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable employeeTable = new JTable(tableModel);
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
        // Modern table design (copied from View/Add Employee)
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        employeeTable.setRowHeight(32);
        employeeTable.setShowGrid(false);
        employeeTable.setIntercellSpacing(new Dimension(0, 0));
        employeeTable.setSelectionBackground(new Color(0x4F5EC0));
        employeeTable.setSelectionForeground(Color.WHITE);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        employeeTable.getTableHeader().setBackground(new Color(0x4F5EC0));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Employee Data Pane (right, scrollable)
        JPanel formPanel = new JPanel(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder("Employee Data");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        formPanel.setBorder(border);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 15);
        String[] labels = {"Employee Number", "First Name", "Last Name", "Birthday", "Position", "Position Type", "Status", "Supervisor", "Address", "Phone", "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #", "Basic Salary", "Hourly Rate", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Semi-monthly Gross"};
        JComponent[] valueFields = new JComponent[labels.length];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            JLabel l = new JLabel(labels[i] + ":");
            l.setFont(labelFont);
            l.setHorizontalAlignment(SwingConstants.LEFT);
            formPanel.add(l, gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            if ("Address".equals(labels[i])) {
                JTextArea addressArea = new JTextArea();
                addressArea.setFont(valueFont);
                addressArea.setLineWrap(true);
                addressArea.setWrapStyleWord(true);
                addressArea.setRows(2);
                addressArea.setEditable(false);
                addressArea.setOpaque(false);
                addressArea.setBorder(null);
                addressArea.setFocusable(false);
                formPanel.add(addressArea, gbc);
                valueFields[i] = addressArea;
            } else if ("Birthday".equals(labels[i])) {
                String[] months = java.util.Arrays.stream(java.time.Month.values())
                    .map(m -> m.toString().substring(0,1) + m.toString().substring(1).toLowerCase().substring(0,2)).toArray(String[]::new);
                JComboBox<String> cbMonth = new JComboBox<>(months);
                Integer[] days = java.util.stream.IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);
                JComboBox<Integer> cbDay = new JComboBox<>(days);
                int currentYear = java.time.LocalDate.now().getYear();
                Integer[] years = java.util.stream.IntStream.rangeClosed(currentYear-70, currentYear-16).boxed().sorted((a,b)->b-a).toArray(Integer[]::new);
                JComboBox<Integer> cbYear = new JComboBox<>(years);
                JPanel birthdayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
                birthdayPanel.setOpaque(false);
                birthdayPanel.add(cbMonth);
                birthdayPanel.add(cbDay);
                birthdayPanel.add(cbYear);
                formPanel.add(birthdayPanel, gbc);
                valueFields[i] = birthdayPanel;
            } else {
                JTextField v = new JTextField();
                v.setFont(valueFont);
                v.setEditable(false);
                v.setBorder(BorderFactory.createLineBorder(new java.awt.Color(176, 176, 176), 1));
                v.setBackground(Color.WHITE);
                v.setHorizontalAlignment(SwingConstants.LEFT);
                formPanel.add(v, gbc);
                valueFields[i] = v;
            }
        }
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setPreferredSize(new Dimension(520, 500));
        
        // Save button for editing
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
        saveBtn.setForeground(java.awt.Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));
        saveBtn.setVisible(false);
        JPanel saveBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        saveBtnPanel.setOpaque(false);
        saveBtnPanel.add(saveBtn);
        
        // Add below the scroll pane
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(formScroll, BorderLayout.CENTER);
        rightPanel.add(saveBtnPanel, BorderLayout.SOUTH);
        dialog.add(rightPanel, BorderLayout.EAST);

        // Update fields on row selection
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = employeeTable.getSelectedRow();
                if (row != -1) {
                    Employee emp = employees.stream()
                        .filter(em -> em.getEmployeeNumber() == (int) tableModel.getValueAt(row, 0))
                        .findFirst().orElse(null);
                    if (emp != null) {
                        Object[] values = {
                            String.valueOf(emp.getEmployeeNumber()),
                            emp.getFirstName(),
                            emp.getLastName(),
                            emp.getBirthday(),
                            emp.getPosition().getPosition(),
                            emp.getPosition().getPosition(),
                            emp.getStatus(),
                            emp.getPosition().getSupervisor() != null ? emp.getPosition().getSupervisor().getFullName() : "None",
                            emp.getContact().getAddress(),
                            emp.getContact().getPhoneNumber(),
                            emp.getGovernmentID().getSss(),
                            emp.getGovernmentID().getPhilhealth(),
                            emp.getGovernmentID().getTin(),
                            emp.getGovernmentID().getPagibig(),
                            "₱" + emp.getCompensation().getBasicSalary(),
                            "₱" + emp.getHourlyRate(),
                            "₱" + emp.getCompensation().getRiceSubsidy(),
                            "₱" + emp.getCompensation().getPhoneAllowance(),
                            "₱" + emp.getCompensation().getClothingAllowance(),
                            "₱" + emp.getCompensation().getSemiGross()
                        };
                        for (int i = 0; i < valueFields.length; i++) {
                            if (valueFields[i] instanceof JTextField v) {
                                v.setText(values[i].toString());
                            } else if (valueFields[i] instanceof JTextArea a) {
                                a.setText(values[i].toString());
                            } else if (valueFields[i] instanceof JPanel p && i == 3) { // Birthday dropdowns
                                LocalDate bday = (LocalDate) values[i];
                                JComboBox<String> cbMonth = (JComboBox<String>) p.getComponent(0);
                                JComboBox<Integer> cbDay = (JComboBox<Integer>) p.getComponent(1);
                                JComboBox<Integer> cbYear = (JComboBox<Integer>) p.getComponent(2);
                                cbMonth.setSelectedIndex(bday.getMonthValue() - 1);
                                cbDay.setSelectedItem(bday.getDayOfMonth());
                                cbYear.setSelectedItem(bday.getYear());
                            }
                        }
                        // Make all fields non-editable by default
                        for (int i = 0; i < valueFields.length; i++) {
                            if (valueFields[i] instanceof JTextField v) {
                                v.setEditable(false);
                            } else if (valueFields[i] instanceof JTextArea a) {
                                a.setEditable(false);
                            } else if (valueFields[i] instanceof JPanel p && i == 3) {
                                for (Component c : p.getComponents()) {
                                    c.setEnabled(false);
                                }
                            }
                        }
                        saveBtn.setVisible(false);
                    }
                } else {
                    for (JComponent v : valueFields) {
                        if (v instanceof JTextField f) f.setText("");
                        else if (v instanceof JTextArea a) a.setText("");
                        else if (v instanceof JPanel p) {
                            JComboBox<String> cbMonth = (JComboBox<String>) p.getComponent(0);
                            JComboBox<Integer> cbDay = (JComboBox<Integer>) p.getComponent(1);
                            JComboBox<Integer> cbYear = (JComboBox<Integer>) p.getComponent(2);
                            cbMonth.setSelectedIndex(0);
                            cbDay.setSelectedIndex(0);
                            cbYear.setSelectedIndex(0);
                        }
                    }
                    saveBtn.setVisible(false);
                }
            }
        });

        // Buttons panel (Update, Delete)
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        // Match style to View Employee/New Employee
        updateBtn.setPreferredSize(new Dimension(180, 38));
        deleteBtn.setPreferredSize(new Dimension(180, 38));
        updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        updateBtn.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
        deleteBtn.setBackground(new java.awt.Color(0x4f, 0x5e, 0xc0));
        updateBtn.setForeground(java.awt.Color.WHITE);
        deleteBtn.setForeground(java.awt.Color.WHITE);
        updateBtn.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0x4f, 0x5e, 0xc0), 1, false));
        deleteBtn.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0x4f, 0x5e, 0xc0), 1, false));
        updateBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Enable buttons only when a row is selected
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = employeeTable.getSelectedRow();
            updateBtn.setEnabled(row >= 0);
            deleteBtn.setEnabled(row >= 0);
        });

        // Update action
        updateBtn.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row < 0) return;
            // Make all fields editable including Employee Number
            for (int i = 0; i < valueFields.length; i++) {
                if (valueFields[i] instanceof JTextField v) {
                    v.setEditable(true);
                } else if (valueFields[i] instanceof JTextArea a) {
                    a.setEditable(true);
                } else if (valueFields[i] instanceof JPanel p && i == 3) {
                    for (Component c : p.getComponents()) {
                        c.setEnabled(true);
                    }
                }
            }
            saveBtn.setVisible(true);
        });

        // Save action
        saveBtn.addActionListener(e -> {
            System.out.println("Save button clicked!");
            int row = employeeTable.getSelectedRow();
            if (row < 0) return;
            int empNum = (int) tableModel.getValueAt(row, 0);
            int index = -1;
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeNumber() == empNum) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                System.out.println("Employee not found in list!");
                StyledErrorDialog(dialog, "Error", "Employee not found. Please refresh the list.");
                reloadEmployeesAndRefreshTable(tableModel);
                return;
            }
            Employee emp = employees.get(index);
            try {
                int newEmpNum = Integer.parseInt(((JTextField)valueFields[0]).getText().trim());
                emp.setEmployeeNumber(newEmpNum);
                emp.setFirstName(((JTextField)valueFields[1]).getText().trim());
                emp.setLastName(((JTextField)valueFields[2]).getText().trim());
                // Birthday
                JPanel bdayPanel = (JPanel) valueFields[3];
                JComboBox<String> cbMonth = (JComboBox<String>) bdayPanel.getComponent(0);
                JComboBox<Integer> cbDay = (JComboBox<Integer>) bdayPanel.getComponent(1);
                JComboBox<Integer> cbYear = (JComboBox<Integer>) bdayPanel.getComponent(2);
                int month = cbMonth.getSelectedIndex() + 1;
                int day = (Integer) cbDay.getSelectedItem();
                int year = (Integer) cbYear.getSelectedItem();
                emp.setBirthday(LocalDate.of(year, month, day));
                emp.getContact().setAddress(((JTextArea)valueFields[8]).getText().trim());
                emp.getContact().setPhoneNumber(((JTextField)valueFields[9]).getText().trim());
                emp.getGovernmentID().setSss(((JTextField)valueFields[10]).getText().trim());
                emp.getGovernmentID().setPhilhealth(((JTextField)valueFields[11]).getText().trim());
                emp.getGovernmentID().setTin(((JTextField)valueFields[12]).getText().trim());
                emp.getGovernmentID().setPagibig(((JTextField)valueFields[13]).getText().trim());
                emp.setStatus(((JTextField)valueFields[6]).getText().trim());
                emp.getPosition().setPosition(((JTextField)valueFields[4]).getText().trim());
                // Compensation
                emp.getCompensation().setBasicSalary(Double.parseDouble(((JTextField)valueFields[14]).getText().replace("₱", "").replace(",", "").trim()));
                emp.setHourlyRate(Double.parseDouble(((JTextField)valueFields[15]).getText().replace("₱", "").replace(",", "").trim()));
                emp.getCompensation().setRiceSubsidy(Double.parseDouble(((JTextField)valueFields[16]).getText().replace("₱", "").replace(",", "").trim()));
                emp.getCompensation().setPhoneAllowance(Double.parseDouble(((JTextField)valueFields[17]).getText().replace("₱", "").replace(",", "").trim()));
                emp.getCompensation().setClothingAllowance(Double.parseDouble(((JTextField)valueFields[18]).getText().replace("₱", "").replace(",", "").trim()));
                emp.getCompensation().setSemiGross(Double.parseDouble(((JTextField)valueFields[19]).getText().replace("₱", "").replace(",", "").trim()));
                System.out.println("Updated: " + emp.getEmployeeNumber() + " " + emp.getFirstName() + " " + emp.getLastName());
                System.out.println("Writing to CSV...");
                com.compprog1282025.util.CSVWriter.writeAllEmployeesToCSV(employees);
                System.out.println("CSV write done.");
                reloadEmployeesAndRefreshTable(tableModel);
                if (tableModel.getRowCount() > 0) {
                    System.out.println("Table refreshed. First row: " + tableModel.getValueAt(0, 0));
                } else {
                    System.out.println("Table refreshed. No rows.");
                }
                // Make all fields non-editable again
                for (int i = 0; i < valueFields.length; i++) {
                    if (valueFields[i] instanceof JTextField v) {
                        v.setEditable(false);
                    } else if (valueFields[i] instanceof JTextArea a) {
                        a.setEditable(false);
                    } else if (valueFields[i] instanceof JPanel p && i == 3) {
                        for (Component c : p.getComponents()) {
                            c.setEnabled(false);
                        }
                    }
                }
                saveBtn.setVisible(false);
                // Show success dialog
                JDialog successDialog = new JDialog(dialog, "Success", true);
                successDialog.setLayout(new BorderLayout(10, 10));
                JLabel successLabel = new JLabel("Employee updated successfully!");
                successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                successLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
                JButton okBtn = new JButton("OK");
                styleButtonSmall(okBtn);
                okBtn.setBackground(new Color(0x4F5EC0));
                okBtn.setForeground(Color.WHITE);
                okBtn.addActionListener(e2 -> successDialog.dispose());
                JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                btnPanel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                btnPanel2.add(okBtn);
                successDialog.add(successLabel, BorderLayout.CENTER);
                successDialog.add(btnPanel2, BorderLayout.SOUTH);
                successDialog.pack();
                successDialog.setLocationRelativeTo(dialog);
                successDialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                StyledErrorDialog(dialog, "Error", "Invalid or missing value: " + ex.getMessage());
            }
        });

        // Delete action
        deleteBtn.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row < 0) return;
            int empNum = (int) tableModel.getValueAt(row, 0);
            Employee emp = employeeService.findEmployeeByNumber(empNum);
            if (emp == null) {
                StyledErrorDialog(dialog, "Error", "Employee not found.");
                return;
            }
            // Confirm dialog
            JDialog confirmDialog = new JDialog(dialog, "Confirm Deletion", Dialog.ModalityType.APPLICATION_MODAL);
            confirmDialog.setLayout(new BorderLayout(10, 10));
            JLabel messageLabel = new JLabel("Are you sure you want to delete " + emp.getFullName() + "?");
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
            JButton yesBtn = new JButton("Yes");
            JButton noBtn = new JButton("No");
            // Set plain font, size, and color directly (not bold)
            Font dialogBtnFont = new Font("Segoe UI", Font.PLAIN, 16);
            Dimension dialogBtnSize = new Dimension(100, 38);
            yesBtn.setFont(dialogBtnFont);
            noBtn.setFont(dialogBtnFont);
            yesBtn.setPreferredSize(dialogBtnSize);
            noBtn.setPreferredSize(dialogBtnSize);
            yesBtn.setBackground(new Color(0x4F5EC0));
            noBtn.setBackground(new Color(0x4F5EC0));
            yesBtn.setForeground(Color.WHITE);
            noBtn.setForeground(Color.WHITE);
            yesBtn.setFocusPainted(false);
            noBtn.setFocusPainted(false);
            yesBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
            noBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
            yesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            noBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            yesBtn.addActionListener(e2 -> {
                try {
                    CSVWriter.deleteEmployeeFromCSV(empNum);
                    employees.remove(emp);
                    tableModel.removeRow(row);
                    // Success dialog
                    JDialog deletedDialog = new JDialog(dialog, "Success", Dialog.ModalityType.APPLICATION_MODAL);
                    deletedDialog.setLayout(new BorderLayout(10, 10));
                    JLabel successLabel = new JLabel("Employee deleted!");
                    successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    successLabel.setBorder(BorderFactory.createEmptyBorder(10, 75, 5, 75));
                    JButton okBtn = new JButton("OK");
                    okBtn.setFont(dialogBtnFont);
                    okBtn.setPreferredSize(dialogBtnSize);
                    okBtn.setBackground(new Color(0x4F5EC0));
                    okBtn.setForeground(Color.WHITE);
                    okBtn.setFocusPainted(false);
                    okBtn.setBorder(BorderFactory.createLineBorder(new Color(0x4F5EC0), 1, false));
                    okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    okBtn.addActionListener(e3 -> deletedDialog.dispose());
                    JPanel btnPanel = new JPanel();
                    btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                    btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                    btnPanel.add(Box.createHorizontalGlue());
                    btnPanel.add(okBtn);
                    btnPanel.add(Box.createHorizontalGlue());
                    deletedDialog.add(successLabel, BorderLayout.CENTER);
                    deletedDialog.add(btnPanel, BorderLayout.SOUTH);
                    deletedDialog.pack();
                    deletedDialog.setLocationRelativeTo(dialog);
                    deletedDialog.setVisible(true);
                } catch (Exception ex) {
                    StyledErrorDialog(dialog, "Error", "Failed to delete employee: " + ex.getMessage());
                }
                confirmDialog.dispose();
            });
            noBtn.addActionListener(e2 -> confirmDialog.dispose());
            // Use BoxLayout for button panel to respect button sizes
            JPanel btnPanel = new JPanel();
            btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            btnPanel.add(Box.createHorizontalGlue());
            btnPanel.add(yesBtn);
            btnPanel.add(Box.createRigidArea(new Dimension(16, 0)));
            btnPanel.add(noBtn);
            btnPanel.add(Box.createHorizontalGlue());
            confirmDialog.add(messageLabel, BorderLayout.CENTER);
            confirmDialog.add(btnPanel, BorderLayout.SOUTH);
            confirmDialog.pack();
            confirmDialog.setLocationRelativeTo(dialog);
            confirmDialog.setVisible(true);
        });

        dialog.setVisible(true);

        // When dialog closes, clear static reference
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                updateDeleteTableModel = null;
            }
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                updateDeleteTableModel = null;
            }
        }); // <-- This closes the addWindowListener

    } // <-- ADD THIS to close showUpdateDeleteEmployeeList

    // Helper to reload employees from CSV and refresh the table
    private void reloadEmployeesAndRefreshTable(DefaultTableModel tableModel) {
        try {
            employees.clear();
            employees.addAll(com.compprog1282025.util.CSVReader.readEmployeesFromCSV("data/employees.csv"));
            // Clear and repopulate the table
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
        } catch (Exception ex) {
            StyledErrorDialog(this, "Error", "Failed to reload employees: " + ex.getMessage());
        }
    }

    }