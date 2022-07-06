package com.lenged.system;

import com.sun.deploy.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
public class SystemApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(SystemApplication.class, args);
        ConfigurableEnvironment environment = app.getEnvironment();
        String port = environment.getProperty("server.port");
        System.out.println("swagger:http://localhost:"+port+"/swagger-ui/index.html#/");
    }

}
