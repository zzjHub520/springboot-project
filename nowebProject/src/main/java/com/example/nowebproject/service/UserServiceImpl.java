package com.example.nowebproject.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHi(String name) {
        System.out.println("hi" +name);
        //访问dao层
        return name;
    }
}
