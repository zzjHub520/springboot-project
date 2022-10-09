package com.liyh.controller;

import com.liyh.config.FileResourceConfig;
import com.liyh.config.ServerConfig;
import com.liyh.utils.ImageUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用请求处理
 */
@RestController
@RequestMapping("/file")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws Exception {
        // 上传文件路径
        String filePath = FileResourceConfig.getProjectPath();
        // 上传并返回新文件名称
        String fileName = ImageUploadUtils.upload(filePath, file);
        String url = serverConfig.getUrl() + fileName;
        System.out.println(url);
        return url;
    }

}
