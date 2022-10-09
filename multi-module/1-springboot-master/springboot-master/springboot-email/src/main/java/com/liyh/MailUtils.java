package com.liyh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * 统一邮件发送工具类
 *
 * @author Liyh
 * @date 2022/03/28
 */

@Component
public class MailUtils {

    /**
     * 从配置文件中注入是否需要身份验证
     */
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    /**
     * 从配置文件中注入邮箱类型
     */
    @Value("${spring.mail.host}")
    private String emailHost;

    /**
     * 从配置文件中注入发件人
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 从配置文件中注入授权码
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * 发送复杂邮件
     *
     * @param receivers  收件人
     * @param subject   邮件标题
     * @param content   邮件内容
     * @param imgPath   图片路径
     * @param filePath1 文件路径
     * @param filePath2 文件路径
     */
    public String sendComplexMail(String receivers, String subject, String content, String imgPath, String filePath1, String filePath2) {
        try {
            Properties props = new Properties();
            // 表示SMTP发送邮件，需要进行身份验证
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.host", emailHost);
            // 发件人的账号
            props.put("mail.user", fromEmail);
            // 访问SMTP服务时需要提供的密码
            props.put("mail.password", password);

            // 1.Session对象.连接(与邮箱服务器连接)
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            // 2.构建邮件信息
            Message message = new MimeMessage(session);
            // 发件人邮箱
            message.setFrom(new InternetAddress(fromEmail));

            // Message.RecipientType.TO：消息接受者
            // Message.RecipientType.CC：消息抄送者
            // Message.RecipientType.BCC：匿名抄送接收者(其他接受者看不到这个接受者的姓名和地址)

            // 判断接收人个数
            if (receivers != null && receivers != "") {
                String[] receiverArray = receivers.split(",");
                if (receiverArray.length == 1) {
                    // 一对一发送邮件
                    message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiverArray[0]));
                } else {
                    // 一对多发送邮件
                    // 构建一个群发地址数组
                    String[] receiverArry = receivers.split(",");
                    InternetAddress[] adr = new InternetAddress[receiverArry.length];
                    for (int i = 0; i < receiverArry.length; i++) {
                        adr[i] = new InternetAddress(receiverArry[i]);
                    }
                    // Message的setRecipients方法支持群发。。注意:setRecipients方法是复数和点 到点不一样
                    message.setRecipients(Message.RecipientType.TO, adr);
                }
            }

            // 发送日期
            message.setSentDate(new Date());
            // 设置标题
            message.setSubject(subject);

            // 3.准备邮件内容
            // 3.1.准备图片数据
            MimeBodyPart image = new MimeBodyPart();
            FileDataSource imageSource = new FileDataSource(imgPath);
            DataHandler handler = new DataHandler(imageSource);
            image.setDataHandler(handler);
            image.setFileName(imageSource.getName());
            // 创建图片的一个表示用于显示在邮件中显示
            image.setContentID(imageSource.getName());

            // 3.2准备本文本数据
            MimeBodyPart text = new MimeBodyPart();
            text.setContent("<img src='cid:" + image.getContentID() + "'/>" + "<h2>" + content + "</h2>", "text/html;charset=utf-8");

            // 3.3.准备附件数据
            MimeBodyPart appendix1 = new MimeBodyPart();
            FileDataSource fileSource1 = new FileDataSource(filePath1);
            appendix1.setDataHandler(new DataHandler(fileSource1));
            appendix1.setFileName(fileSource1.getName());

            MimeBodyPart appendix2 = new MimeBodyPart();
            FileDataSource fileSource2= new FileDataSource(filePath2);
            appendix2.setDataHandler(new DataHandler(fileSource2));
            appendix2.setFileName(fileSource2.getName());

            // 3.4.拼装邮件正文
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(image);
            mimeMultipart.addBodyPart(text);
            // 文本和图片内嵌成功
            mimeMultipart.setSubType("related");

            // 3.5.将拼装好的正文内容设置为主体
            MimeBodyPart contentText = new MimeBodyPart();
            contentText.setContent(mimeMultipart);

            // 3.6.拼接附件
            MimeMultipart allFile = new MimeMultipart();
            // 附件
            allFile.addBodyPart(image);
            allFile.addBodyPart(appendix1);
            allFile.addBodyPart(appendix2);
            // 正文
            allFile.addBodyPart(contentText);
            // 正文和附件都存在邮件中，所有类型设置为mixed
            allFile.setSubType("mixed");

            // 3.7.放到Message消息中
            message.setContent(allFile);

            // 4.发送邮件信息
            Transport.send(message);
            return "发送复杂邮件成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送复杂邮件失败！！！";
        }
    }

}
