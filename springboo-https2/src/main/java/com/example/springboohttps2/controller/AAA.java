package com.example.springboohttps2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AAA {
    @GetMapping("/hello")
    public String aa(){
        return "hello world!";
    }
}
