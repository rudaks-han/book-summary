package com.example.designpattern.visitor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class ProjectLead extends AbstractEmployee {

    private List<Employee> directReports = new ArrayList<>();

    public ProjectLead(String name, Employee...employees) {
        super(name);
        Arrays.stream(employees).forEach(directReports::add);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return directReports;
    }
}
