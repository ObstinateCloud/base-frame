package com.lenged.system.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.lenged.system.es.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @title: ESNewClientController
 * @description: 基于新版java api客户端
 * @auther: zhangjianyun
 * @date: 2022/7/29 9:45
 */


@RestController
@RequestMapping("esNew")
@Api(tags = "es 新版客户端测试接口")
public class ESNewClientController {

    @Autowired
    ElasticsearchClient esNewClient;

    @ApiOperation(value = "新版esClient创建索引")
    @GetMapping("esClientCreateIndex")
    public IndexResponse esClientCreateIndex(String index) throws IOException {

        SysUser sysUser = SysUser.builder()
                .id("11")
                .username("user111")
                .password("pwd11")
                .build();

        IndexResponse response = esNewClient.index(i -> i
                .index(index)
                .id("1")
                .document(sysUser)
        );


        return response;
    }
}
