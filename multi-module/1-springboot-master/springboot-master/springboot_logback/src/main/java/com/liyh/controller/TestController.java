package com.liyh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liyh
 * @Date: 2020/9/1 17:32
 */
@RestController
public class TestController {

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/")
    public String query() {
        logger.debug("这个是debug测试来的数据");
        logger.info("这个是info测试来的数据");
        logger.warn("这个是warn测试来的数据");
        logger.error("这个是error测试来的数据");
        return "Hello Liyh";
    }

}
