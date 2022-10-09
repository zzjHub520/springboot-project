package com.liyh;

import lombok.*;

@Data
public class MessageInfo {

    /**
     * 收件人邮箱
     **/
    private String receiver;

    /**
     * 一对多群发收件人邮箱
     **/
    private String receivers;

    /**
     * 邮件标题
     **/
    private String subject;

    /**
     * 邮件内容
     **/
    private String content;

    /**
     * 图片路径
     **/
    private String imgPath;

    /**
     * 文件路径
     **/
    private String filePath1;

    /**
     * 文件路径
     **/
    private String filePath2;

    /**
     * 发件人姓名
     **/
    private String fromName;

    /**
     * 收件人姓名
     **/
    private String receiverName;

}
