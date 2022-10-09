package com.liyh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 */

@Component
@ConfigurationProperties(prefix = "file")
public class FileResourceConfig {

    /**
     * 上传路径
     */
    private static String profile;

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        FileResourceConfig.profile = profile;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取头像上传路径
     */
    public static String getProjectPath() {
        return getProfile() + "/project";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile();
    }
}
