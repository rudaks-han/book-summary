package com.example.design.command;

public class EWSService {

    public void addMember(String contact, String contactGroup) {
        System.out.println("Added " + contact + " to " + contactGroup);
    }

    public void removeMember(String contact, String contactGroup) {
        System.out.println("Removed " + contact + " from " + contactGroup);
    }
}
