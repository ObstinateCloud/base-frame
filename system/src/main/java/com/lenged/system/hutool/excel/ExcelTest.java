package com.lenged.system.hutool.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import com.lenged.system.hutool.csv.UserBean;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @title: ExcelTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/17 11:41
 */
public class ExcelTest {

    @Test
    public void getExcelReader() {
        ExcelReader reader;
        //1.
//        reader = ExcelUtil.getReader(FileUtil.file("../../src/main/java/com/hutool/excel/test.xlsx"));
        //2. resources 目录下的相对路径
        reader = ExcelUtil.getReader(ResourceUtil.getStream("template/excel/test.xlsx"));
        //通过sheet编号获取
//        reader = ExcelUtil.getReader(FileUtil.file("../../src/main/java/com/hutool/excel/test.xlsx"), 0);
        //通过sheet名获取
//      reader = ExcelUtil.getReader(FileUtil.file("../../src/main/java/com/hutool/excel/test.xlsx"), "Sheet1");
        List<List<Object>> read = reader.read();
        System.out.println(read.size());
    }

    @Test
    public void getExcelData() {
        ExcelReader reader = ExcelUtil.getReader("template/excel/test.xlsx");
        List<List<Object>> readAllList = reader.read();
        readAllList.forEach(p -> {
            System.out.println(Arrays.toString(p.toArray()));
        });
        System.out.println("---------------------");
        List<Map<String, Object>> readAllMap = reader.readAll();
        readAllMap.forEach(p -> {
            System.out.println(p.toString());
        });
        System.out.println("---------------------");
        List<Employee> readAllBean = reader.readAll(Employee.class);
        readAllBean.forEach(p -> {
            System.out.println(p.toString());
        });
    }

    @Test
    public void getBigData() {

//        ExcelUtil.readBySax("../../src/main/java/com/kd/hutool/excel/test.xlsx", 0,
//                new MyRowHandler(0,0,5));
        MyRowHandler myRowHandler = new MyRowHandler(0, 0, 5);

        MyRowHandler2 myRowHandler2 = new MyRowHandler2(0, 6, 0);
        //自动识别 07 还是03
        ExcelUtil.readBySax("template/excel/test.xlsx", 0,
                myRowHandler2);
        List<String> headerList = myRowHandler2.getHeaderList();
        System.out.println(headerList);

        //只支持Excel2007格式的Sax读取
        Excel07SaxReader reader2007 = new Excel07SaxReader(myRowHandler2);
        reader2007.read("template/excel/test.xlsx", 0);
        //只支持Excel2003格式的Sax读取
        Excel03SaxReader reader2003 = new Excel03SaxReader(myRowHandler2);
        reader2003.read("template/excel/test.xlsx", 0);
    }

    private List<List<String>> prepareDataList() {
        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
        return rows;
    }

    private ArrayList<Map<String, Object>> prepareDataMap() {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        return rows;
    }

    private List<UserBean> prepareDataBean() {
        UserBean row1 = UserBean.builder().age(10).name("zhangsan").focus("java").sex("男").build();
        UserBean row2 = UserBean.builder().age(20).name("李四").focus("ps").sex("女").build();
        UserBean row3 = UserBean.builder().age(30).name("贾平凹").focus("散文").sex("男").build();

        List<UserBean> rows = CollUtil.newArrayList(row1, row2, row3);
        return rows;
    }

    @Test
    public void getExcelWriter() {
        //通过工具类创建writer  默认为target/class
        ExcelWriter writer = ExcelUtil.getWriter("writeTest.xlsx");
        //通过构造方法创建writer
        //ExcelWriter writer = new ExcelWriter("d:/writeTest.xls");

        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

//        List<List<String>> rows = prepareDataList();
//        ArrayList<Map<String, Object>> rows = prepareDataMap();
        //合并单元格后的标题行，使用默认标题样式
//        writer.merge(rows.get(0).size() - 1, "测试标题");
        List<UserBean> rows = prepareDataBean();
//        //仅限写出bean使用
//      writer.addHeaderAlias("name", "姓名");
        //此处下标从0开始
        writer.merge(3, "测试标题");

        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

}
