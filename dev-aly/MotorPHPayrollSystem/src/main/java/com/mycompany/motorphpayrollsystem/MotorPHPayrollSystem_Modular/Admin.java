/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

/**
 *
 * @author aliss
 */
public class Admin extends Employee {

    private String username;

    public Admin(int employeeNumber, String firstName, String lastName,
            java.time.LocalDate birthday, ContactInfo contact,
            GovernmentID governmentID, String status, Position position,
            Compensation compensation, String password, String username) {

        super(employeeNumber, firstName, lastName, birthday,
                contact, governmentID, status, position, compensation, password);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String inputUser, String inputPass) {
        return this.username.equals(inputUser) && this.getPassword().equals(inputPass);
    }

    public String getAdminInfo() {
        return String.format("Admin: %s (Username: %s)", getFullName(), username);
    }
}
