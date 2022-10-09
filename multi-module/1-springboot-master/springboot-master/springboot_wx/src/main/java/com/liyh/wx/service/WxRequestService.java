package com.liyh.wx.service;

public interface WxRequestService {
    /**
     * 验证签名
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    boolean checkRequest(String timestamp, String nonce, String signature);
}
