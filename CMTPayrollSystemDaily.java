public class CMTPayrollSystemDaily {
    public void calculatePayroll() {
        try (Scanner scanner = new Scanner(System.in)) {
            String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

            String timeInInput, timeOutInput;
            while (true) {
                try {
                    timeInInput = getValidatedInput(scanner, "Enter Time-In (HH:MM): ", timePattern);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    timeOutInput = getValidatedInput(scanner, "Enter Time-Out (HH:MM): ", timePattern);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            // Convert time strings to hours and minutes separately
            int timeInHours = Integer.parseInt(timeInInput.split(":")[0]);
            int timeInMinutes = Integer.parseInt(timeInInput.split(":")[1]);
            int timeOutHours = Integer.parseInt(timeOutInput.split(":")[0]);
            int timeOutMinutes = Integer.parseInt(timeOutInput.split(":")[1]);

            double totalHoursWorked = (timeOutHours - timeInHours) + ((timeOutMinutes - timeInMinutes) / 60.0);

            double hourlyRate = getValidatedDouble(scanner, "Enter Hourly Rate (no commas): ");
            double basicSalary = getValidatedDouble(scanner, "Enter Basic Salary (no commas): ");
            double sssContribution = getValidatedDouble(scanner, "Enter SSS Contribution (no commas): ");

            double hourlyRateSalary = totalHoursWorked * hourlyRate;

            double philHealth = (basicSalary * 0.05) / 2;
            double pagIbig = 200;

            double philHealthDaily = philHealth / 20;
            double sssDaily = sssContribution / 20;
            double pagIbigDaily = pagIbig / 20;
            double dailyDeductions = philHealthDaily + sssDaily + pagIbigDaily;

            System.out.println("\n[Daily Payslip]");
            System.out.printf("Total Hours Worked: %.2f hours\n", totalHoursWorked);
            System.out.printf("Salary Based on Hours Worked: %.2f Pesos\n", hourlyRateSalary);

            System.out.println("\nGovernment Contributions:");
            System.out.printf("PhilHealth - Monthly: %.2f, Daily: %.2f Pesos\n", philHealth, philHealthDaily);
            System.out.printf("SSS - Monthly: %.2f, Daily: %.2f Pesos\n", sssContribution, sssDaily);
            System.out.printf("Pag-Ibig - Monthly: %.2f, Daily: %.2f Pesos\n", pagIbig, pagIbigDaily);

            System.out.printf("\nTotal Daily Deductions: %.2f Pesos\n", dailyDeductions);
            System.out.printf("Daily Net Salary: %.2f Pesos\n", hourlyRateSalary - dailyDeductions);
        }
    }

    private String getValidatedInput(Scanner scanner, String prompt, String pattern) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (!input.matches(pattern)) {
            throw new IllegalArgumentException("<<Invalid input. Please follow HH:MM format.>>");
        }
        return input;
    }

    private double getValidatedDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (!input.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("<<Invalid input. Please enter a valid number without commas.>>");
        }
        return Double.parseDouble(input);
    }
}
