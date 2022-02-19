package com.example.manycase2.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {
    @Autowired
    @Qualifier(value = "dog1")
    private Dog dog1;

    @Autowired
    @Qualifier(value = "dog2")
    private Dog dog2;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("dog1: "+dog1);
        System.out.println("dog1: "+dog2);

    }
}
