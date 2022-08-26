package com.lenged.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;
    private String name;
    private Integer age;
    private String email;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//出参格式化
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//入参格式化
    private Date createDate;
}
