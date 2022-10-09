package com.liyh.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class JsoupTest2 {
    public void writeTest() throws Exception {
        //输入你要爬取的URL
        Document document = Jsoup.connect("https://news.163.com/19/0921/15/EPJVQ1S4000189FH.html").get();
        //获取HTML文本的内容
        String title = document.title();
        //通过HTML文本的内容的id来获取内容（网易新闻一般为:endText）
        String content = document.getElementById("endText").text();
        //创建存放文本的地址和文件名
        File file = new File("D://jsoup//0921//" + title.replaceAll("[: ]", "_") + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        //存入内容
        bw.write(content);
        bw.close();
        System.out.println("爬虫完毕");
    }

    //读取 你上面形成的txt文档
    public void readerTest() throws Exception {
        //这是你上面定义的txt文件路径
        File file = new File("D://jsoup//0921");
        //可能是多个文件用file数组接收
        File[] files = file.listFiles();
        //遍历
        for (File file2 : files) {
            BufferedReader br = new BufferedReader(new FileReader(file2));
            //文件的文件名
            String title = file2.getName().split("//.")[0];
            //文件的内容
            String content = br.readLine();
            System.out.println(title);
            System.out.println(content);
        }
    }
}
