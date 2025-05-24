/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

/**
 *
 * @author aliss
 */
public class Position {
    private String title;
    private Employee supervisor; // Can be null if top-level

    public Position(String title, Employee supervisor) {
        this.title = title;
        this.supervisor = supervisor;
    }

    public String getTitle() {
        return title;
    }

    public Employee getSupervisor() {
        return supervisor;
    }
}
