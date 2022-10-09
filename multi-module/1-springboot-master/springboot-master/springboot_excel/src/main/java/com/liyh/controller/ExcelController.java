package com.liyh.controller;

import com.liyh.entity.Result;
import com.liyh.service.ExcelService;
import com.liyh.utils.ExcelTool;
import com.liyh.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口
 *
 * @Author: liyh
 * @Date: 2020/10/23 17:05
 */

@RestController
@RequestMapping("/excel")
public class ExcelController {

    Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private ExcelService excelService;

    @PostMapping("/import")
    public Result importProject(MultipartFile file) {
        String postfix = ExcelTool.getPostfix(file.getOriginalFilename());

        if (!"xlsx".equals(postfix) && !"xls".equals(postfix)) {
            return Result.error("导入失败，请选择正确的文件格式支持xlsx或xls");
        }
        return excelService.importProject(file);
    }

    @GetMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "template.xlsx";
        String result = FileUtils.downloadFiles(request, response, fileName);
        if (request == null) {
            return null;
        }
        return result;
    }
}
