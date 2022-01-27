package com.example.nowebproject;

import com.example.nowebproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NowebProjectApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.sayHi("jiuyue");
    }

    public static void main(String[] args) {
        SpringApplication.run(NowebProjectApplication.class, args);
    }

}
