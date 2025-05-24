/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

/**
 *
 * @author aliss
 */

public class Payroll {
    private Employee employee;
    private double sss;
    private double philhealth;
    private double pagibig;
    private double tax;
    private double grossSalary;
    private double netSalary;

    public Payroll(Employee employee) {
        this.employee = employee;
        computeGross();
        computeDeductions();
        computeNetSalary();
    }

    private void computeGross() {
        Compensation comp = employee.getCompensation();
        grossSalary = comp.getBasicSalary() / 2 + comp.getRiceSubsidy() + comp.getPhoneAllowance() + comp.getClothingAllowance();
    }

    private void computeDeductions() {
        sss = 0.045 * (employee.getCompensation().getBasicSalary()); // Sample computation
        philhealth = 0.035 * (employee.getCompensation().getBasicSalary());
        pagibig = 100.0; // fixed
        tax = 0.10 * grossSalary; // flat 10% withholding
    }

    private void computeNetSalary() {
        double totalDeductions = sss + philhealth + pagibig + tax;
        netSalary = grossSalary - totalDeductions;
    }

    public void generatePayslip() {
        System.out.println("\n===== PAYSLIP =====");
        System.out.println("Employee: " + employee.getFullName());
        System.out.println("Position: " + employee.getPosition().getTitle());
        System.out.println("\n--- Gross Income ---");
        System.out.printf("Basic Salary (Semi-Monthly): %.2f\n", employee.getCompensation().getBasicSalary() / 2);
        System.out.printf("Rice Subsidy: %.2f\n", employee.getCompensation().getRiceSubsidy());
        System.out.printf("Phone Allowance: %.2f\n", employee.getCompensation().getPhoneAllowance());
        System.out.printf("Clothing Allowance: %.2f\n", employee.getCompensation().getClothingAllowance());
        System.out.printf("Gross Salary: %.2f\n", grossSalary);

        System.out.println("\n--- Deductions ---");
        System.out.printf("SSS: %.2f\n", sss);
        System.out.printf("PhilHealth: %.2f\n", philhealth);
        System.out.printf("Pag-IBIG: %.2f\n", pagibig);
        System.out.printf("Withholding Tax: %.2f\n", tax);

        System.out.printf("\nNet Salary: %.2f\n", netSalary);
        System.out.println("===================\n");
    }

    public double getNetSalary() {
        return netSalary;
    }
}
