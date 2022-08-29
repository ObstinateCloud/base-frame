package com.lenged.system.hutool.excel;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.sax.handler.MapRowHandler;

import java.util.Map;

/**
 * @title: MyRowHandler
 * @description: 继承 MapRowHandler 拿到的数据没有标题行
 * @auther: zhangjianyun
 * @date: 2022/8/17 17:27
 */
public class MyRowHandler extends MapRowHandler {

    /**
     * 构造
     *
     * @param headerRowIndex 标题所在行（从0开始计数）
     * @param startRowIndex  读取起始行（包含，从0开始计数）
     * @param endRowIndex    读取结束行（包含，从0开始计数）
     */
    public MyRowHandler(int headerRowIndex, int startRowIndex, int endRowIndex) {
        super(headerRowIndex, startRowIndex, endRowIndex);
    }

    @Override
    public void handleData(int sheetIndex, long rowIndex, Map<String, Object> data) {
        Console.log("[{}] [{}] {}", sheetIndex, rowIndex, data);
    }

}
