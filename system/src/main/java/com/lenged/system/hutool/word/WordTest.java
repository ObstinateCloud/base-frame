package com.lenged.system.hutool.word;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.word.Word07Writer;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: WordTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/24 15:43
 */
public class WordTest {

    @Test
    public void wordExport(){
         //获取系统支持的字体
//        String[] availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//        for (String availableFontFamilyName : availableFontFamilyNames) {
//            System.out.println(availableFontFamilyName);
//        }

        Word07Writer writer = new Word07Writer();

// 添加段落（标题）
        writer.addText(new Font("华文行楷", Font.PLAIN, 22), "我是第一部分", "我是第二部分");
// 添加段落（正文）
        writer.addText(new Font("宋体", Font.PLAIN, 22), "我是正文第一部分", "我是正文第二部分");
// 写出到文件
        writer.flush(FileUtil.file("./wordWrite.docx"));

// 关闭
        writer.close();
    }

    @Test
    public void wordImport() throws Exception{
        XWPFDocument xwpfDocument = new XWPFDocument(new FileInputStream("template/word/word_template.docx"));;
        //获取文本数据
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            System.out.println("----");
            for (XWPFRun run : paragraph.getRuns()) {
                System.out.println(run.text());
            }
        }
//        获取表格数据
        List<XWPFTable> tables = xwpfDocument.getTables();
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell tableCell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : tableCell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            System.out.println(run.text());
                        }
                    }
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream("./wordWrite.docx");

        // 获取第一段文件的第一个run
        XWPFRun xwpfRun = xwpfDocument.getParagraphs().get(2).getRuns().get(0);
        System.out.println(xwpfRun.text());
        //此处不设置 pos ,会追加到原始数据上
        xwpfRun.setText("修改的数据",0);


        xwpfDocument.write(fileOutputStream);
        fileOutputStream.close();
    }

    @Test
    public void wordGenerate() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("template/word/word_template.docx");

        FileOutputStream fileOutputStream = new FileOutputStream("./wordWrite1.docx");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("name","日常工作");
        paramMap.put("dataTime", DateUtil.now());
        paramMap.put("count", 256);
        paramMap.put("content","Apache POI 团队很高兴地宣布发布 5.2.2。一些依赖项已更新到最新版本，以获取安全修复和其他改进。\n" +
                "\n" +
                "发行说明中提供了更改摘要 。更改日志中提供了完整的更改列表。感兴趣的人还应该关注开发列表以跟踪进度。\n" +
                "\n" +
                "有关详细信息，请参阅下载页面。\n" +
                "\n" +
                "POI 自 4.0.1 版起需要 Java 8 或更高版本。");

        paramMap.put("phone", "13666666666");
        paramMap.put("addr", "地球村");
        paramMap.put("email", "Gmail.com");
        paramMap.put("meeting", "（一）：第一个议题\r" +
                "首席：王老五，张仨疯\r" +
                "列席：刘德华，张春燕，王大宝，三丰的\r" +
                "请假：郭富城(帅的不想来)\r" +
                "（二）：第二个议题\r" +
                "首席：乔帮主，慕容复\r" +
                "列席：刘德华，张春燕，王大宝，三丰的\r" +
                "请假：郭富城(帅的不想来)\r" +
                "（三）：第三个议题\r" +
                "首席：王辉文，张发兵 \r" +
                "列席：张三，李四，松江，智多星 \r" +
                "请假：刘备(帅的不想来)\r");

        List<String[]> tableData = new ArrayList<>();
        tableData.add(new String[]{"code1", "类型1", "标题1"});
        tableData.add(new String[]{"code2", "类型2", "标题2"});


        KdWordUtil.generateWord(fileInputStream,fileOutputStream,paramMap,tableData);
    }


}
