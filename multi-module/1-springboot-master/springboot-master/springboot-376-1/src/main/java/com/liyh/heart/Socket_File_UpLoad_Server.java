package com.liyh.heart;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * 模拟文件上传 服务端
 */
public class Socket_File_UpLoad_Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(10087);

        while (true) {
            Socket socket = serverSocket.accept();  //通过服务器的对象 获取客户端的socket

            InputStream is = socket.getInputStream(); //获取客户端的输入流 ，准备接受数据


            String filename = System.currentTimeMillis() + new Random().nextInt(99999) + ".jpg";
            File file = new File("E:\\upload");//创建需要上传文件的文件夹
            if (!file.exists()) {  //如果文件夹不存在
                file.mkdirs();  //创建文件夹
            }

            //创建需要保存文件的输出流，
            FileOutputStream fos = new FileOutputStream(file + "\\" + filename);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {  //死循环一直读取消息
                fos.write(bytes, 0, len);
            }

            OutputStream outputStream = socket.getOutputStream(); //获取socket的输入流
            outputStream.write("上传成功".getBytes()); //向客户端发送 上传成功

            /**
             * 三个流都需要关闭，
             * */
            fos.close();
            socket.close();
        }


        // serverSocket.close(); 如果需要服务器一直启动，就不需要关闭serverSocket


    }

}

