package com.example.designpattern.simplefactory;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Post {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;
}
