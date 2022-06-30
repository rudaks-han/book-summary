package com.example.designpattern.simplefactory;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsPost extends Post {

    private String headLine;

    private LocalDateTime newsTime;
}
