package com.lenged.system.easypoi.csv;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @title: CsvTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/25 15:55
 */
public class CsvTest {

    @Test
    public void csvImport() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/template/testRead.csv");
        CsvImportParams csvImportParams = new CsvImportParams();
        csvImportParams.setEncoding(CsvImportParams.UTF8);
        csvImportParams.setTitleRows(0);
        List<UserBean> objects = CsvImportUtil.importCsv(fileInputStream, UserBean.class, csvImportParams);
        objects.forEach(p->{
            System.out.println(p.toString());
        });
    }


    @Test
    public void csvExport(){

    }
}
