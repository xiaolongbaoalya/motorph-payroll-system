package com.compprog1282025;

import com.compprog1282025.auth.UserRole;
import com.compprog1282025.model.Employee;
import com.compprog1282025.model.GovernmentID;
import com.compprog1282025.model.Position;
import com.compprog1282025.model.Attendance;
import com.compprog1282025.model.Compensation;
import com.compprog1282025.model.ContactInfo;
import com.compprog1282025.service.EmployeeService;
import com.compprog1282025.service.AttendanceService;
import com.compprog1282025.service.DataLoaderService;
import com.compprog1282025.service.PayrollService;
import com.compprog1282025.service.ReportService;
import com.compprog1282025.util.CSVWriter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Load data
        DataLoaderService loader = new DataLoaderService();
        loader.loadAllData();

        List<Employee> employees = loader.getEmployees();
        List<Attendance> attendanceRecords = loader.getAttendanceRecords();

        // Initialize services
        AttendanceService attendanceService = new AttendanceService(attendanceRecords);
        PayrollService payrollService = new PayrollService(attendanceService);
        EmployeeService employeeService = new EmployeeService(employees);
        ReportService reportService = new ReportService(payrollService, attendanceService);

        Scanner scanner = new Scanner(System.in);

        // Simulate role selection
        System.out.println("Login as:");
        System.out.println("1. Employee");
        System.out.println("2. Admin");
        System.out.print("Enter your role (1 or 2): ");
        String roleInput = scanner.nextLine().trim();

        UserRole role;
        if (roleInput.equals("1")) {
            role = UserRole.EMPLOYEE;
        } else if (roleInput.equals("2")) {
            role = UserRole.ADMIN;
        } else {
            System.out.println("Invalid role. Exiting.");
            scanner.close();
            return;
        }

    if (role == UserRole.EMPLOYEE) {
    System.out.print("Enter your employee number: ");
    int empNum = Integer.parseInt(scanner.nextLine());

    Employee employee = employees.stream()
        .filter(e -> e.getEmployeeNumber() == empNum)
        .findFirst()
        .orElse(null);

    if (employee == null) {
        System.out.println("Employee not found.");
        return;
    }

    System.out.println("\n=== Employee Self-Service ===");
    System.out.println("Name: " + employee.getFullName());
    System.out.println("Position: " + employee.getPosition());
    System.out.println("Hourly Rate: " + employee.getHourlyRate());
    System.out.println("Compensation:");
    System.out.printf("  Rice Subsidy: %.2f%n", employee.getCompensation().getRiceSubsidy());
    System.out.printf("  Phone Allowance: %.2f%n", employee.getCompensation().getPhoneAllowance());
    System.out.printf("  Clothing Allowance: %.2f%n", employee.getCompensation().getClothingAllowance());

    while (true) {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. View Monthly Gross/Net Salary");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter target month and year (yyyy-MM): ");
            String monthInput = scanner.nextLine().trim();
            try {
                YearMonth targetMonth = YearMonth.parse(monthInput);
                double gross = payrollService.calculateMonthlySalary(employee, targetMonth);
                double net = payrollService.calculateNetSalary(employee, targetMonth);

                System.out.println("\n=== Payroll Summary for " + targetMonth + " ===");
                System.out.printf("Gross Salary: %.2f%n", gross);
                System.out.printf("Net Salary: %.2f%n", net);
            } catch (Exception e) {
                System.out.println("Invalid month format. Please use yyyy-MM.");
            }

        } else if (choice.equals("2")) {
            System.out.println("Goodbye!");
            break;
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }

    } else if (role == UserRole.ADMIN) {
    while (true) {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. View All Employees");
        System.out.println("2. View Employee Record");
        System.out.println("3. Monthly Payroll Reports");
        System.out.println("4. Weekly Payroll Reports");
        System.out.println("5. Export Payroll Report to CSV");
        System.out.println("6. Add New Employee to Record");
        System.out.println("7. Exit");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                System.out.println("\n=== Employee List ===");
                employeeService.printAllEmployeeData();
                break;

            case "2":
                System.out.print("Enter Employee Number: ");
                try {
                    int empNum = Integer.parseInt(scanner.nextLine().trim());
                    Employee emp = employeeService.findEmployeeByNumber(empNum);
                    if (emp != null) {
                        employeeService.printEmployeeData(emp);
                    } else {
                        System.out.println("Employee not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid employee number.");
                }
            break;

        case "3":
            String monthlyChoice;
            do {
                System.out.println("\n--- Monthly Payroll Reports ---");
                System.out.println("a. Gross Salary for ALL employees");
                System.out.println("b. Net Salary for ALL employees");
                System.out.println("c. Gross Salary for a specific employee");
                System.out.println("d. Net Salary for a specific employee");
                System.out.println("e. Return to main menu");
                System.out.print("Select an option: ");
                monthlyChoice = scanner.nextLine().trim().toLowerCase();

                switch (monthlyChoice) {
                    case "a":
                        try {
                            System.out.print("Enter target month and year (yyyy-MM): ");
                            YearMonth grossAllMonth = YearMonth.parse(scanner.nextLine().trim());
                            for (Employee emp : employees) {
                                double gross = payrollService.calculateMonthlySalary(emp, grossAllMonth);
                                System.out.printf("Employee %d (%s): Gross Salary = %.2f%n",
                                        emp.getEmployeeNumber(), emp.getFullName(), gross);
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Use yyyy-MM.");
                        }
                        break;

                    case "b":
                        try {
                            System.out.print("Enter target month and year (yyyy-MM): ");
                            YearMonth netAllMonth = YearMonth.parse(scanner.nextLine().trim());
                            for (Employee emp : employees) {
                                double net = payrollService.calculateNetSalary(emp, netAllMonth);
                                System.out.printf("Employee %d (%s): Net Salary = %.2f%n",
                                        emp.getEmployeeNumber(), emp.getFullName(), net);
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Use yyyy-MM.");
                        }
                        break;

                    case "c":
                     try {
                            System.out.print("Enter employee number: ");
                            int empNum = Integer.parseInt(scanner.nextLine().trim());

                            Employee target = employeeService.findEmployeeByNumber(empNum);
                            if (target == null) {
                                System.out.println("Employee not found.");
                            break;
                            }

                            System.out.print("Enter target month and year (yyyy-MM): ");
                            YearMonth month = YearMonth.parse(scanner.nextLine().trim());

                            double gross = payrollService.calculateMonthlySalary(target, month);
                            System.out.printf("Gross Salary for %s in %s: %.2f%n", target.getFullName(), month, gross);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid employee number.");
                        } catch (Exception e) {
                        System.out.println("Invalid date format. Use yyyy-MM.");
                        }       
                        break;
                    case "d":
                        try {
                            System.out.print("Enter employee number: ");
                            int empNum = Integer.parseInt(scanner.nextLine().trim());
                            Employee target = employeeService.findEmployeeByNumber(empNum);
                            if (target == null) {
                                System.out.println("Employee not found.");
                                break;
                            }

                            System.out.print("Enter target month and year (yyyy-MM): ");
                            YearMonth month = YearMonth.parse(scanner.nextLine().trim());

                            if (monthlyChoice.equals("c")) {
                                double gross = payrollService.calculateMonthlySalary(target, month);
                                System.out.printf("Gross Salary for %s in %s: %.2f%n", target.getFullName(), month, gross);
                            } else {
                                double net = payrollService.calculateNetSalary(target, month);
                                System.out.printf("Net Salary for %s in %s: %.2f%n", target.getFullName(), month, net);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid employee number.");
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Use yyyy-MM.");
                        }
                        break;

                    case "e":
                        System.out.println("Returning to main menu...");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (!monthlyChoice.equals("e"));
            break;

        case "4":
            String weeklyChoice;
            do {
                System.out.println("\n--- Weekly Payroll Reports ---");
                System.out.println("a. Gross Salary for a specific employee");
                System.out.println("b. Net Salary for a specific employee");
                System.out.println("c. Return to main menu");
                System.out.print("Select an option: ");
                weeklyChoice = scanner.nextLine().trim().toLowerCase();

                switch (weeklyChoice) {
                    case "a":
                    case "b":
                        try {
                            System.out.print("Enter employee number: ");
                            int empNum = Integer.parseInt(scanner.nextLine().trim());
                            Employee target = employeeService.findEmployeeByNumber(empNum);
                            if (target == null) {
                                System.out.println("Employee not found.");
                                break;
                            }

                            System.out.print("Enter a reference date (yyyy-MM-dd): ");
                            LocalDate date = LocalDate.parse(scanner.nextLine().trim());

                            if (weeklyChoice.equals("a")) {
                                double gross = payrollService.calculateWeeklySalary(target, date);
                                System.out.printf("Gross Salary for %s: %.2f%n", target.getFullName(), gross);
                            } else {
                                double net = payrollService.calculateNetWeeklySalary(target, date);
                                System.out.printf("Net Salary for %s: %.2f%n", target.getFullName(), net);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid employee number.");
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Use yyyy-MM-dd.");
                        }
                        break;

                    case "c":
                        System.out.println("Returning to main menu...");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (!weeklyChoice.equals("c"));
            break;

        case "5":
            try {
                System.out.print("Enter target month and year (yyyy-MM): ");
                YearMonth exportMonth = YearMonth.parse(scanner.nextLine().trim());

                System.out.println("Enter the full path for the export CSV file.");
                System.out.println("Example: ./exports/payroll_report_2024-06.csv");
                System.out.print("File path: ");
                String filePath = scanner.nextLine().trim();

                reportService.exportPayrollReportToCSV(employees, exportMonth, filePath);
            } catch (Exception e) {
                System.out.println("Invalid input or error exporting CSV: " + e.getMessage());
            }
            break;

        case "6":
                        try {
                System.out.print("Employee Number: ");
                int empNum = Integer.parseInt(scanner.nextLine());

                System.out.print("First Name: ");
                String firstName = scanner.nextLine();

                System.out.print("Last Name: ");
                String lastName = scanner.nextLine();

                System.out.print("Birthday (MM/dd/yyyy): ");
                LocalDate birthday = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                System.out.print("Address: ");
                String address = scanner.nextLine();

                System.out.print("Phone Number: ");
                String phone = scanner.nextLine();

                System.out.print("SSS #: ");
                String sss = scanner.nextLine();

                System.out.print("Philhealth #: ");
                String philhealth = scanner.nextLine();

                System.out.print("TIN #: ");
                String tin = scanner.nextLine();

                System.out.print("Pag-ibig #: ");
                String pagibig = scanner.nextLine();

                System.out.print("Status (e.g., Regular, Contractual): ");
                String status = scanner.nextLine();

                System.out.print("Position Title: ");
                String pos = scanner.nextLine();

                System.out.print("Immediate Supervisor (full name or leave blank): ");
                String supervisorName = scanner.nextLine();
                Employee supervisor = employees.stream()
                        .filter(e -> e.getFullName().equalsIgnoreCase(supervisorName))
                        .findFirst()
                        .orElse(null);

                System.out.print("Basic Salary: ");
                double basic = Double.parseDouble(scanner.nextLine());

                System.out.print("Rice Subsidy: ");
                double rice = Double.parseDouble(scanner.nextLine());

                System.out.print("Phone Allowance: ");
                double phoneAllow = Double.parseDouble(scanner.nextLine());

                System.out.print("Clothing Allowance: ");
                double clothing = Double.parseDouble(scanner.nextLine());

                System.out.print("Gross Semi-monthly Rate: ");
                double semiGross = Double.parseDouble(scanner.nextLine());

                System.out.print("Hourly Rate: ");
                double hourly = Double.parseDouble(scanner.nextLine());

                Employee emp = new Employee(
                        empNum,
                        firstName,
                        lastName,
                        birthday,
                        new ContactInfo(address, phone),
                        new GovernmentID(sss, philhealth, tin, pagibig),
                        new Position(pos, supervisor),
                        new Compensation(basic, rice, phoneAllow, clothing, semiGross, hourly),
                        status,
                        "" // placeholder
                );

                CSVWriter.appendEmployeeToCSV(emp, "employees.csv");
                employees.add(emp);

                System.out.println("New employee added successfully!");

            } catch (Exception e) {
                System.out.println("Error creating employee: " + e.getMessage());
            }
            break;


        case "7":
            System.out.println("Goodbye!");
            return;

        default:
            System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}
