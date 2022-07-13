package com.airport.controller;

import com.airport.model.MailDetail;
import com.airport.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    protected final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("syncController.run");
        MailDetail mailDetail = new MailDetail();
        // 发件人名称
        mailDetail.setSenderName("云深小麦");
        // 发件人邮箱
        mailDetail.setSenderMail("owl_email@163.com");
        // 邮件名称
        mailDetail.setMailTitle("这里是测试");
        // 收件人邮箱
        mailDetail.setAddresseeMail("guoqingyan_email@163.com");
        // 发送纯文本 setHtml(false) 默认false
//        mailDetail.setContent("你好！我是云深小麦");

        // 发送HTML setHtml(true)
        mailDetail.setContent("<html><head></head><body><h1>你好！我是云深小麦</h1></body></html>");
        // 内容是否为HTML
        mailDetail.setHtml(true);

        boolean b = mailService.sendMessage(mailDetail);
        if (b) {
            log.info("发送成功");
        }
    }
}
