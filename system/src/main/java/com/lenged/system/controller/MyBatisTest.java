package com.lenged.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.lenged.system.config.MybaitsplusConfig;
import com.lenged.system.entity.User;
import com.lenged.system.entity.UserDistrict;
import com.lenged.system.mapper.UserDistrictMapper;
import com.lenged.system.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @title: MyBatisTest
 * @description:
 * 1.使用MybatisPlusTest的测试类时，包扫描注解（MapperScan）必须加到SystemApplication启动类上否则无法正常注入
 * 2.或者mybaits-plus的配置类引入到SystemApplication启动类
 * 3.目前多数据源执行单元测试时找不到数据源问题 需要修改配置为单数据源 目前不会修改
 * @auther: zhangjianyun
 * @date: 2022/7/7 14:13
 */
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//禁止myBatis使用内存数据库
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
    @Rollback(value = false) //解决单元测试中会出现事务自动回滚问题
    void setUserTest(){
        User user = new User();
        user.setAge(13);
        user.setEmail("gamil");
        user.setCreateDate(new Date());
        user.setName("user1");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test  //insert 语句会出现自动回滚问题
    void deleteUserTest(){
        int insert = userMapper.deleteById(1+"");
        System.out.println(insert);
    }

}
