package com.lenged.system.controller;

import com.lenged.system.dto.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: RedisTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/6 16:43
 */
@Api(tags = "redis操作")
@RestController
@RequestMapping("redis")
public class RedisController {

    Logger logger = LoggerFactory.getLogger("recommend");


    @Autowired
    private  StringRedisTemplate stringRedisTemplate;

    @ApiOperation("存入String值")
    @GetMapping("/set/{key}/{value}")
    public BaseResponse setValue(@PathVariable("key") String key, @PathVariable("value") String value) {
        logger.info("key:{},value:{}",key,value);
        stringRedisTemplate.opsForValue().set(key, value);
        return BaseResponse.success();
    }

    @ApiOperation("取String值")
    @GetMapping("/get/{key}")
    public BaseResponse getValue(@PathVariable("key") String key) {
        logger.info("key:{}",key);
        return BaseResponse.success(stringRedisTemplate.opsForValue().get(key));
    }


}
