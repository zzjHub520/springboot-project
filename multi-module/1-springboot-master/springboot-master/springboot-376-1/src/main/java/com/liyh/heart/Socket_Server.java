package com.liyh.heart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器的流也是 使用 client 传递过来的socket获取的
 * 说白了 socket就是一个管道
 */
public class Socket_Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        //获取和client 交互的 socket
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        OutputStream outputStream = accept.getOutputStream();

        byte[] bytes = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes));
            outputStream.write("服务器收到了，谢谢".getBytes());
        }
    }
}
