package com.lenged.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @title: User
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/7 13:45
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//出参格式化
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//入参格式化
    private Date createDate;
}
