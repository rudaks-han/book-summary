package com.example.design.builder.type1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserWebDTO implements UserDTO {

    private String name;

    private String address;

    private String age;
}
