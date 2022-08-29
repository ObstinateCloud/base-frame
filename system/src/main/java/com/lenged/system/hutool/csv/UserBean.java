package com.lenged.system.hutool.csv;

import cn.hutool.core.annotation.Alias;
import lombok.Builder;
import lombok.Data;

/**
 * @title: UserBean
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/15 14:37
 */
@Data
@Builder
public class UserBean {

    // 如果csv中标题与字段不对应，可以使用alias注解设置别名
    @Alias("姓名")
    private String name;
    private String sex;
    private String focus;
    private Integer age;
}
