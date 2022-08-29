package com.lenged.system.hutool.excel;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @title: Employee
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/18 9:38
 */
@Data
public class Employee {

    @Alias("姓名")
    private String name;

    @Alias("部门")
    private String depart;

    @Alias("岗位")
    private String title;

    @Alias("职责")
    private String duty;

    @Alias("参与项目")
    private String project;

    @Alias("备注")
    private String remark;
}
