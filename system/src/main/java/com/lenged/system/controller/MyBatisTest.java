package com.lenged.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.lenged.system.entity.User;
import com.lenged.system.entity.UserDistrict;
import com.lenged.system.mapper.UserDistrictMapper;
import com.lenged.system.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

/**
 * @title: MyBatisTest
 * @description:
 * 1.使用MybatisPlusTest的测试类时，包扫描注解（MapperScan）必须加到SystemApplication启动类上否则无法正常注入
 * 2.或者mybaits-plus的配置类引入到SystemApplication启动类
 * @auther: zhangjianyun
 * @date: 2022/7/7 14:13
 */
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//禁止myBatista使用内存数据库
public class MyBatisTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDistrictMapper userDistrictMapper;



    @Test
    void getUsersTest(){
        List<User> users = userMapper.selectList(null);
        users.forEach(p->{
            System.out.println(p.toString());
        });
    }

    @Test
    void getUserDistrictTest(){
        LambdaQueryWrapper<UserDistrict> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(UserDistrict::getName,"南");
        List<UserDistrict> users = userDistrictMapper.selectList(queryWrapper);
        users.forEach(p->{
            System.out.println(p.toString());
        });
        Long aLong = userDistrictMapper.selectCount(queryWrapper);
        System.out.println(aLong);

    }

    @Test
    void getUserDistrictPageTest(){
        //分页插件，分页需要配置分页拦截器
        LambdaQueryWrapper<UserDistrict> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(UserDistrict::getName,"南");
        IPage<UserDistrict> page  = new Page<>();
        page.setSize(20);
        page.setPages(2);
        IPage<UserDistrict> pageRes = userDistrictMapper.selectPage(page,queryWrapper);
        pageRes.getRecords().forEach(p->{
            System.out.println(p.toString());
        });
        System.out.println("page:"+pageRes.getPages());
        System.out.println("size:"+pageRes.getSize());
        System.out.println("total:"+pageRes.getTotal());
        System.out.println("current:"+pageRes.getCurrent());

    }

    @Test
    void setUserTest(){


    }

}
