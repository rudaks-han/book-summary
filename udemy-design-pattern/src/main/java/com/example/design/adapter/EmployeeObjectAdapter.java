package com.example.design.adapter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeObjectAdapter implements Customer {

    private Employee adaptee;

    @Override
    public String getName() {
        return adaptee.getFullName();
    }

    @Override
    public String getDesignation() {
        return adaptee.getJobTitle();
    }

    @Override
    public String getAddress() {
        return adaptee.getOfficeLocation();
    }
}
