package com.example.springbootpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

@SpringBootApplication
public class SpringbootPathApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootPathApplication.class, args);
        ApplicationHome home = new ApplicationHome(SpringbootPathApplication.class);
        String parent = home.getDir().getParent();
        File dir = home.getDir();
        File parentFile = dir.getParentFile();
        File abc = new File("abc");


        System.out.println("jdfghjdfgjabcd" + parent);

//////////////////////////////////////////////////////////////////////
        File file = new File(SpringbootPathApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println("abc " + file);
        System.out.println("abc " + file.getParent());
        System.out.println("abc " + file.getParentFile().getParent());
        System.out.println("abc " + file);
    }

}
