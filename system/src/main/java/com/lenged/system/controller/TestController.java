package com.lenged.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @title: TestController
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/6 10:40
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试接口控制器")
public class TestController {

    @GetMapping("getCurrentTime")
    @ApiOperation("获取当前时间")
    public String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String dateStr = dateFormat.format(new Date());
        return "CurrentTime:"+dateStr;
    }
}
