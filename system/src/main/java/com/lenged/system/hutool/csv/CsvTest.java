package com.lenged.system.hutool.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.*;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @title: CsvTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/15 14:00
 */
public class CsvTest {

    @Test
    public void csvExport() {
        //指定路径和编码
        //路径为 target/classes/testWrite.csv
        CsvWriter writer = CsvUtil.getWriter("./testWrite.csv", CharsetUtil.CHARSET_UTF_8);
//按行写出
        writer.write(
                new String[]{"key_a", "key_b", "key_c"},
                new String[]{"a1", "b1", "c1"},
                new String[]{"a2", "b2", "c2"},
                new String[]{"a3", "b3", "c3"}
        );
    }

    @Test
    public void csvImport() {
        CsvReader reader = CsvUtil.getReader();
//从文件中读取CSV数据
        CsvData data = reader.read(FileUtil.file("template/csv/testRead.csv"),CharsetUtil.CHARSET_UTF_8);
        Console.log(data.toString());
        List<CsvRow> rows = data.getRows();
//遍历行
        for (CsvRow csvRow : rows) {
            //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
            Console.log(csvRow.getRawList());
            //字段个数
            Console.log(csvRow.getFieldCount());
            //当前行数
            Console.log(csvRow.getOriginalLineNumber());

        }
    }

    @Test
    public void csvImportToBean() {
        CsvReader reader = CsvUtil.getReader();
        //从文件中读取CSV数据  直接转换为对象
        List<UserBean> userBeans = reader.read(FileUtil.getReader("template/csv/testRead.csv", CharsetUtil.CHARSET_UTF_8), UserBean.class);
        userBeans.forEach(p->{
            System.out.println(p.toString());
        });


    }
}
