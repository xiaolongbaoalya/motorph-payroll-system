import java.io.*;
import java.util.*;

public class payrollSystem {
    // class to represent the Attendance Record side
    class attendanceRecord {
        private int employeeNumber;
        private String lastName;
        private String firstName;
        private String date;
        private String logIn;
        private String logOut;

    public attendanceRecord(int employeeNumber, String lastName, String firstName, String date, String logIn, String logOut) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        }

    @Override
    public String toString() {
        return "Employee # " + employeeNumber + ", Name: " + firstName + " " + lastName + ", Date: " + date + ", Log In: " + logIn + ", Log Out: " + logOut;
        }
    }

    // method to handle employee login
    public static boolean employeeLogin(String username, String password) {
        return password.equals("arbitrary");
    }

    // method to handle admin login
    public static boolean adminLogin(String username, String password) {
        return password.equals("admin");
    }

    // method to load attendance records from CSV file into an ArrayList
    public static void loadAttendanceFromCSV(String fileName) {
        ArrayList<attendanceRecord> attendanceList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // skip the header line
            br.readLine();

            // read each line from the CSV
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split by comma

                // ensure the line has the correct number of fields
                // there are always 6 fields in each line
                if (data.length == 6) {
                    int employeeNumber = Integer.parseInt(data[0].trim());
                    String lastName = data[1].trim();
                    String firstName = data[2].trim();
                    String date = data[3].trim();
                    String logIn = data[4].trim();
                    String logOut = data[5].trim();

                    // create a new attendanceRecord object and add it to the list
                    attendanceRecord record = new attendanceRecord(employeeNumber, lastName, firstName, date, logIn, logOut);
                    attendanceList.add(record);
                }
            }

            // display loaded attendance records
            System.out.println("Attendance records loaded successfully:");
            for (attendanceRecord record : attendanceList) {
                System.out.println(record);
            }

        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // displays login options
            System.out.println("\nMotorPH Payroll Portal");
            System.out.println("Select your side:");
            System.out.println("1. Employee Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Load Attendance Records from CSV");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1:
                    // Employee login
                    System.out.print("Enter Employee Username: ");
                    String empUsername = scanner.nextLine();
                    System.out.print("Enter Employee Password: ");
                    String empPassword = scanner.nextLine();

                    if (employeeLogin(empUsername, empPassword)) {
                        System.out.println("Employee login successful!");
                        // proceed with employee side functionalities
                    } else {
                        System.out.println("Invalid login details for Employee.");
                    }
                    break;

                case 2:
                    // Admin login
                    System.out.print("Enter Admin Username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter Admin Password: ");
                    String adminPassword = scanner.nextLine();

                    if (adminLogin(adminUsername, adminPassword)) {
                        System.out.println("Admin login successful!");
                        // proceed with admin side functionalities
                    } else {
                        System.out.println("Invalid login details for Admin.");
                    }
                    break;

                case 3:
                    // load attendance records from CSV
                    loadAttendanceFromCSV("attendance.csv");
                    break;

                case 4:
                    System.out.println("Exiting the Payroll System.");
                    scanner.close();
                    // exit the program
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

