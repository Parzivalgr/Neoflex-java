package com.example.vacationpaycalculator.controller;

import com.example.vacationpaycalculator.util.VacationPayUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class VacationPayCalculatorController {

    @GetMapping("/calculate")
    public ResponseEntity<String> calculateVacationPay(@RequestParam Double averageSalary,
                                                       @RequestParam(required = false) Integer vacationDays,
                                                       @RequestParam(required = false) List<String> exactVacationDays) {
        // Validate the inputs
        if (averageSalary == null || averageSalary <= 0) {
            return ResponseEntity.badRequest().body("Error: Average salary must be positive and not null.");
        }

        if (vacationDays != null && vacationDays < 0) {
            return ResponseEntity.badRequest().body("Error: Vacation days must be positive.");
        }

        if ((vacationDays == null || vacationDays == 0) && (exactVacationDays == null || exactVacationDays.isEmpty())) {
            return ResponseEntity.badRequest().body("Error: At least one of vacationDays or exactVacationDays must be specified.");
        }

        // Validate exactVacationDays contains valid dates
        if (exactVacationDays != null) {
            for (String dateStr : exactVacationDays) {
                try {
                    LocalDate.parse(dateStr); // Default ISO_LOCAL_DATE
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest().body(String.format("Error: '%s' is not a valid date in YYYY-MM-DD format.", dateStr));
                }
            }
        }

        final double vacationPay = VacationPayUtil.calculateVacationPay(averageSalary, vacationDays, exactVacationDays);
        return ResponseEntity.ok(String.format("Vacation pay: %.2f", vacationPay));
    }
}
