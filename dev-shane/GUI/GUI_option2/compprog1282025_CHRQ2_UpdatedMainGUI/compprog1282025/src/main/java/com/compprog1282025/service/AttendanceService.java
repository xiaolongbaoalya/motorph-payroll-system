package com.compprog1282025.service;

import com.compprog1282025.model.Attendance;
import com.compprog1282025.model.Employee;

import java.time.YearMonth;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class AttendanceService {

    private final List<Attendance> attendanceRecords;

    public AttendanceService(List<Attendance> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    public double calculateMonthlyHours(Employee employee, YearMonth month) {
        return attendanceRecords.stream()
                .filter(a -> a.getEmployee().equals(employee))
                .filter(a -> YearMonth.from(a.getDate()).equals(month))
                .mapToDouble(a -> {
                    Duration duration = Duration.between(a.getTimeIn(), a.getTimeOut());
                    // Defensive: if timeOut is before timeIn (should not happen), return 0
                    return duration.isNegative() ? 0 : duration.toMinutes() / 60.0;
                })
                .sum();
    }

    public double calculateFixedWeekHours(Employee employee, LocalDate referenceDate) {
        LocalDate[] weekRange = calculateFixedWeekRange(referenceDate);
        LocalDate startDate = weekRange[0];
        LocalDate endDate = weekRange[1];

        return attendanceRecords.stream()
            .filter(a -> a.getEmployee().equals(employee))
            .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
            .mapToDouble(a -> {
                Duration duration = Duration.between(a.getTimeIn(), a.getTimeOut());
                return duration.isNegative() ? 0 : duration.toMinutes() / 60.0;
            })
        .sum();
    }


    public LocalDate[] calculateFixedWeekRange(LocalDate referenceDate) {
        // Anchor date: known start of first week in pattern (3-7 June 2024)
        LocalDate anchorStart = LocalDate.of(2024, 6, 3);

        // Calculate days between reference and anchor
        long daysSinceAnchor = Duration.between(anchorStart.atStartOfDay(), referenceDate.atStartOfDay()).toDays();

        // Determine which cycle (0-indexed) the reference date falls into
        long cycleIndex = daysSinceAnchor / 7;  // 5 work days + 2-day gap = 7-day cycle

        // Calculate start of the current cycle
        LocalDate weekStart = anchorStart.plusDays(cycleIndex * 7);
        LocalDate weekEnd = weekStart.plusDays(4);  // 5-day week

        return new LocalDate[]{weekStart, weekEnd};
}

    public boolean hasAttendanceForMonth(Employee employee, YearMonth month) {
        return attendanceRecords.stream()
                .anyMatch(a -> a.getEmployee().equals(employee) && YearMonth.from(a.getDate()).equals(month));
    }

}
