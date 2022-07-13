package com.airport.model;

public class MailDetail {
    /**
     * 发件人名称
     */
    private String senderName;

    /**
     * 发件人邮件地址
     */
    private String senderMail;

    /**
     * 收件人地址
     */
    private String addresseeMail;

    /**
     * 邮件标题
     */
    private String mailTitle;

    /**
     * 抄送人
     */
    private String[] cc;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * true内容为HTML，false内容为文本 默认文本
     */
    private Boolean html = false;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getAddresseeMail() {
        return addresseeMail;
    }

    public void setAddresseeMail(String addresseeMail) {
        this.addresseeMail = addresseeMail;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }
}
