package com.lenged.system.controller;

import cn.hutool.core.date.DateUtil;
import com.lenged.system.entity.User;
import com.lenged.system.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: MysqlController
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/7 13:47
 */
@Api(tags = "mysql数据库操作")
@RequestMapping("mysql")
@RestController
@Slf4j
public class MysqlController {

    @Autowired
    private UserMapper userMapper;

    @ApiOperation("查询所有用户")
    @GetMapping("getUsers")
    public List<User> getUsers(){
        log.info("current time:"+ DateUtil.now());
       return userMapper.selectList(null);
    }




}
