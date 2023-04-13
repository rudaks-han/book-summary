package com.example.solid.isp.after;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Entity {

    private String name;

    private LocalDateTime lastLogin;
}