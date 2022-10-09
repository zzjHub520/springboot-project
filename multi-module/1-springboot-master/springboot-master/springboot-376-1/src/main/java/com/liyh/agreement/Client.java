package com.liyh.agreement;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket;
    public static boolean connectionState = false;

    public static void main(String[] args) {
        while (!connectionState) {
            try {
                connectServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void connectServer() {
        try {
            socket = new Socket("127.0.0.1", 10010);
            System.out.println("客户端连接到服务器...");
            connectionState = true;

            // 读取服务器发送的消息
            ThreadUtil.execute(() -> {
                try {
                    while (true) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message = br.readLine();
                        if (StrUtil.isNotEmpty(message)) {
                            System.out.println("服务器发送的消息：" + message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            ThreadUtil.execute(() -> {
                try {
                    while (true) {
                        Scanner scanner = new Scanner(System.in);
                        String message = scanner.nextLine();
                        if (StrUtil.isNotEmpty(message)) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bw.write(message + "\n");
                            bw.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    connectionState = false;
                    reconnect();
                }
            });
            ThreadUtil.execute(() -> {
                System.out.println("心跳线程启动...");
                try {
                    while (true) {
                        Thread.sleep(60000);
                        String message = "心跳：68 32 00 32 00 68 C9 03 44 04 00 00 02 72 00 00 04 00 8C 16";
                        if (StrUtil.isNotEmpty(message)) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bw.write(message + "\n");
                            bw.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    connectionState = false;
                    reconnect();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            connectionState = false;
        }
    }

    public static void reconnect() {
        while (!connectionState) {
            System.out.println("正在尝试重新连接...");
            connectServer();
        }
    }
}
