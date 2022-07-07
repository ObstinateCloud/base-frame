package com.lenged.system;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lenged.system.config.MybaitsplusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

@Import(value = {MybaitsplusConfig.class})
@SpringBootApplication
public class SystemApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(SystemApplication.class, args);
        ConfigurableEnvironment environment = app.getEnvironment();
        String port = environment.getProperty("server.port");
        System.out.println("swagger:http://localhost:"+port+"/swagger-ui/index.html#/");
    }

}
