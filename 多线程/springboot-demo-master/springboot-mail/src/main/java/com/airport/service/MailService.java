package com.airport.service;

import com.airport.model.MailDetail;

public interface MailService {

    /**
     * 发送邮件
     *
     * @param detail
     * @return boolean
     */
    boolean sendMessage(MailDetail detail);
}
