/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayrollsystem.MotorPHPayrollSystem_Modular;

/**
 *
 * @author aliss
 */
public class GovernmentID {
    private String sss;
    private String philhealth;
    private String tin;
    private String pagibig;

    public GovernmentID(String sss, String philhealth, String tin, String pagibig) {
        this.sss = sss;
        this.philhealth = philhealth;
        this.tin = tin;
        this.pagibig = pagibig;
    }

    public String getSSS() {
        return sss;
    }

    public String getPhilhealth() {
        return philhealth;
    }

    public String getTIN() {
        return tin;
    }

    public String getPagibig() {
        return pagibig;
    }
}
