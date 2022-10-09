package com.liyh.test;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JsoupTest3 {
    public static void main(String[] args) throws IOException {
        List<String> sourceFilePaths = new ArrayList<String>();
        sourceFilePaths.add("E:\\file\\upload\\20220115\\4d91238c332d20ab8f24906fc713ad96.png");
        // 试一个找不到的文件(文件找不到不会压缩)
        sourceFilePaths.add("d:/test/找不到我.jpg");
        // 指定打包到哪个zip（绝对路径）
        String zipTempFilePath = "E:/test/test.zip";
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
