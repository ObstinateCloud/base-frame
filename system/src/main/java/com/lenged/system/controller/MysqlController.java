package com.lenged.system.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.lenged.system.entity.User;
import com.lenged.system.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @ApiOperation("查询所有用户-主库")
    @GetMapping("getUsersMaster")
    public List<User> getUsersMaster(){
        log.info("current time:"+ DateUtil.now());
       return userMapper.selectList(null);
    }

    @ApiOperation("查询所有用户-从库")
    @GetMapping("getSlaveUsers")
    @DS("config-db")
    public List<User> getSlaveUsers(){
        log.info("current time:"+ DateUtil.now());
        return userMapper.selectList(null);
    }

    @ApiOperation("新增用户-主库")
    @GetMapping("setUser")
    public int insertUser(){
        User user = new User();
        user.setAge(13);
        user.setEmail("gamil");
        user.setCreateDate(new Date());
        user.setName("user1");
        int insert = userMapper.insert(user);
        System.out.println(insert);
        return insert;
    }




}
