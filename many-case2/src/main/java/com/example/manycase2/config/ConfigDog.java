package com.example.manycase2.config;

import com.example.manycase2.code.Dog;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDog {
    @Bean(value = "dog1")
    @ConfigurationProperties(prefix = "dog1")
    public Dog getDog1(){
        Dog dog = new Dog();

        return dog;
    }

    @Bean(value = "dog2")
    @ConfigurationProperties(prefix = "dog2")
    public Dog getDog2(){
        Dog dog = new Dog();

        return dog;
    }
}
