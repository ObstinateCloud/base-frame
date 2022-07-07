package com.lenged.system.entity;

import lombok.Data;

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
}
