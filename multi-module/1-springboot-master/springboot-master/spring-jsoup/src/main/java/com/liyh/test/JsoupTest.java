package com.liyh.test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JsoupTest {

    // 简单了解Jsoup
    // jsoup提供了一套非常省力的API,可以通过DOM,CSS以及类似于JQuery的操作方法来取出和操作数据
    // 从一个URL,文件或者字符串中解析HTML
    // 使用DOM或CSS选择器来查找,取出数据
    // 可以操作HTML元素,属性和文本
    // 是基于MIT协议发布的,可放心适用于商业项目!

    // Jsoup框架中的常用方法
    // 1. connet (String url): 创建一个新的Connection连接对象
    // 2. get(): 取得要解析的一个HTML文件,如果从该URL获取HTML时发生错误,则会抛出IOException异常.
    // 3.  parse(String html,String vaseUri): 将输入的HTML解析为一个新的文档(Document),参数baseUri用来相对一个URL转成绝对URL.并指定从哪个网站获取文档.只要解析的不是空字符串,就能返回一个结构合理的文档,其中包含(至少)一个head和一个body的元素
    // 4. parseBodyFragment(): 创建一个空壳的文档,并插入解析过得HTML到Body元素中,如果使用正常的Jsoup.parse(String html)方法,通常也可以得到相同的结果.
    // 5. Document.body(): 能够取得文档body元素的所有子元素,具体功能与doc.getElementsByTag("body")相同
    // 6. Node.atter(String key): 获取一个属性的值
    // 7. Element.text(): 获取一个元素中的文本
    // 8. Element.html()或Node.outerHtml(): 获取元素或属性中的HTML内容

    // 2. org.jsoup.nodes.Document类该类表示通过Jsoup库加载HTML文档。可以使用此类执行适用于整个HTML文档的操作。
    // Element类的重要方法可以参见 - http://jsoup.org/apidocs/org/jsoup/nodes/Document.html 。

    // 3. org.jsoup.nodes.Element类HTML元素是由标签名称，属性和子节点组成。 使用Element类，您可以提取数据，遍历节点和操作HTML。
    // Element类的重要方法可参见 - http://jsoup.org/apidocs/org/jsoup/nodes/Element.html 。

    // Document类常用方法
    // 1.Element document.body()
    // 2.Element document.head()
    // 3.String document.title()
    // 4.Elements element.children()
    // 5.Element document.getElementById("id")
    // 6.Elements document.getElementsByAttributeValue("key", "value")
    // 7.Elements document.getElementsByClass("class")
    // 8.Elements document.getElementsByTag("tag");

    // Element类常用方法
    // 1.String element.attr("attributeKey")
    // 2.String element.text()
    // 3.Elements element.children()
    // 4.Element element.getElementById("id")
    // 5.Elements element.getElementsByAttributeValue("key", "value")
    // 6.Elements element.getElementsByClass("class")
    // 7.Elements element.getElementsByTag("tag")

    public static void main(String args[]) throws IOException {
        // 通过以下代码就可以拿到网页文档了。
        // Jsoup 类是 jsoup 的入口类，通过 connect 方法可以从指定链接中加载 HTML 文档（用 Document 对象来表示）。
        String url = "https://www.cnblogs.com/liyhbk/p/16077443.html";
        Document document = Jsoup.connect(url).get();
        String title = document.title();
        System.out.println(title);

//        Element element = Jsoup.parse(new URL(url), 3000);
//        Elements images = element.select("img[src~=(?i)\\.(png)]");

        // 再通过以下代码可以获取文章所有的图片节点：
//        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
        Elements images = document.select("img[src~=(?i)\\.(png)]");
        for (Element image : images) {
            System.out.println("src : " + image.attr("src"));
            System.out.println("height : " + image.attr("height"));
            System.out.println("width : " + image.attr("width"));
            System.out.println("alt : " + image.attr("alt"));
        }

        // 拿到图片的 URL 地址后，事情就好办了，可以直接通过 JDK 的原生 API 下载图片到指定文件夹。
        List<String> sourceFilePaths = new ArrayList<String>();
        // 指定打包到哪个zip（绝对路径）
        String zipTempFilePath = System.getProperty("user.dir") + "/spring-jsoup/src/jsoupImages/jsoupImages.zip";

        String downloadPath = System.getProperty("user.dir") + "/spring-jsoup/src/jsoupImages/";
        File file = new File(downloadPath);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        for (Element image : images) {
            URL urls = new URL(image.attr("src"));
            InputStream inputStream = urls.openStream();
            String filePath = downloadPath + System.currentTimeMillis() + ".png";
            sourceFilePaths.add(filePath);
            OutputStream outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        // 调用压缩
        int s = compress(sourceFilePaths, zipTempFilePath, false);
        System.out.println("成功压缩" + s + "个文件");
    }

    /**
     * @param filePaths        需要压缩的文件地址列表（绝对路径）
     * @param zipFilePath      需要压缩到哪个zip文件（无需创建这样一个zip，只需要指定一个全路径）
     * @param keepDirStructure 压缩后目录是否保持原目录结构
     * @return int  压缩成功的文件个数
     * @throws IOException
     * @Title: compress
     * @Description: TODO
     */
    public static int compress(List<String> filePaths, String zipFilePath, Boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        // zip文件不存在，则创建文件，用于压缩
        if (!zipFile.exists())
            zipFile.createNewFile();
        // 记录压缩了几个文件？
        int fileCount = 0;
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < filePaths.size(); i++) {
                String relativePath = filePaths.get(i);
                if (StringUtils.isEmpty(relativePath)) {
                    continue;
                }
                File sourceFile = new File(relativePath);//绝对路径找到file
                if (sourceFile == null || !sourceFile.exists()) {
                    continue;
                }
                FileInputStream fis = new FileInputStream(sourceFile);
                if (keepDirStructure != null && keepDirStructure) {
                    // 保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                } else {
                    // 直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                // System.out.println("压缩当前文件："+sourceFile.getName());
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
                fileCount++;
            }
            zos.close();
            // System.out.println("压缩完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileCount;
    }


}
