package com.lenged.system.hutool.word;

import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @title: KdWordUtil
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/25 9:05
 */
public class KdWordUtil {

    /**
     * @param wordValue ${...} 带${}的变量
     * @param map       存储需要替换的数据
     * @return java.lang.String
     * @Description 有${}的值匹配出替换的数据，没有${}就返回原来的数据
     */
    public static String matchesValue(String wordValue, Map<String, Object> map) {
        for (String s : map.keySet()) {
            String s1 = new StringBuilder("${").append(s).append("}").toString();
            //查询原始值中是否包含${key}
            if (wordValue.contains(s1)) {
                wordValue = wordValue.replace(s1, map.get(s).toString());
            }
        }
        return wordValue;
    }


    /**
     * @return boolean
     * @Description 测试是否包含需要替换的数据
     */
    public static boolean isReplacement(String text) {
        boolean check = false;
        if (text.contains("$")) {
            check = true;
        }
        return check;
    }

    /**
     * @param xwpfDocument
     * @param insertTextMap
     * @Description 处理所有文段数据，除了表格
     */
    public static void handleParagraphs(XWPFDocument xwpfDocument, Map<String, Object> insertTextMap) {
        for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
            String text = paragraph.getText();
            if (isReplacement(text)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    run.setText(matchesValue(run.text(), insertTextMap), 0);
                }
            }
        }

    }

    /**
     * @param inputStream
     * @param outputStream
     * @param insertTextMap
     * @param addList
     * @Description 通过word模板生成word的主方法
     */
    public static void generateWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> insertTextMap, List<String[]> addList) throws IOException {
        //获取docx解析对象
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);

        // 处理所有文段数据，除了表格
        handleParagraphs(xwpfDocument, insertTextMap);
        // 处理表格数据
        handleTable(xwpfDocument, insertTextMap, addList);

        // 写出数据
        xwpfDocument.write(outputStream);
        outputStream.close();
    }

    /**
     * @param xwpfDocument
     * @param insertTextMap
     * @param addList
     * @Description 处理表格数据方法
     */
    public static void handleTable(XWPFDocument xwpfDocument, Map<String, Object> insertTextMap, List<String[]> addList) {
        List<XWPFTable> tables = xwpfDocument.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            if (rows.size() > 1) {
                if (isReplacement(table.getText())) {
                    // 替换数据
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> tableCells = row.getTableCells();
                        for (XWPFTableCell tableCell : tableCells) {
                            if (isReplacement(tableCell.getText())) {
                                // 替换数据
                                XWPFParagraph xwpfParagraph = tableCell.addParagraph();
                                xwpfParagraph.createRun().setText(matchesValue(tableCell.getText(), insertTextMap), 0);
                                if(tableCell.getParagraphs().size()>1){
                                    tableCell.removeParagraph(0);
                                }

                            }
                        }
                    }
                } else {
                    // 插入数据
                    for (int i = 1; i < addList.size(); i++) {
                        XWPFTableRow row = table.createRow();
                    }
                    List<XWPFTableRow> rowList = table.getRows();
                    for (int i = 1; i < rowList.size(); i++) {
                        XWPFTableRow xwpfTableRow = rowList.get(i);
                        List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
                        for (int j = 0; j < tableCells.size(); j++) {
                            XWPFTableCell xwpfTableCell = tableCells.get(j);
                            xwpfTableCell.setText(addList.get(i - 1)[j]);
                        }
                    }

                }


            }
        }

    }

}
