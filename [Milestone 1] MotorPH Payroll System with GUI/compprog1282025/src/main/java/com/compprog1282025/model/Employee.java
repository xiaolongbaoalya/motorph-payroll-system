package com.compprog1282025.model;

import java.time.LocalDate;

public class Employee extends Person {
    protected int employeeNumber;
    protected LocalDate birthday;
    protected ContactInfo contact;
    protected GovernmentID governmentID;
    protected Position position;
    protected Compensation compensation;
    protected String status;
    protected String password;

    // Constructor
    public Employee(int employeeNumber, String firstName, String lastName, LocalDate birthday,
                    ContactInfo contact, GovernmentID governmentID, Position position,
                    Compensation compensation, String status, String password) {
        super(firstName, lastName);
        this.employeeNumber = employeeNumber;
        this.birthday = birthday;
        this.contact = contact;
        this.governmentID = governmentID;
        this.position = position;
        this.compensation = compensation;
        this.status = status;
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

    public Position getPosition() {
        return position;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    public String getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public double getHourlyRate() {
        return compensation.getHourlyRate();
    }

    public String getBasicInfo() {
        return String.format("ID: %d, Name: %s, Position: %s, Status: %s",
                employeeNumber, getFullName(), position.getPosition(), status);
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    @Override
    public String toString() {
    return "[" + employeeNumber + "] " + firstName + " " + lastName + "\n"
        + " - Birthday: " + birthday + "\n"
        + " - Address: " + contact.getAddress() + "\n"
        + " - Phone: " + contact.getPhoneNumber() + "\n"
        + " - Position: " + position.getPosition() + "\n"
        + " - Supervisor: " + (position.getSupervisor() != null
                                ? position.getSupervisor().getFirstName() + " " + position.getSupervisor().getLastName()
                                : "None") + "\n"
        + " - Salary: " + compensation.getBasicSalary() + "\n"
        + " - Status: " + status + "\n"
        + " - SSS #: " + governmentID.getSss() + "\n"
        + " - PhilHealth #: " + governmentID.getPhilhealth() + "\n"
        + " - TIN #: " + governmentID.getTin() + "\n"
        + " - Pag-IBIG #: " + governmentID.getPagibig() + "\n"
        + " - Rice Subsidy: " + compensation.getRiceSubsidy() + "\n"
        + " - Phone Allowance: " + compensation.getPhoneAllowance() + "\n"
        + " - Clothing Allowance: " + compensation.getClothingAllowance() + "\n"
        + " - Gross Semi-monthly Rate: " + compensation.getSemiGross() + "\n"
        + " - Hourly Rate: " + compensation.getHourlyRate();
    }

}
