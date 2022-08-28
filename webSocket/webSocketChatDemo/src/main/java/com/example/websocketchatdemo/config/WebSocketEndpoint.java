package com.example.websocketchatdemo.config;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//ws://localhost:8071/websocket/A
@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketEndpoint {
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        //把会话存入到连接池中
        SessionPool.sessions.put(userId, session);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(Session session){
        try {
            SessionPool.close(session.getId());
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session){
        SessionPool.sendMessage(message);
    }
}
