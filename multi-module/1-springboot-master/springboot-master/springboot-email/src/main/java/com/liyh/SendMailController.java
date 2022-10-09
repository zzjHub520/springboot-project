package com.liyh;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发送邮件接口
 */
@RestController
@RequestMapping("/mail")
public class SendMailController {

    /**
     * 发送文本邮件
     *
     * @param messageInfo 邮件信息
     */
    @PostMapping("sendSimpleMail")
    public String sendSimpleMail(MessageInfo messageInfo) {
        MailUtil mailUtil = SpringUtils.getBean(MailUtil.class);
        return mailUtil.sendSimpleMail(messageInfo.getReceiver(), messageInfo.getSubject(), messageInfo.getContent());
    }

    /**
     * 发送带附件的邮件
     *
     * @param messageInfo 邮件信息
     * @param multipartFile 上传的文件
     */
    @PostMapping("sendAttachmentsMail")
    public String sendAttachmentsMail(MessageInfo messageInfo, MultipartFile multipartFile) {
        MailUtil mailUtil = SpringUtils.getBean(MailUtil.class);
        // 发送带附件的邮件( 路径中的 \ 通常需要使用 \\, 如果是 / 就不需要使用转义了, \ 常用于本地，而/ 常用于网络连接地址 )
        // D:\nacos1.2.1\bin\logs\access_log.2022-03-22.log
        // C:/Users/Administrator/Pictures/1.jpg
        return mailUtil.sendAttachmentsMail(messageInfo.getReceiver(), messageInfo.getSubject(), messageInfo.getContent(),
                messageInfo.getFilePath1(), messageInfo.getImgPath(), multipartFile);
    }

    /**
     * 发送模板邮件
     *
     * @param messageInfo 邮件信息
     * @return
     */
    @PostMapping("sendTemplateMail")
    public String sendTemplateMail(MessageInfo messageInfo) {
        MailUtil mailUtil = SpringUtils.getBean(MailUtil.class);
        // 发送Html模板邮件
        return mailUtil.sendTemplateMail(messageInfo.getReceiver(), messageInfo.getSubject(), messageInfo.getContent(),
                messageInfo.getFromName(), messageInfo.getReceiverName());
    }

    /**
     * 发送复杂邮件
     *
     * @param messageInfo 邮件信息
     */
    @PostMapping("sendComplexMail")
    public String sendComplexMail(MessageInfo messageInfo) {
        MailUtils mailUtils = SpringUtils.getBean(MailUtils.class);
        return mailUtils.sendComplexMail(messageInfo.getReceivers(), messageInfo.getSubject(), messageInfo.getContent(),
                messageInfo.getImgPath(), messageInfo.getFilePath1(), messageInfo.getFilePath2());
    }
}
