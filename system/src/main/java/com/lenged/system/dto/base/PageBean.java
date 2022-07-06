package com.lenged.system.dto.base;

import lombok.Data;

import java.util.List;

/**
 * 分页查询javaBean
 */
@Data
public class PageBean<T> {
    //总记录数
    private int totalCount;

    //当前页数据
    private List<T> rows;
}
