package com.online.yugaofan.utils;

import cn.hutool.core.util.StrUtil;
import com.online.yugaofan.utils.fanyi.FanyiUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * excel 工具
 */
public class ExcelUtils {

    /**
     * 读取文本并翻译
     */
    public static String test1() throws Exception{
        File file = new File("C:\\Users\\86158\\Desktop/test.xlsx");
        if (!file.exists()){
            throw new Exception("文件不存在!");
        }
        InputStream in = new FileInputStream(file);

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        //默认第一行为标题行，i = 0
//        XSSFRow titleRow = sheetAt.getRow(0);
//        List<Map<String,Object>> mapList = new ArrayList<>();
        // 循环获取每一行数据
        for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            // 读取每一列内容
            Map<String,Object> map = new HashMap<>();
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
//                XSSFCell titleCell = titleRow.getCell(index);
                XSSFCell cell = row.getCell(index);
                cell.setCellType(CellType.STRING);
                if (cell.getStringCellValue().equals("")) {
                    continue;
                }
                //表头数据
//                String titleName = titleCell.getStringCellValue();
                //单元格内容
                String valueName = cell.getStringCellValue();
                String youdao = FanyiUtils.baidu(valueName, "", "");
                cell.setCellValue(StrUtil.isNotBlank(youdao) ? youdao : valueName);
                //每一行的数据
//                map.put(titleName,valueName);
            }
//            mapList.add(map);
        }
//        System.out.println(JSON.toJSONString(mapList));

        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\86158\\Desktop/test_翻译.xlsx"));
        sheets.write(out);
        out.close();
        in.close();
        return "";
    }

}
