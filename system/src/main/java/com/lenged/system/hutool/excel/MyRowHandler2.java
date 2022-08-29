package com.lenged.system.hutool.excel;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.sax.handler.AbstractRowHandler;

import java.util.List;
import java.util.Map;

/**
 * @title: MyRowHandler
 * @description: 继承 AbstractRowHandler 可以自定义处理逻辑 可以拿到标题行
 * @auther: zhangjianyun
 * @date: 2022/8/17 17:27
 */
public class MyRowHandler2 extends AbstractRowHandler<Map<String, Object>> {

    private  int headerRowIndex;

    List<String> headerList;

    /**
     * 构造
     *
     * @param startRowIndex 读取起始行（包含，从0开始计数）
     * @param endRowIndex   读取结束行（包含，从0开始计数）
     */
    public MyRowHandler2(int startRowIndex, int endRowIndex,int headerRowIndex) {
        super(startRowIndex, endRowIndex);
        this.headerRowIndex = headerRowIndex;
        this.convertFunc = (rowList)-> IterUtil.toMap(headerList, rowList);
    }

    @Override
    public void handle(int sheetIndex, long rowIndex, List rowList) {
        if (rowIndex == this.headerRowIndex) {
            this.headerList = ListUtil.unmodifiable(Convert.toList(String.class, rowList));
            return;
        }
        super.handle(sheetIndex, rowIndex, rowList);
    }

    @Override
    public void handleData(int sheetIndex, long rowIndex, Map<String, Object> data) {
        Console.log("[{}] [{}] {}", sheetIndex, rowIndex, data);
    }

    public List<String> getHeaderList(){
        return headerList;
    }

}
