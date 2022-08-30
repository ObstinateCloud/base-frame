package com.lenged.system.easypoi.excel;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.handler.inter.IWriter;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.lenged.system.easypoi.csv.MsgClient;
import com.lenged.system.easypoi.csv.MsgClientGroup;
import com.lenged.system.easypoi.csv.UserBean;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @title: ExcelTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/29 17:08
 */
public class ExcelTest {

    @Test
    public void importToBean(){
        ImportParams importParams = new ImportParams();
        File file = new File("src/main/resources/template/excel/test.xlsx");
        List<EmployeeEasyPoi> objects = ExcelImportUtil.importExcel(file, EmployeeEasyPoi.class, importParams);
        objects.forEach(p->{
            System.out.println(p.toString());
//            p.keySet().forEach(k->{
//                System.out.println("key:"+k+",value:"+p.get(k));
//            });
        });



    }

    @Test
    public void exportToMap() throws Exception{

        ImportParams importParams = new ImportParams();
        File file = new File("src/main/resources/template/excel/test.xlsx");
//        List<Map<String,Object>> objects = ExcelImportUtil.importExcel(file, Map.class, importParams);
//        objects.forEach(p->{
//            System.out.println(p.toString());
//        });


        /**
         * 增加简单校验
         */

        FileInputStream fileInputStream = new FileInputStream(file);
        //要求下面的列必须存在
        importParams.setImportFields(new String[]{"姓名","部门","岗位","备注"});
        importParams.setVerifyHandler(new MyIExcelVerifyHandler());
        ExcelImportResult<Map<String,Object>> objectExcelImportResult = ExcelImportUtil.importExcelMore(fileInputStream, Map.class, importParams);
        List<Map<String,Object>> list = objectExcelImportResult.getList();
        list.forEach(p->{
            System.out.println(p);
        });
        System.out.println("--------------");
        objectExcelImportResult.getFailList().forEach(p->{
            System.out.println(p);
        });

    }

    class MyIExcelVerifyHandler implements IExcelVerifyHandler<Map<String,Object>>{

        @Override
        public ExcelVerifyHandlerResult verifyHandler(Map<String, Object> obj) {
            ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
            if(obj.get("备注") ==null){
                result.setSuccess(false);
                result.setMsg("备注不能为空");
            }
            return result;
        }
    }

