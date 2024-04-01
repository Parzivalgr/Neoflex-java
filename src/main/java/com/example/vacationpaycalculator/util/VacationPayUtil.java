package com.example.vacationpaycalculator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class VacationPayUtil {

    /**
     * Checks if a given date is a holiday or a weekend.
     *
     * @param date The date to check.
     * @return {@code true} if the date is either a holiday or a weekend, {@code false} otherwise.
     */
    private static boolean isHolidayOrWeekend(LocalDate date) {
        final String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd"));
        final int numberOfNonWorkDays = 5;
        // Check if the day is a weekend or a defined Russian holiday.
        return date.getDayOfWeek().getValue() > numberOfNonWorkDays || Stream.of(RussianHolidays.values())
                .map(RussianHolidays::getDate)
                .anyMatch(holiday -> holiday.equals(formattedDate));
    }

    /**
     * Calculates the vacation pay based on the average salary, number of vacation days,
     * and the specific vacation days provided.
     *
     * @param averageSalary The average monthly salary of the employee.
     * @param vacationDays The total number of vacation days.
     * @param exactVacationDays The list of exact vacation days (optional).
     * @return The calculated vacation pay.
     */
    public static double calculateVacationPay(Double averageSalary, Integer vacationDays, List<String> exactVacationDays) {
        int effectiveVacationDays = vacationDays;
        // If exact vacation days are provided, calculate effective vacation days excluding weekends and holidays.
        if (exactVacationDays != null && !exactVacationDays.isEmpty()) {
            effectiveVacationDays = (int) exactVacationDays.stream()
                    .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                    .filter(date -> !isHolidayOrWeekend(date))
                    .count();
        }
        final int estimateNumberOfDaysInMonth = 30;
        return averageSalary / estimateNumberOfDaysInMonth * effectiveVacationDays;
    }

}
