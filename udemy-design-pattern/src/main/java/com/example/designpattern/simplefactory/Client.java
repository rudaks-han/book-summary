package com.example.designpattern.simplefactory;

public class Client {

    public static void main(String[] args) {
        Post blogPost = PostFactory.createPost("blog");
        System.out.println(blogPost);
        Post newsPost = PostFactory.createPost("news");
        System.out.println(newsPost);
    }
}
