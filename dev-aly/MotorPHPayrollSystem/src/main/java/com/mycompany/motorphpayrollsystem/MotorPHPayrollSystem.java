/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorphpayrollsystem;

import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.Admin;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.Compensation;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.ContactInfo;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.Employee;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.GovernmentID;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.LoginManager;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.Position;
import com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular.Payroll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author aliss
 */

public class MotorPHPayrollSystem {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        List<Admin> admins = new ArrayList<>();

        // Create supervisor placeholder (Admin as top-level)
        Admin ceo = new Admin(
            10001, "Manuel", "Garcia", LocalDate.of(1983, 10, 11),
            new ContactInfo("Valero Street, Makati", "966-860-270"),
            new GovernmentID("44-4506057-3", "820126853951", "442-605-657-000", "691295330870"),
            "Regular",
            new Position("Chief Executive Officer", null),
            new Compensation(90000, 1500, 2000, 1000, 535.71),
            "adminpass",
            "admin1"
        );
        admins.add(ceo);

        // Create employee under the CEO
        Employee antonio = new Employee(
            10002, "Antonio", "Lim", LocalDate.of(1988, 6, 19),
            new ContactInfo("Dasmarinas Village, Makati", "171-867-411"),
            new GovernmentID("52-2061274-9", "331735646338", "683-102-776-000", "663904995411"),
            "Regular",
            new Position("COO", ceo),
            new Compensation(60000, 1500, 2000, 1000, 357.14),
            "limpass"
        );
        employees.add(antonio);

        // Setup login manager
        LoginManager loginManager = new LoginManager(employees, admins);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to MotorPH Payroll System");
        System.out.print("Login as (1) Employee or (2) Admin: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter employee number: ");
                int empNum = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter password: ");
                String empPass = scanner.nextLine();
                Employee e = loginManager.authenticateEmployee(empNum, empPass);
                if (e != null) {
                    System.out.println("\nLogin successful!\n");
                    System.out.println(e.getInfoSummary());

                    // Generate payslip
                    Payroll payroll = new Payroll(e);
                    payroll.generatePayslip();
                } else {
                    System.out.println("Invalid employee login.");
                }
                break;
            case 2:
                System.out.print("Enter admin username: ");
                String user = scanner.nextLine();
                System.out.print("Enter password: ");
                String adminPass = scanner.nextLine();
                Admin a = loginManager.authenticateAdmin(user, adminPass);
                if (a != null) {
                    System.out.println("\nAdmin login successful!\n");
                    System.out.println(a.getAdminInfo());
                } else {
                    System.out.println("Invalid admin login.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}
