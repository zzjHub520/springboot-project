package com.airport.service.impl;

import com.airport.model.MailDetail;
import com.airport.service.MailService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendMessage(MailDetail detail) {
        log.info("mailServiceImpl.sendMessage.request:{}", new Gson().toJson(detail));
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            // 发件人地址
            InternetAddress fromAddress = new InternetAddress(MimeUtility.encodeText(detail.getSenderName()) + "<" + detail.getSenderMail() + ">");
            messageHelper.setFrom(fromAddress);
            // 收件人地址
            InternetAddress toAddress = new InternetAddress(MimeUtility.encodeText(detail.getAddresseeMail()) + "<" + detail.getAddresseeMail() + ">");
            messageHelper.setTo(toAddress);
            // 邮件名称
            messageHelper.setSubject(detail.getMailTitle());
            // 第二个参数指定发送的是HTML格式
            messageHelper.setText(detail.getContent(), detail.getHtml());
            // 抄送人
            if (detail.getCc() != null && detail.getCc().length > 0) {
                messageHelper.setCc(detail.getCc());
            }
            // 测试图片附件（ClassPathResource要把图片放到resources，并且编译代码把图片加载到target里）
//            messageHelper.addInline("myLogo", new ClassPathResource("WechatIMG2602.jpeg"));
            javaMailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("mailServiceImpl.sendMessage.error:{},{}", e.getMessage(), e.getStackTrace());
        }
        return false;
    }
}