    /**
     * 导出复杂bean
     */
    @Test
    public void exportBean() throws IOException {
        long start = System.currentTimeMillis();
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("excelExportBean");
        exportParams.setSheetName("sheet666");
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
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MsgClient.class, msgClients);
        FileOutputStream outputStream = new FileOutputStream("./target/excelExportBean.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }

    /**
     * 导出一个表格包含多个sheet页
     * @throws IOException
     */
    @Test
    public void excelExportMap() throws IOException {
        long start = System.currentTimeMillis();

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



        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("excelExportMap1");
        exportParams.setSheetName("sheet666");

        ExportParams exportParams2 = new ExportParams();
        exportParams2.setTitle("excelExportMap2");
        exportParams2.setSheetName("sheet777");

        Map<String,Object> excelMap1 = new HashMap<>();
        excelMap1.put("title",exportParams);
        excelMap1.put("entity",MsgClient.class);
        excelMap1.put("data",msgClients);

        Map<String,Object> excelMap2 = new HashMap<>();
        excelMap2.put("title",exportParams2);
        excelMap2.put("entity",MsgClient.class);
//        注意此处原始数据列表会被清空 所以需要拷贝 如果两个sheet页使用不同数据，则不会存在这个问题
        excelMap2.put("data", ((ArrayList<MsgClient>) msgClients).clone());



        List<Map<String,Object>> excelList = new ArrayList<>();
        excelList.add(excelMap1);
        excelList.add(excelMap2);
        Workbook workbook = ExcelExportUtil.exportExcel(excelList, ExcelType.XSSF);
        FileOutputStream outputStream = new FileOutputStream("./target/excelExportMap.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        //验证原始数据列表会被清空
        System.out.println(msgClients.size());

        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }


    /**
     * 导出map数据
     * @throws IOException
     */
    @Test
    public void excelExportMap2() throws IOException {
        long start = System.currentTimeMillis();
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

        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("excelExportMap");
        exportParams.setSheetName("sheet666");
        exportParams.setType(ExcelType.XSSF);


        List<ExcelExportEntity> entityList = new ArrayList<>();
        ExcelExportEntity excelExportEntity1 = new ExcelExportEntity("客户端名称","clientName");
        excelExportEntity1.setNeedMerge(true);
        ExcelExportEntity excelExportEntity2 = new ExcelExportEntity("生日","birthday");
        ExcelExportEntity excelExportEntity3 = new ExcelExportEntity("客户端电话","clientPhone");
        ExcelExportEntity excelExportEntity4 = new ExcelExportEntity("用户分组","subMap");
        entityList.add(excelExportEntity1);
        entityList.add(excelExportEntity2);
        entityList.add(excelExportEntity3);
        entityList.add(excelExportEntity4);
        ExcelExportEntity excelExportEntity5 = new ExcelExportEntity("用户列表","subList");
        ExcelExportEntity excelExportEntity5_1 = new ExcelExportEntity("age","age");
        ExcelExportEntity excelExportEntity5_2 = new ExcelExportEntity("性别","性别");
        excelExportEntity5.setList(Arrays.asList(excelExportEntity5_1,excelExportEntity5_2));
        entityList.add(excelExportEntity5);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,entityList,mapList);
        FileOutputStream outputStream = new FileOutputStream("./target/excelExportMap.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }

    /**
     * 大数据量导出  一般用于百万行以上
     * @throws IOException
     */
    @Test
    public void bigExcelExport() throws IOException {
        long start = System.currentTimeMillis();
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("excelExportMap");
        exportParams.setSheetName("sheet666");
        exportParams.setType(ExcelType.XSSF);

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

        IWriter<Workbook> workbookIWriter = ExcelExportUtil.exportBigExcel(exportParams, MsgClient.class);
        workbookIWriter.write(msgClients);
        workbookIWriter.close();
        Workbook workbook = workbookIWriter.get();
        FileOutputStream outputStream = new FileOutputStream("./target/bigExcelExport.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        System.out.println("耗时："+(System.currentTimeMillis()-start));

    }




    /**
     * 通过模板导出
     * @throws IOException
     */
    @Test
    public void templateExcelExport() throws IOException {
        long start = System.currentTimeMillis();
        TemplateExportParams templateExportParams = new TemplateExportParams();
        templateExportParams.setTemplateUrl("template/excel/excelExportTemplate.xlsx");
        templateExportParams.setSheetNum(new Integer[]{0});

        Map<String,Object> dataMap = new HashMap<>();
        Map<String,Object> subMap = new HashMap<>();
        subMap.put("a",9);
        subMap.put("b",18);
//        Map<String,Object> sonMap = new HashMap<>();
//        sonMap.put("c","mapList");
//        subMap.put("sonMap",sonMap);
        dataMap.put("subMap",subMap);
        dataMap.put("numType","汉字没问题");
        dataMap.put("aa",818);
        dataMap.put("bb",886);

        List<Map<String,Object>> userList = new ArrayList<>();
        Map<String,Object> userMap1 = new HashMap<>();
        userMap1.put("id",1);
        userMap1.put("name","user1");
        userMap1.put("sex","manmark");
        userList.add(userMap1);
        Map<String,Object> userMap2 = new HashMap<>();
        userMap2.put("id",2);
        userMap2.put("name","user2");
        userMap2.put("sex","manmark");
        userList.add(userMap2);
        Map<String,Object> userMap3 = new HashMap<>();
        userMap3.put("id",3);
        userMap3.put("name","user3");
        userMap3.put("sex","manmark3");
        userList.add(userMap3);
        dataMap.put("userList",userList);
        Workbook workbook = ExcelExportUtil.exportExcel(templateExportParams,dataMap);
        FileOutputStream outputStream = new FileOutputStream("./target/templateExcelExportRes.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        System.out.println("耗时："+(System.currentTimeMillis()-start));

    }

}
