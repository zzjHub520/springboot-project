package com.example.nowebproject2.asdf;

import org.springframework.stereotype.Service;

@Service
public class IPServiceImpl implements  IPService{
    @Override
    public String sayHi(String name) {
        System.out.println("hi  " +name);
        //访问dao层
        return name;
    }
}
