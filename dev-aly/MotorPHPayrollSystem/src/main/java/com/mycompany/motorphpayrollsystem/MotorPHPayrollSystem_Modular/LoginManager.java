/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

import java.util.List;

/**
 *
 * @author aliss
 */

public class LoginManager {
    private List<Employee> employeeList;
    private List<Admin> adminList;

    // Constructor
    public LoginManager(List<Employee> employeeList, List<Admin> adminList) {
        this.employeeList = employeeList;
        this.adminList = adminList;
    }

    // Employee login
    public Employee authenticateEmployee(int empNumber, String password) {
        for (Employee e : employeeList) {
            if (e.getEmployeeNumber() == empNumber && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null; // Not found or invalid credentials
    }

    // Admin login
    public Admin authenticateAdmin(String username, String password) {
        for (Admin a : adminList) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }
        return null; // Not found or invalid credentials
    }
}

