
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private Map<String, String> credentialsMap;

    public LoginGUI() {
        setTitle("MotorPH Employee Login");
        setSize(400, 220);  // Slightly reduced height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        credentialsMap = loadCredentials("credentials.csv");

        // Panel for form content
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Minimal padding

        // Combine all panels
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(messagePanel, BorderLayout.NORTH);

        // Event handling
        loginButton.addActionListener(e -> authenticate());

        setVisible(true);
    }

    private Map<String, String> loadCredentials(String filename) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read credentials file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return map;
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (credentialsMap.containsKey(username) && credentialsMap.get(username).equals(password)) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Login successful!");
            // TODO: Redirect to main dashboard
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("❗ Invalid username or password.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
