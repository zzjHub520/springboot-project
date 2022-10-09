package com.liyh;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailUtil {

    /**
     * 从配置文件中注入发件人的姓名
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Spring官方提供的集成邮件服务的实现类，目前是Java后端发送邮件和集成邮件服务的主流工具。
     */
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 发送文本邮件
     *
     * @param receiver 收件人
     * @param subject  邮件标题
     * @param content  邮件内容
     */
    public String sendSimpleMail(String receiver, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 发件人
            message.setFrom(fromEmail);
            // 收件人
            message.setTo(receiver);
            // 邮件标题
            message.setSubject(subject);
            // 邮件内容
            message.setText(content);

            mailSender.send(message);
            return "发送文本邮件成功";
        } catch (MailException e) {
            e.printStackTrace();
            return "发送文本邮件失败！！！";
        }
    }

    /**
     * 发送html邮件
     *
     * @param receiver 收件人
     * @param subject  邮件标题
     * @param content  邮件内容
     */
    public String sendHtmlMail(String receiver, String subject, String content) {
        try {
            // 注意这里使用的是MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发件人
            helper.setFrom(fromEmail);
            // 收件人
            helper.setTo(receiver);
            // 邮件标题
            helper.setSubject(subject);
            // 邮件内容，第二个参数：格式是否为html
            helper.setText(content, true);
            mailSender.send(message);
            return "发送html邮件成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送html邮件失败！！！";
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param receiver      收件人
     * @param subject       邮件标题
     * @param content       邮件内容
     * @param filePath      文件路径
     * @param imgPath       图片路径
     * @param multipartFile 上传的文件
     */
    public String sendAttachmentsMail(String receiver, String subject, String content, String filePath, String imgPath, MultipartFile multipartFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 要带附件第二个参数设为true
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发件人
            helper.setFrom(fromEmail);
            // 收件人
            helper.setTo(receiver);
            // 邮件标题
            helper.setSubject(subject);
            // 邮件内容，第二个参数：格式是否为html
            helper.setText(content, true);

            // 添加文件附件1
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);

            // 添加文件附件2
            FileSystemResource files = new FileSystemResource(multipartFileToFile(multipartFile));
            helper.addAttachment(files.getFilename(), files);

            // 添加图片附件
            FileSystemResource image = new FileSystemResource(new File(imgPath));
            helper.addAttachment(image.getFilename(), image);

            mailSender.send(message);
            return "发送带附件的邮件成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送带附件的邮件失败！！！";
        }

    }

    /**
     * 发送模板邮件
     *
     * @param receiver     收件人
     * @param subject      邮件标题
     * @param content      邮件内容
     * @param fromName     发件人姓名
     * @param receiverName 收件人姓名
     * @return
     */
    public String sendTemplateMail(String receiver, String subject, String content, String fromName, String receiverName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获得模板
        Template template = null;
        try {
            template = freeMarkerConfigurer.getConfiguration().getTemplate("message.ftl");
        } catch (IOException e) {
            e.printStackTrace();
            return "获取模板失败！！！";
        }
        // 使用Map作为数据模型，定义属性和值
        Map<String, Object> model = new HashMap<>();
        model.put("fromName", fromName);
        model.put("receiverName", receiverName);
        model.put("content", content);
        model.put("sendTime", simpleDateFormat.format(new Date()));

        // 传入数据模型到模板，替代模板中的占位符，并将模板转化为html字符串
        String templateHtml = null;
        try {
            templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
            return "转换模板失败！！！";
        } catch (TemplateException e) {
            e.printStackTrace();
            return "转换模板失败！！！";
        }

        // 该方法本质上还是发送html邮件，调用之前发送html邮件的方法
        return this.sendHtmlMail(receiver, subject, templateHtml);
    }

    /**
     * 把 multiFile 转换为 file
     *
     * @param multiFile
     * @return
     */
    private File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
