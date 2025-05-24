/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

import java.time.LocalDate;
/**
 *
 * @author aliss
 */

public class Employee extends Person {
    private int employeeNumber;
    private LocalDate birthday;
    private ContactInfo contact;
    private GovernmentID governmentID;
    private String status;
    private Position position;
    private Compensation compensation;
    private String password;

    // Constructor
    public Employee(int employeeNumber, String firstName, String lastName, LocalDate birthday,
                    ContactInfo contact, GovernmentID governmentID, String status,
                    Position position, Compensation compensation, String password) {
        super(firstName, lastName);
        this.employeeNumber = employeeNumber;
        this.birthday = birthday;
        this.contact = contact;
        this.governmentID = governmentID;
        this.status = status;
        this.position = position;
        this.compensation = compensation;
        this.password = password;
    }

    // Getters
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public GovernmentID getGovernmentID() {
        return governmentID;
    }

    public String getStatus() {
        return status;
    }

    public Position getPosition() {
        return position;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    public String getPassword() {
        return password;
    }

    // Business Logic
    public double getHourlyRate() {
        return compensation.getHourlyRate();
    }

    public Employee getSupervisor() {
        return position.getSupervisor();
    }

    public String getInfoSummary() {
        return String.format(
            "Employee #%d: %s\nBirthday: %s\nPosition: %s\nStatus: %s\nContact: %s\nCompensation: %.2f/hr",
            employeeNumber,
            getFullName(),
            birthday.toString(),
            position.getTitle(),
            status,
            contact.toString(),
            getHourlyRate()
        );
    }
}

