package com.liyh.controller;

import com.liyh.server.WebSocketSever;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/websocket")
public class TestController {

    /**
     * 推送消息
     *
     * @param id
     * @param message
     * @throws IOException
     */
    @GetMapping("/getMessage")
    public void getMessage(int id, String message) {
        WebSocketSever.sendMessageByUser(id, message);
    }
}
