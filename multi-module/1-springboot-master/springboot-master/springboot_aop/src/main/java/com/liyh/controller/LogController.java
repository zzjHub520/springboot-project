package com.liyh.controller;

import com.liyh.entity.User;
import com.liyh.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liyh
 * @Date: 2020/9/12 14:12
 */

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    Logger logger = LoggerFactory.getLogger(LogController.class);


    @RequestMapping("/query/{id}")
    public void query(@PathVariable("id") int id) {
        User user = logService.query(id);
        logger.debug("这个是debug测试来的数据");
        logger.info("这个是info测试来的数据");
        logger.warn("这个是warn测试来的数据");
        logger.error("这个是error测试来的数据");
        System.out.println(user.getName());
    }

    @RequestMapping("/test")
    public void test() {
        int a = 2;
        int b= 0;
        logger.debug("这个是debug测试来的数据");
        logger.info("这个是info测试来的数据");
        logger.warn("这个是warn测试来的数据");
        logger.error("这个是error测试来的数据");
        System.out.println(a/b);
    }

}
