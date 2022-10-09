package com.liyh.heart;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 模拟文件上传 客户端
 */
public class Socket_File_UpLoad_client {
    public static void main(String[] args) throws IOException {
        //创建本地输入流对象  准备读取需要上传的数据
        FileInputStream fis = new FileInputStream("d:\\1.jpg");
        Socket socket = new Socket("127.0.0.1", 10087);      // 创建和服务器交互的socket对象，

        OutputStream outputStream = socket.getOutputStream();         //创建和服务器交互的输出流对象。准备向服务器发送数据


        /**
         * 注意：
         * 因为 我们读文件是以-1结束的
         * 所以当文件结束后，不会进入循环
         * 那么-1这个结束标记不会上传到服务器
         * 所以服务器没有结束标记会一直阻塞（read 的方法，如果没有消息，会一直阻塞的）
         *
         * 客户端会等待服务器回传的“上传成功”
         * 所以也会阻塞
         * 这个时候我们需要结束客户端发送的输出流（模拟结束标记）
         * */
        int len;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        socket.shutdownOutput();  //关闭输出流，那么客户端的输入流结束

        InputStream inputStream = socket.getInputStream();           //获取服务器回传的数据

        if (socket.isConnected()) {
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len));
            }
        }


        fis.close();   //关闭流
        socket.close();

    }

}
