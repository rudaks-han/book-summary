package com.example.design.simplefactory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Post {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;
}
