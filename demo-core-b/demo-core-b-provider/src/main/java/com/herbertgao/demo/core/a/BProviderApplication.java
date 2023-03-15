package com.herbertgao.demo.core.a;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class BProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BProviderApplication.class, args);
    }

}
