package com.example.nowebproject2.asdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class StartMain implements CommandLineRunner {
    @Autowired
    private IPService ipService;

    @Override
    public void run(String... args) throws Exception {
        ipService.sayHi("jiuyue");
    }
}
