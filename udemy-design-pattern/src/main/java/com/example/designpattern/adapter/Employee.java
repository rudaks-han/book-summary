package com.example.designpattern.adapter;

import lombok.Getter;
import lombok.Setter;

/**
 * 시스템에서 사용된 기존 클래스
 * Adaptee
 */
@Getter
@Setter
public class Employee {

    private String fullName;

    private String jobTitle;

    private String officeLocation;
}
