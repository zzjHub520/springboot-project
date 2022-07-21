package com.example.sbtest1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Contorller {
    @GetMapping("/hello")
    public String aa(){
        return "fasdfasdf";
    }
    @PostMapping("/post")
    public String bb(@RequestBody Map<String,Object> params){
        return params.get("pageSize")+"  fasdfasdf\n";
    }
}
