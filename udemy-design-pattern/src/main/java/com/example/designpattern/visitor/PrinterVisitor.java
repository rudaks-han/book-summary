package com.example.designpattern.visitor;

public class PrinterVisitor implements Visitor {
    @Override
    public void visit(Programmer programmer) {
        String msg = programmer.getName() + " is a " + programmer.getSkill() + " programmer.";
        System.out.println(msg);
    }

    @Override
    public void visit(ProjectLead lead) {
        String msg = lead.getName() + " is a Project Lead with " + lead.getDirectReports().size() + " programmers reporting.";
        System.out.println(msg);
    }

    @Override
    public void visit(Manager manager) {
        String msg = manager.getName() + " is a Manager with " + manager.getDirectReport().size() + " leads reporting.";
        System.out.println(msg);
    }

    @Override
    public void visit(VicePresident vicePresident) {
        String msg = vicePresident.getName() + " is a Vice President with " + vicePresident.getDirectReports().size() + " managers reporting.";
        System.out.println(msg);
    }
}
