package com.example.designpattern.visitor;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

@Getter
public abstract class AbstractEmployee implements Employee {

    private int performanceRating;

    private String name;

    private static int employeeIdCounter = 101;

    private int employeeId;

    public AbstractEmployee(String name) {
        this.name = name;
        employeeId = employeeIdCounter++;
    }

    @Override
    public void setPerformanceRating(int rating) {
        this.performanceRating = rating;
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return Collections.emptyList();
    }
}
