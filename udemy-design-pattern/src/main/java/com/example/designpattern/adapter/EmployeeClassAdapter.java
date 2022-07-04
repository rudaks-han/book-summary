package com.example.designpattern.adapter;

/**
 * 클래스 어댑터, 양방향 어댑터로 동작한다.
 */
public class EmployeeClassAdapter extends Employee implements Customer {
    @Override
    public String getName() {
        return this.getFullName();
    }

    @Override
    public String getDesignation() {
        return this.getJobTitle();
    }

    @Override
    public String getAddress() {
        return this.getOfficeLocation();
    }
}
