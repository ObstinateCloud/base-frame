package com.lenged.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @title: UserDistrict
 * @description: TODO
 * @auther: 全国省市县
 * @date: 2022/7/7 14:31
 */

@Data
public class UserDistrict {

    private String code;

    private String name;

    private int level;

    private int status;

    private String uuid;

    private String creatorUuid;

    private String updaterUuid;

    private String cityCode;

    private String provinceCode;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//出参格式化
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//入参格式化
    private Date createdAt;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
