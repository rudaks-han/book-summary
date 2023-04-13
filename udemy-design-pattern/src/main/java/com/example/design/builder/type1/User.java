package com.example.design.builder.type1;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

// DTO를 구성하기 위해 사용되는 엔티티 클래스
@Getter
@Setter
public class User {

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private Address address;
}
