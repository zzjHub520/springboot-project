package com.liyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.liyh.mapper")
public class SpringbootExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootExcelApplication.class, args);
    }

}
