package com.lenged.system.easypoi.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @title: Employee
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/18 9:38
 */
@Data
public class EmployeeEasyPoi {

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "部门")
    private String depart;

    @Excel(name = "岗位")
    private String title;

    @Excel(name = "职责")
    private String duty;
    /**
     * isImportField = "true" 导入是会校验这个字段是否存在，不存在则标记excel不合法
     * 如果是map类型，则通过设置导入参数来实现校验 ImportParams.setImportFields
     */
    @Excel(name = "参与项目",isImportField = "true")
    private String project;

    @Excel(name = "备注")
    private String remark;
}
