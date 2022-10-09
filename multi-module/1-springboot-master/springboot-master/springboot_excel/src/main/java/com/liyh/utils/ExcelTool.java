package com.liyh.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 解析Excel文件单元格内容
 *
 * @Author: liyh
 * @Date: 2020/10/24 17:05
 */
public class ExcelTool {
    public static final String EMPTY = "";
    private static final String POINT = ".";

    /**
     * 获得path的后缀名
     *
     * @param path 文件路径
     * @return 路径的后缀名
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }

    /**
     * 解析xls和xlsx不兼容问题
     *
     * @param pfs
     * @param workbook
     * @param file
     * @return
     */
    public static Workbook getWorkBook(POIFSFileSystem pfs, Workbook workbook, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename.endsWith("xls")) {
            pfs = new POIFSFileSystem(file.getInputStream());
            workbook = new HSSFWorkbook(pfs);
            return workbook;
        } else if (filename.endsWith("xlsx")) {
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
                return workbook;
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
