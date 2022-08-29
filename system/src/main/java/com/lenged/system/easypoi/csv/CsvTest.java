package com.lenged.system.easypoi.csv;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.afterturn.easypoi.handler.inter.IWriter;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @title: CsvTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/25 15:55
 */
public class CsvTest {

    /**
     * 将csv导入为简单实体 非大数据量
     */
    @Test
    public void csvImportToBean() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/template/csv/testRead.csv");
        CsvImportParams csvImportParams = new CsvImportParams();
        csvImportParams.setEncoding(CsvImportParams.UTF8);
        //设置标题行
        csvImportParams.setTitleRows(0);
        List<UserBean> objects = CsvImportUtil.importCsv(fileInputStream, UserBean.class, csvImportParams);
        objects.forEach(p -> {
            System.out.println(p.toString());
        });
    }

    /**
     * 将csv导入为简单Map 非大数据量
     */
    @Test
    public void csvImportToMap() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/template/csv/testRead.csv");
        CsvImportParams csvImportParams = new CsvImportParams();
        csvImportParams.setEncoding(CsvImportParams.UTF8);
        csvImportParams.setTitleRows(0);
        List<Map> objects = CsvImportUtil.importCsv(fileInputStream, Map.class, csvImportParams);
        objects.forEach(p -> {
            p.keySet().forEach(pk -> {
                        System.out.println(pk);
                        System.out.println(p.get(pk));
                    }
            );
        });
    }


    /**
     * 将csv导入为简单bean 大数据量
     */
    @Test
    public void csvBigDataImportToBean() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/template/csv/BigDataExport.csv");
        CsvImportParams csvImportParams = new CsvImportParams();
        csvImportParams.setEncoding(CsvImportParams.UTF8);
        csvImportParams.setTitleRows(0);
        long start = System.currentTimeMillis();
        System.out.println(start);
        List<MsgClient> msgClients = new ArrayList<>();
        CsvImportUtil.importCsv(fileInputStream, MsgClient.class, csvImportParams, new IReadHandler() {
            @Override
            public void handler(Object o) {
                System.out.println(o.toString());
                msgClients.add((MsgClient) o);
                //大数据量处理 每1000条做一次处理
                if (msgClients.size()==1000) {
                    //大数据量处理逻辑
                }
            }

            @Override
            public void doAfterAll() {
                long end = System.currentTimeMillis();
                System.out.println(end);
                System.out.println((end - start) / 1000);
            }
        });
        System.out.println(msgClients.size());
    }

    /**
     * 将csv导入为简单Map 大数据量
     */
    @Test
    public void csvBigDataImportToMap() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/template/csv/BigDataExport.csv");
        CsvImportParams csvImportParams = new CsvImportParams();
        csvImportParams.setEncoding(CsvImportParams.UTF8);
        csvImportParams.setTitleRows(0);
        long start = System.currentTimeMillis();
        System.out.println(start);
        List<Map> msgClients = new ArrayList<>();
        CsvImportUtil.importCsv(fileInputStream, Map.class, csvImportParams, new IReadHandler() {
            @Override
            public void handler(Object o) {
                msgClients.add((Map) o);
            }

            @Override
            public void doAfterAll() {
                long end = System.currentTimeMillis();
                System.out.println(end);
                System.out.println((end - start) / 1000);
            }
        });
        System.out.println(msgClients.size());
    }

    /**
     * 导出复杂bean列表
     */
    @Test
    public void csvExportBean() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        long start = System.currentTimeMillis();
        CsvExportParams exportParams = new CsvExportParams(CsvExportParams.UTF8);
        List<MsgClient> msgClients = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            MsgClient msgClient = new MsgClient();
            msgClient.setBirthday(new Date());
            msgClient.setClientName("client客户端" + i);
            msgClient.setClientPhone("client电话" + i);
            msgClient.setId(i + "");
            msgClient.setCreateBy("创建于：" + new Date().toString());
            msgClient.setRemark("备注这个第" + i + "个对象");
            MsgClientGroup msgClientGroup = new MsgClientGroup();
            msgClientGroup.setCreateBy("创建于：" + new Date().toString());
            msgClientGroup.setGroupName("分组：" + i);
            msgClient.setGroup(msgClientGroup);
            List<UserBean> userBeans = new ArrayList<>();
            for (int j = 0; j < new Random().nextInt(5); j++) {
                UserBean userBean = new UserBean();
                userBean.setAge(j + 10);
                userBean.setFocus("爱好" + j);
                userBean.setName("用户编号" + i + "" + j);
                userBean.setSex(new Random().nextInt(1) == 1 ? "男" : "女");
                userBeans.add(userBean);
            }
            msgClient.setUserBeans(userBeans);
            msgClients.add(msgClient);

        }
        FileOutputStream outputStream = new FileOutputStream("./target/csvExportBean.csv");
        CsvExportUtil.exportCsv(exportParams, MsgClient.class, msgClients,outputStream);
        System.out.println(System.currentTimeMillis()-start);
    }


    /**
     * 导出map数据
     * @throws IOException
     */
    @Test
    public void csvExportMap() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        long start = System.currentTimeMillis();
        CsvExportParams exportParams = new CsvExportParams(CsvExportParams.UTF8);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("clientName","client客户端" + i);
            map.put("birthday",new Date());
            map.put("clientPhone","client电话" + i);
            Map<String,Object> subMap = new HashMap<>();
            subMap.put("分组","分组：" + i);
            subMap.put("创建时间","创建于：" + new Date().toString());
            map.put("subMap",subMap);
            List<Map<String,Object>> subList = new ArrayList<>();
            for (int j = 0; j < new Random().nextInt(5); j++) {
                Map<String,Object> subListMap = new HashMap<>();
                subListMap.put("age",j+10);
                subListMap.put("性别",new Random().nextInt(1) == 1 ? "男" : "女");
                subList.add(subListMap);
            }
            map.put("subList",subList);
            mapList.add(map);

        }
        FileOutputStream outputStream = new FileOutputStream("./target/csvExportMap.csv");
        List<ExcelExportEntity> excelExportEntities = fillMapTitle();
        IWriter<Void> voidIWriter = CsvExportUtil.exportCsv(exportParams, excelExportEntities, outputStream);
        //write方法默认做了单批1000条处理
        voidIWriter.write(mapList);
        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }

    //封装map列表表头
    private List<ExcelExportEntity> fillMapTitle(){

        //封装一级表头
        List<ExcelExportEntity> excelExportEntities = new ArrayList<>();
        ExcelExportEntity excelExportEntity1 = new ExcelExportEntity();
        //表头名字
        excelExportEntity1.setName("客户端名称");
        //map中对应key名字
        excelExportEntity1.setKey("clientName");

        ExcelExportEntity excelExportEntity2 = new ExcelExportEntity();
        //表头名字
        excelExportEntity2.setName("生日");
        //map中对应key名字
        excelExportEntity2.setKey("birthday");

        ExcelExportEntity excelExportEntity3 = new ExcelExportEntity();
        //表头名字
        excelExportEntity3.setName("客户端电话");
        //map中对应key名字
        excelExportEntity3.setKey("clientPhone");
        excelExportEntities.add(excelExportEntity1);
        excelExportEntities.add(excelExportEntity2);
        excelExportEntities.add(excelExportEntity3);

        //封装list类型数据
        ExcelExportEntity subExcelExportEntity = new ExcelExportEntity();
        subExcelExportEntity.setKey("subList");
        subExcelExportEntity.setName("subList");
        excelExportEntities.add(subExcelExportEntity);

        ExcelExportEntity excelExportEntity4 = new ExcelExportEntity();
        //表头名字
        excelExportEntity4.setName("subMap");
        //map中对应key名字
        excelExportEntity4.setKey("subMap");
        excelExportEntities.add(excelExportEntity4);


        return excelExportEntities;
    }

}
