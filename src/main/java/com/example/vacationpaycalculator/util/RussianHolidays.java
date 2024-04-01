package com.example.vacationpaycalculator.util;

public enum RussianHolidays {
    NEW_YEAR("01-01"),
    CHRISTMAS("01-07"),
    DEFENDER_OF_THE_FATHERLAND_DAY("02-23"),
    INTERNATIONAL_WOMENS_DAY("03-08"),
    LABOUR_DAY("05-01"),
    VICTORY_DAY("05-09"),
    RUSSIA_DAY("06-12"),
    UNITY_DAY("11-04");

    private final String date;

    RussianHolidays(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }
}
