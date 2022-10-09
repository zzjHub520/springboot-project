package com.liyh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: liyh
 * @Date: 2020/10/23 17:33
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index()
    {
        return "index";
    }
}
