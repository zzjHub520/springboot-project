package com.example.manycase.code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@Scope("prototype")
@ConfigurationProperties(prefix = "dog")
public class Dog {
    private int age;
    private String sex;
}
