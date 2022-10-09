package com.liyh.wx.controller;

import com.liyh.wx.service.WxRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/wx")
public class WxRequestController {

    @Autowired
    private WxRequestService wxRequestService;

    @RequestMapping("/request")
    public void request(HttpServletRequest request, HttpServletResponse response) {
        /**
         * signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
         * timestamp  时间戳
         * nonce	  随机数
         * echostr	  随机字符串
         */
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        // 校验请求
        if (wxRequestService.checkRequest(timestamp, nonce, signature)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }
}
