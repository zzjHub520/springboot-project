package com.liyh;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author: liyh
 * @Date: 2020/10/26 17:24
 */
public class Test {

    @org.junit.jupiter.api.Test
    public void test() throws UnsupportedEncodingException {
        File file = new File("C:/Maiun/rizhi/template.xlsx");
        String filePath = URLDecoder.decode(file.getPath(), "utf-8");
        File xlsfile = new File(filePath);
        try (FileInputStream is = new FileInputStream(xlsfile)) {
            //同时支持Excel 2003、2007
            Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("getLastRowNum: " + sheet.getLastRowNum());
            System.out.println("getPhysicalNumberOfRows: " + sheet.getPhysicalNumberOfRows());
            int rowCount = sheet.getLastRowNum() + 1; //获取总行数
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                int cellCount = 0;
                if (row != null) {//row  该行所有单元格为空时，row是null值    2017-01-05 pelin
                    cellCount = row.getLastCellNum();//获取最后一个不为空的列是第几个。
                    System.out.println(i + "索引行getLastCellNum: " + cellCount);
                    System.out.println(i + "索引行getPhysicalNumberOfCells: " + row.getPhysicalNumberOfCells());

                    cellCount = cellCount < 0 ? 0 : cellCount;//getLastCellNum没有单元格时会返回负数
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
