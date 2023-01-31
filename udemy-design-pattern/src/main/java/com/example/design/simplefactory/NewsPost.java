package com.example.design.simplefactory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsPost extends Post {

    private String headLine;

    private LocalDateTime newsTime;
}
