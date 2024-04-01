package com.example.vacationpaycalculator.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VacationPayCalculatorController.class)
public class VacationPayCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    static Stream<TestParams> testParameters() {
        return Stream.of(
                new TestParams("1000", "10", null, "Vacation pay: 333,33"),
                new TestParams("-500", "10", null, "Error: Average salary must be positive and not null."),
                new TestParams("0", "10", null, "Error: Average salary must be positive and not null."),
                new TestParams("0.01", "10", null, "Vacation pay: 0,00"),
                new TestParams("5000", "0", null, "Error: At least one of vacationDays or exactVacationDays must be specified."),
                new TestParams("5000", "1", null, "Vacation pay: 166,67"),
                new TestParams("5000", "-5", null, "Error: Vacation days must be positive."),
                new TestParams("5000", null, null, "Error: At least one of vacationDays or exactVacationDays must be specified."),
                new TestParams("5000", "10", "2023-09-02,2023-09-03", "Vacation pay: 0,00"),
                new TestParams("5000", "10", "invalid-date", "Error: 'invalid-date' is not a valid date in YYYY-MM-DD format."),
                new TestParams("5000", "10", "2024-02-29", "Vacation pay: 166,67"),
                new TestParams("5000", "10", "2023-02-29", "Error: '2023-02-29' is not a valid date in YYYY-MM-DD format."),
                new TestParams("5000", "10", "2023-04-31", "Error: '2023-04-31' is not a valid date in YYYY-MM-DD format."),
                new TestParams("5000", "10", "", "Vacation pay: 1666,67"),
                new TestParams("5000", "10", "2023-01-01,2023-01-01", "Vacation pay: 0,00"),
                new TestParams("5000", "10", "2023-01-01,not-a-date,2023-01-03", "Error: 'not-a-date' is not a valid date in YYYY-MM-DD format."),
                new TestParams("50000000", "1000", null, "Vacation pay: 1666666666,67"),
                new TestParams("5000", "10", "2023-12-31,2024-01-01", "Vacation pay: 0,00"),
                new TestParams("5000", "10", "future-date", "Error: 'future-date' is not a valid date in YYYY-MM-DD format.")
        );
    }

    @ParameterizedTest
    @MethodSource("testParameters")
    void calculateVacationPayTest(TestParams params) throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("averageSalary", params.averageSalary)
                        .param("vacationDays", params.vacationDays != null ? params.vacationDays : "")
                        .param("exactVacationDays", params.exactVacationDays != null ? params.exactVacationDays : ""))
                .andExpect(content().string(params.expected));
    }

    private static class TestParams {
        String averageSalary;
        String vacationDays;
        String exactVacationDays;
        String expected;

        TestParams(String averageSalary, String vacationDays, String exactVacationDays, String expected) {
            this.averageSalary = averageSalary;
            this.vacationDays = vacationDays;
            this.exactVacationDays = exactVacationDays;
            this.expected = expected;
        }
    }
}
