package com.example.itodolist;

public class Task {
    final String name;
    final String beginDate;
    final String endDate;
    final String measureUnit;
    final int totalUnits;
    final int currentUnits;
    final int progressBar;

    public Task(String name, String beginDate, String endDate, String measureUnit, int totalUnits, int currentUnits, int progressBar) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.measureUnit = measureUnit;
        this.totalUnits = totalUnits;
        this.currentUnits = currentUnits;
        this.progressBar = progressBar;
    }
}