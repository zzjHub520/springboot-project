package com.liyh;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class SpringbootAopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test() throws ParseException {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (i % 2 == 0) {
                    System.out.println(i+"::"+j+"--"+(j+1));
                }else {
                    System.out.println(10%10);
                    System.out.println(30%10);
                    System.out.println(i+"--"+j+"--"+(j+1));
                }
            }
        }
    }

}
