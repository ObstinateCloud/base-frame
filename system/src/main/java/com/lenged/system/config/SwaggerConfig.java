package com.lenged.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @title: SwaggerConfig
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/6 14:26
 */

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                ;
    }

    ApiInfo apiInfo() {
        Contact contact = new Contact("lengedyun", "https://blog.csdn.net/qq_32429805", "1114592799@qq.com");
        ArrayList arrayList = new ArrayList();
        arrayList.add("多学一点儿知识，少些两行代码");
        ApiInfo apiInfo = new ApiInfo(
                "spring 集成基础框架",
                "用于快速启动项目开发的基框架，集成常用中间件",
                "v1.0.0",
                "urn:tos",
                contact,
                "github addr",
                "https://github.com/ObstinateCloud/base-frame",new ArrayList<>()
        );

        return apiInfo;
    }
}
