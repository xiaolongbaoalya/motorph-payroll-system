import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class BirthdatePickerPanel extends JPanel {

    private JComboBox<Integer> yearBox;
    private JComboBox<String> monthBox;
    private JPanel dayPanel;
    private ButtonGroup dayGroup;
    private JLabel confirmationLabel;
    private Map<Integer, JToggleButton> dayButtons = new HashMap<>();
    private int selectedDay = 1;

    public BirthdatePickerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel birthdayLabel = new JLabel("Birthday");
        birthdayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        leftPanel.add(birthdayLabel);

        // Month + Year Dropdown Panel
        JPanel dateSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        Month[] months = Month.values();
        monthBox = new JComboBox<>(IntStream.range(0, 12).mapToObj(i -> Month.of(i + 1).name()).toArray(String[]::new));
        monthBox.setPreferredSize(new Dimension(120, 25));

        int currentYear = Year.now().getValue();
        Integer[] years = IntStream.rangeClosed(1900, currentYear).boxed()
                .sorted((a, b) -> b - a).toArray(Integer[]::new);
        yearBox = new JComboBox<>(years);
        yearBox.setPreferredSize(new Dimension(100, 25));

        dateSelectPanel.add(monthBox);
        dateSelectPanel.add(yearBox);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(dateSelectPanel);

        // Weekday headers
        JPanel weekdayPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel lbl = new JLabel(day, JLabel.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            weekdayPanel.add(lbl);
        }
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(weekdayPanel);

        // Day Grid (6 rows to cover months starting on Saturday)
        dayPanel = new JPanel(new GridLayout(6, 7, 5, 5));
        leftPanel.add(dayPanel);

        add(leftPanel, BorderLayout.CENTER);

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        confirmationLabel = new JLabel();
        confirmationLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        confirmationLabel.setForeground(new Color(0, 102, 204));
        updateConfirmation();

        rightPanel.add(Box.createVerticalStrut(60));
        rightPanel.add(new JLabel("Selected Date:"));
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(confirmationLabel);
        add(rightPanel, BorderLayout.EAST);

        // === Event Listeners ===
        monthBox.addActionListener(e -> rebuildDayButtons());
        yearBox.addActionListener(e -> rebuildDayButtons());

        rebuildDayButtons(); // Initial build
    }

    private void rebuildDayButtons() {
        dayPanel.removeAll();
        dayGroup = new ButtonGroup();
        dayButtons.clear();

        int year = (int) yearBox.getSelectedItem();
        int month = monthBox.getSelectedIndex() + 1;

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int maxDays = firstDayOfMonth.lengthOfMonth();
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0

        // Add blank buttons before the first day
        for (int i = 0; i < startDayOfWeek; i++) {
            dayPanel.add(new JLabel(""));
        }

        for (int i = 1; i <= maxDays; i++) {
            JToggleButton dayButton = new JToggleButton(String.valueOf(i));
            int day = i;

            dayButton.setPreferredSize(new Dimension(40, 30));
            dayButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dayButton.setMargin(new Insets(2, 2, 2, 2));
            if (i == selectedDay) {
                dayButton.setSelected(true);
            }

            dayButton.addActionListener(e -> {
                selectedDay = day;
                updateConfirmation();
            });

            dayGroup.add(dayButton);
            dayPanel.add(dayButton);
            dayButtons.put(i, dayButton);
        }

        // Fill remaining grid slots to keep layout consistent
        int totalCellsUsed = startDayOfWeek + maxDays;
        int remaining = (7 * 6) - totalCellsUsed;
        for (int i = 0; i < remaining; i++) {
            dayPanel.add(new JLabel(""));
        }

        dayPanel.revalidate();
        dayPanel.repaint();
        updateConfirmation();
    }

    private void updateConfirmation() {
        confirmationLabel.setText(getFormattedBirthdate());
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public int getSelectedYear() {
        return (int) yearBox.getSelectedItem();
    }

    public String getSelectedMonth() {
        return (String) monthBox.getSelectedItem();
    }

    public String getFormattedBirthdate() {
        return getSelectedMonth() + " " + selectedDay + ", " + getSelectedYear();
    }

    // Demo main
    public static void main(String[] args) {
        JFrame frame = new JFrame("Birthdate Picker Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 380);
        frame.setLayout(new BorderLayout());
        frame.add(new BirthdatePickerPanel(), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
