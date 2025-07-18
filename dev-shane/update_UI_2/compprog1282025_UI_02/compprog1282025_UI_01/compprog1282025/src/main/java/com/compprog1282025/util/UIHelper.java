package com.compprog1282025.util;

import javax.swing.*;
import java.awt.*;

public class UIHelper {

    // MotorPH modern theme settings
    private static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color BG_COLOR = new Color(250, 252, 255);          // Light panel background
    private static final Color FG_COLOR = new Color(33, 37, 41);             // Dark text
    private static final Color BORDER_COLOR = new Color(220, 220, 220);      // Light gray borders
    private static final Color BUTTON_COLOR = new Color(0x1c3680);           // MotorPH Blue
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public static void applyTheme(JComponent comp) {
        comp.setFont(DEFAULT_FONT);
        comp.setForeground(FG_COLOR);

        if (comp instanceof JButton btn) {
            btn.setBackground(BUTTON_COLOR);
            btn.setForeground(BUTTON_TEXT_COLOR);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        } else if (comp instanceof JTextField || comp instanceof JTextArea) {
            comp.setBackground(Color.WHITE);
            comp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        } else if (comp instanceof JComboBox<?> comboBox) {
            comboBox.setFont(DEFAULT_FONT);
            comboBox.setBackground(Color.WHITE);
            comboBox.setForeground(FG_COLOR);
            comboBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        } else {
            comp.setBackground(BG_COLOR);
        }
    }

    public static void applyThemeRecursively(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComponent jComp) {
                applyTheme(jComp);
            }
            if (comp instanceof Container nested) {
                applyThemeRecursively(nested);
            }
        }
    }
} 