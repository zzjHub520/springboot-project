package com.liyh.wx.service.impl;

import com.liyh.wx.service.WxRequestService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class WxRequestServiceImpl implements WxRequestService {
    private final String TOKEN = "LIYH";

    /**
     * 验证签名
     *
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    @Override
    public boolean checkRequest(String timestamp, String nonce, String signature) {
        // 1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(strs);
        // 2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);
        System.out.println(mysig);
        System.out.println(signature);
        // 3）开发者获得加密后的字符串可与signature对比
        return false;
    }

    // sha1加密
    private static String sha1(String str) {
        try {
            // 获取一个加密对象
            MessageDigest sha1 = MessageDigest.getInstance("sha1");
            // 进行加密
            byte[] digest = sha1.digest(str.getBytes());
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            // 处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b>>4)&15]);
                sb.append(chars[b&15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
