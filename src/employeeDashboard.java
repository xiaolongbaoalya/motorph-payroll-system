/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Xhen Rii
 */
import javax.swing.*;
import java.awt.*;

public class EmployeeDashboard extends JFrame {
    private JButton btnPayroll;
    private JButton btnAttendance;
    private JButton btnMrInfo;
    private JButton btnChngPfp;
    private JLabel lblPfp;

    /**
     * Creates new form employeeDashboard
     */
    public EmployeeDashboard() {
        setTitle("Employee Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        lblPfp = new JLabel();
        lblPfp.setBounds(20, 20, 100, 100);
        lblPfp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(lblPfp);

        btnChngPfp = new JButton("Change Profile Picture");
        btnChngPfp.setBounds(130, 60, 180, 30);
        btnChngPfp.addActionListener(e -> JOptionPane.showMessageDialog(this, "Successfully uploaded"));
        add(btnChngPfp);

        btnPayroll = new JButton("Payroll");
        btnPayroll.setBounds(20, 140, 130, 30);
        btnPayroll.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Payroll"));
        add(btnPayroll);

        btnAttendance = new JButton("Attendance");
        btnAttendance.setBounds(170, 140, 130, 30);
        btnAttendance.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Attendance"));
        add(btnAttendance);

        btnMrInfo = new JButton("More Info");
        btnMrInfo.setBounds(95, 190, 130, 30);
        btnMrInfo.addActionListener(e -> JOptionPane.showMessageDialog(this, "More Info"));
        add(btnMrInfo);
    }

    private void onChangePfp() {
        JOptionPane.showMessageDialog(this, "Successfully uploaded");
    }

    private void btnChngPfpActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Successfully uploaded");
    }

    private void btnMrInfoActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "More Info");
    }

    private void btnPyrllActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Open Payroll");
    }

    private void btnAtndActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Open Attendance");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> new EmployeeDashboard().setVisible(true));
    }
}
