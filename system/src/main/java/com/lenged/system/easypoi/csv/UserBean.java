package com.lenged.system.easypoi.csv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: UserBean
 * @description: 实体字段必须要有@Excel注解
 *               嵌套实体必要有ExcelEntity
 *               列表实体必要有ExcelCollection
 *               否则实体无法赋值
 * @auther: zhangjianyun
 * @date: 2022/8/15 14:37
 */
@Data
public class UserBean  {

    // 如果csv中标题与字段不对应，可以使用注解设置别名
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "sex")
    private String sex;
    @Excel(name = "focus")
    private String focus;
    @Excel(name = "age")
    private Integer age;


}
