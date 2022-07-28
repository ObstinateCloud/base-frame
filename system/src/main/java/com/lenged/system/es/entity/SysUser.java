package com.lenged.system.es.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @title: SysUser
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/5/7 11:06
 */

@Document(indexName = "sys_user")   //索引名
@Data
@Builder
public class SysUser {

    @Id //主键
    private String id;  //ES中id不能定义为Long
    private String username;
    private String password;
    private int level;
    @Field(type = FieldType.Keyword)
    private List<String> roles;
}
