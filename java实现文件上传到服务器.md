本文实例为大家分享了java实现文件上传到服务器的具体代码，供大家参考，具体内容如下

1、运行jar包，发送post请求

```java
public static void main(String[] args) {

        //String filePath="C:/Users/706293/IT_onDuty.xls";
        String filePath=args[0];
        String unid=args[1];
       // String unid="155555";
        DataOutputStream out = null;
        final String newLine = "\r\n";
        final String prefix = "--";
        try {
            URL url = new URL("http://172.20.200.64:9000/excel9000/uploads");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            String BOUNDARY = "-------7da2e536604c8";
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            out = new DataOutputStream(conn.getOutputStream());

            // 添加参数file
            File file = new File(filePath);
            StringBuilder sb1 = new StringBuilder();
            sb1.append(prefix);
            sb1.append(BOUNDARY);
            sb1.append(newLine);
            sb1.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"" + newLine);
            sb1.append("Content-Type:application/octet-stream");
            sb1.append(newLine);
            sb1.append(newLine);
            out.write(sb1.toString().getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            out.write(newLine.getBytes());
            in.close();

            // 添加参数sysName
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            sb.append("Content-Disposition: form-data;name=\"unid\"");
            sb.append(newLine);
            sb.append(newLine);
            sb.append(unid);
            out.write(sb.toString().getBytes());

             添加参数returnImage
            //StringBuilder sb2 = new StringBuilder();
            //sb2.append(newLine);
            //sb2.append(prefix);
            //sb2.append(BOUNDARY);
            //sb2.append(newLine);
            //sb2.append("Content-Disposition: form-data;name=\"returnImage\"");
            //sb2.append(newLine);
            //sb2.append(newLine);
            //sb2.append("false");
            //out.write(sb2.toString().getBytes());

            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }
```

2、服务器接收端，将文件上床服务器指定位置

```java
package com.dayang.ExcelController;


import com.dayang.ExcelService.FileService;
import com.dayang.dubbo.CreateExcelConsumer;
import com.dayang.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    protected static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private CreateExcelConsumer createExcelConsumer;
    @Autowired
    FileService fileService;

    @PostMapping("/uploads")
    public String uploads(@RequestParam("file") MultipartFile file,@RequestParam("unid")String unid) throws IOException {
        //String unid="444444";
        String uploadPath = "";
        try{
            logger.info("==>uuid: " + unid);
            if (file == null) {
                logger.error("==>  没有上传文件。");
                return Result.error("没有上传文件。");
            }
            logger.info("==>文件名: " + file.getOriginalFilename());
             uploadPath = fileService.handlerMultipartFile(file,unid);
            //return Result.success("文件上传完成。", newFileName);
            //uploadPath = createExcelConsumer.uploadExcel(file,unid);
            logger.info("==>文件路径: " + uploadPath);
        }
        catch (Exception e){

        }

        return uploadPath;

    }
    @RequestMapping("/test")
    public  String  test(){
        System.out.println("test测试成功。");
        logger.info("==>  测试成功。");
        return  "test";
    }

}
```

3、service

```java
package com.dayang.ExcelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class FileService {

    protected static final Logger logger= LoggerFactory.getLogger(FileService.class);

    private String directoryPath = "C:\\Temp";

    public FileService() {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String handlerMultipartFile(MultipartFile multipartFile ,String unid) {
        String fileOldName = multipartFile.getOriginalFilename();
        int beginIndex = fileOldName.lastIndexOf(".");
       String suffix = fileOldName.substring(beginIndex);
        String newFileName =  unid+ suffix;
        File upFile = new File(directoryPath + "/" + newFileName);
        OutputStream outputStream = null;
        try {
            byte[] fileByte = multipartFile.getBytes();
            outputStream = new FileOutputStream(upFile);
            outputStream.write(fileByte);
            logger.info("<==  文件写出完成: " + newFileName);
            return newFileName;
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return directoryPath + "/" + newFileName;
    }

}
```

4、Result

```java
package com.dayang.util;

import com.alibaba.fastjson.JSONObject;

public class Result {

    public static String success(String msg, Object result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", msg);
        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }

    public static String error(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", -1);
        jsonObject.put("message", msg);
        return jsonObject.toJSONString();
    }

}
```

以上就是本文的全部内容，希望对大家的学习有所帮助，也希望大家多多支持脚本之家。