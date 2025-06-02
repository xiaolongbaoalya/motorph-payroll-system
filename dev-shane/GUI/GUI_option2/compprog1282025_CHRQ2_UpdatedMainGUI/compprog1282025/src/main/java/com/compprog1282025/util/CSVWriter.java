package com.compprog1282025.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.compprog1282025.model.Employee;

public class CSVWriter {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void appendEmployeeToCSV(Employee employee, String filepath) throws IOException {
        try (com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(new FileWriter(filepath, true))) {
            String[] record = new String[] {
                String.valueOf(employee.getEmployeeNumber()),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getBirthday().format(dateFormatter),
                employee.getContact().getAddress(),
                employee.getContact().getPhoneNumber(),
                employee.getGovernmentID().getSss(),
                employee.getGovernmentID().getPhilhealth(),
                employee.getGovernmentID().getTin(),
                employee.getGovernmentID().getPagibig(),
                employee.getStatus(),
                employee.getPosition().getPosition(),
                employee.getPosition().getSupervisor() != null ? employee.getPosition().getSupervisor().getFullName() : "",
                formatDouble(employee.getCompensation().getBasicSalary()),
                formatDouble(employee.getCompensation().getRiceSubsidy()),
                formatDouble(employee.getCompensation().getPhoneAllowance()),
                formatDouble(employee.getCompensation().getClothingAllowance()),
                formatDouble(employee.getCompensation().getSemiGross()),
                formatDouble(employee.getCompensation().getHourlyRate())
            };

            writer.writeNext(record);
        }
    }

    private static String formatDouble(double value) {
        return String.format("\"%,.0f\"", value); // Format as quoted string like "90,000"
    }
}
