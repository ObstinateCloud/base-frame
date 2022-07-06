package com.lenged.system.dto.base;

/**
 * @title: PageParam
 * @description: 分页参数
 * @auther: zhangjianyun
 * @date: 2022/4/1 19:01
 */
public class PageParam {

    private int pageNum = 1;

    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
