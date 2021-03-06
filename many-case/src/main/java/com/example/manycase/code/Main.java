package com.example.manycase.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {
    @Autowired
    private Dog dog1;
    @Autowired
    private Dog dog2;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("dog1: "+dog1);
        dog1.setAge(5);
        System.out.println("dog2: "+dog2);
    }
}
